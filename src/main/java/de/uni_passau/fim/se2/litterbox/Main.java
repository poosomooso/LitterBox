/*
 * Copyright (C) 2019 LitterBox contributors
 *
 * This file is part of LitterBox.
 *
 * LitterBox is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * LitterBox is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LitterBox. If not, see <http://www.gnu.org/licenses/>.
 */
package de.uni_passau.fim.se2.litterbox;


import de.uni_passau.fim.se2.litterbox.analytics.IssueTool;
import de.uni_passau.fim.se2.litterbox.analytics.Scratch3Analyzer;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import static de.uni_passau.fim.se2.litterbox.analytics.Scratch3Analyzer.removeEndSeparator;
import static de.uni_passau.fim.se2.litterbox.utils.GroupConstants.*;

public class Main {

    private static final String PATH = "path";
    private static final String INTERMEDIATE = "intermediate";
    private static final String PROJECTID = "projectid";
    private static final String PROJECTLIST = "projectlist";
    private static final String PROJECTOUT = "projectout";
    private static final String OUTPUT = "output";
    private static final String DETECTORS = "detectors";
    private static final String GROUP = "detectors";
    private static final String HELP = "help";

    private static final Logger log = Logger.getLogger(Main.class.getName());

    private Main() {
    }

    /**
     * Entry point to Litterbox where the arguments are parsed and the selected functionality is called.
     *
     * @param args Arguments that are parsed as options.
     * @throws ParseException thrown when a Scratch Project cannot be parsed.
     */
    public static void main(String[] args) throws ParseException {

        Options options = new Options();

        options.addOption(PATH, true, "path to folder or file that should be analyzed (required)");
        options.addOption(INTERMEDIATE, true, "path to a file or folder to which "
                + "the project(s) will be printed in the intermediate language");
        options.addOption(PROJECTID, true,
                "id of the project that should be downloaded and analysed.");
        options.addOption(PROJECTLIST, true, "path to a file with a list of project ids of projects"
                + " which should be downloaded and analysed.");
        options.addOption(PROJECTOUT, true, "path where the downloaded project(s) should be stored");
        options.addOption(OUTPUT, true, "path with name of the csv file you want to save (required if path argument"
                + " is a folder path)");
        options.addOption(DETECTORS, true, "name all detectors you want to run separated by ',' "
                + "\n(all detectors defined in the README)");
        options.addOption(GROUP, true, "choose a group of detectors to run smells, ctscore or bugs"
                + "\n(all detectors defined in the README)");
        options.addOption(HELP, false, "print this message");
        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption(INTERMEDIATE)) {
            if (cmd.hasOption(PROJECTOUT)) {
                String projectOut = removeEndSeparator(cmd.getOptionValue(PROJECTOUT));
                if (cmd.hasOption(PROJECTID)) {
                    String projectId = cmd.getOptionValue(PROJECTID);
                    Scratch3Analyzer.downloadAndPrint(projectId, projectOut,
                            cmd.getOptionValue(INTERMEDIATE));
                } else if (cmd.hasOption(PROJECTLIST)) {
                    String printPath = removeEndSeparator(cmd.getOptionValue(INTERMEDIATE));
                    Scratch3Analyzer.downloadAndPrintMultiple(
                            cmd.getOptionValue(PROJECTLIST), projectOut, printPath);
                }
            } else {
                Scratch3Analyzer.printIntermediate(cmd.getOptionValue(PATH), cmd.getOptionValue(INTERMEDIATE));
            }
            return;
        } else if (cmd.hasOption(PATH)) {
            File folder = new File(cmd.getOptionValue(PATH));
            if (cmd.hasOption(GROUP)) {
                Scratch3Analyzer.analyze(cmd.getOptionValue(GROUP),
                        cmd.getOptionValue(OUTPUT), folder);
            } else {
                Scratch3Analyzer.analyze(cmd.getOptionValue(DETECTORS, ALL),
                        cmd.getOptionValue(OUTPUT), folder);
            }
            return;
        } else if (cmd.hasOption(PROJECTID) || cmd.hasOption(PROJECTLIST)) {
            if (cmd.hasOption(PROJECTID)) {
                String projectid = cmd.getOptionValue(PROJECTID);
                if (cmd.hasOption(GROUP)) {
                    Scratch3Analyzer.downloadAndAnalyze(projectid, cmd.getOptionValue(PROJECTOUT),
                            cmd.getOptionValue(GROUP),
                            cmd.getOptionValue(OUTPUT));
                } else {
                    Scratch3Analyzer.downloadAndAnalyze(projectid, cmd.getOptionValue(PROJECTOUT),
                            cmd.getOptionValue(DETECTORS, ALL),
                            cmd.getOptionValue(OUTPUT));
                }
            }

            if (cmd.hasOption(PROJECTLIST)) {
                if (cmd.hasOption(GROUP)) {
                    Scratch3Analyzer.downloadAndAnalyzeMultiple(
                            cmd.getOptionValue(PROJECTLIST),
                            cmd.getOptionValue(PROJECTOUT),
                            cmd.getOptionValue(GROUP),
                            cmd.getOptionValue(OUTPUT));
                } else {
                    Scratch3Analyzer.downloadAndAnalyzeMultiple(
                            cmd.getOptionValue(PROJECTLIST),
                            cmd.getOptionValue(PROJECTOUT),
                            cmd.getOptionValue(DETECTORS, ALL),
                            cmd.getOptionValue(OUTPUT));
                }
            }
            return;
        }

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("LitterBox", options);
        System.out.println("Example: " + "java -jar Litterbox.jar -path "
                + "C:\\scratchprojects\\files\\ -output C:\\scratchprojects\\files\\test.csv -detectors bugs\n");

        System.out.println("Detectors:");
        ResourceBundle messages = ResourceBundle.getBundle("IssueDescriptions", Locale.ENGLISH);
        IssueTool iT = new IssueTool();
        System.out.printf("\t%-20s %-30s\n", ALL, messages.getString(ALL));
        System.out.printf("\t%-20s %-30s\n", BUGS, messages.getString(BUGS));
        System.out.printf("\t%-20s %-30s\n", SMELLS, messages.getString(SMELLS));
        System.out.printf("\t%-20s %-30s\n", CTSCORE, messages.getString(CTSCORE));
        iT.getAllFinder().keySet().forEach(finder -> System.out.printf(
                "\t%-20s %-30s\n",
                finder,
                messages.getString(finder)
        ));
    }
}
