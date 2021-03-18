/*
 * Copyright (C) 2020 LitterBox contributors
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
package de.uni_passau.fim.se2.litterbox.analytics;

import de.uni_passau.fim.se2.litterbox.analytics.FeatureExtractor;
import de.uni_passau.fim.se2.litterbox.analytics.metric.AvgScriptWidthCount;
import de.uni_passau.fim.se2.litterbox.analytics.metric.*;
import de.uni_passau.fim.se2.litterbox.ast.model.*;
import de.uni_passau.fim.se2.litterbox.ast.visitor.ScratchBlocksVisitor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FeatureTool {

    private List<FeatureExtractor> metrics = Arrays.asList(
            new AvgScriptWidthCount(),
            new AvgBlockStatementCount(),
            new MaxBlockStatementCount(),
            new BlockCount(),
            new MaxScriptWidthCount(),
            new NestedBlockCount(),
            new StackedStatementCount(),
            new StatementCount(),
            new VariableCount(),
            new AvgVariableLengthCount(),
            new MaxVariableLengthCount(),
            new MotionBlockCount(),
            new LooksBlockCount(),
            new SoundBlockCount(),
            new EventsBlockCount(),
            new ControlBlockCount(),
            new SensingBlockCount(),
            new OperatorsBlockCount(),
            new VariablesBlockCount(),
            new MyBlocksBlockCount()
    );

    public List<String> getMetricNames() {
        return metrics.stream().map(FeatureExtractor::getName).collect(Collectors.toList());
    }

    public List<FeatureExtractor> getAnalyzers() {
        return Collections.unmodifiableList(metrics);
    }

    public void createCSVFile(Program program, String fileName) throws IOException {
        List<String> headers = new ArrayList<>();
        headers.add("project");
        headers.add("id");
        metrics.stream().map(FeatureExtractor::getName).forEach(headers::add);
        headers.add("scratch_block_code");
        CSVPrinter printer = getNewPrinter(fileName, headers);
        int count = 0;
        List<ActorDefinition> actorDefinitions = getActors(program);
        for (ActorDefinition actorDefinition : actorDefinitions) {
            ScriptList scripts = actorDefinition.getScripts();
            if (scripts != null) {

                for (Script script : scripts.getScriptList()) {
                    List<String> row = new ArrayList<>();
                    count = count + 1;
                    row.add(program.getIdent().getName());
                    String uniqueID = program.toString().replace("de.uni_passau.fim.se2.litterbox.ast.model.", "")
                            + script.toString().replace("de.uni_passau.fim.se2.litterbox.ast.model.", "");
                    row.add(uniqueID + count);
                    for (FeatureExtractor extractor : metrics) {
                        row.add(Double.toString(extractor.calculateMetric(script)));
                    }
                    String stringScratchCode = getScratchBlockCode(script);
                    row.add(stringScratchCode);
                    printer.printRecord(row);
                }
            }
        }
        printer.flush();
    }

    private String getScratchBlockCode(Script script) {
        ScratchBlocksVisitor visitor = new ScratchBlocksVisitor();
        visitor.begin();
        script.accept(visitor);
        visitor.end();
        return visitor.getScratchBlocks();
    }

    // TODO: Code clone -- same is in CSVReportGenerator
    protected CSVPrinter getNewPrinter(String name, List<String> heads) throws IOException {

        if (Files.exists(Paths.get(name))) {
            BufferedWriter writer = Files.newBufferedWriter(
                    Paths.get(name), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return new CSVPrinter(writer, CSVFormat.DEFAULT.withSkipHeaderRecord());
        } else {
            BufferedWriter writer = Files.newBufferedWriter(
                    Paths.get(name), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(heads.toArray(new String[0])));
        }
    }

    private List<ActorDefinition> getActors(Program program) {
        ActorDefinitionList actorDefinitionList = program.getActorDefinitionList();
        return actorDefinitionList.getDefinitions();
    }
}
