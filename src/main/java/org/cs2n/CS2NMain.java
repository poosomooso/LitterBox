package org.cs2n;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;
import de.uni_passau.fim.se2.litterbox.analytics.BugAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

import static de.uni_passau.fim.se2.litterbox.utils.GroupConstants.ALL;
import static de.uni_passau.fim.se2.litterbox.utils.GroupConstants.DEFAULT;

public class CS2NMain {

    public static void main(String[] args) throws IOException {
        String test_file = "../Vacuum348/GOOD_348_C4C30_session_data.json";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(new File(test_file));
        Iterator<JsonNode> frames = root.get(0).get("frames").elements();
        for (int i = 0; i < 730; i++) {
            frames.next();
        }
//        int i = 0;
//        while (frames.hasNext()) {
//            System.out.println(i);
//            JsonNode f = frames.next();
//            JsonNode target = f.get("state_info").get("program").get("targets").get(0).get("blocks");
//            System.out.println(target);
//            i += 1;
//        }
        analyze(frames.next(), objectMapper);




//        Iterator<FramePair> framePairIterator = consecutivePairsIterator(frames);

    }

    static void analyze(JsonNode frame, ObjectMapper objectMapper) throws IOException {
        JsonNode target = frame.get("state_info").get("program");
        String outputPath = null;
        String detectors = ALL;
        System.out.println(objectMapper.writeValueAsString(target));
        String fname = "temp_program.json";
        objectMapper.writeValue(new File(fname), target);
        BugAnalyzer analyzer = new BugAnalyzer(fname, outputPath, detectors, false, false);
        analyzer.analyzeFile();

//        if (cmd.hasOption(ANNOTATE)) {
//            String annotationPath = cmd.getOptionValue(ANNOTATE);
//            analyzer.setAnnotationOutput(annotationPath);
//        }
//
//        runAnalysis(cmd, analyzer);
    }
    static Iterator<FramePair> consecutivePairsIterator(Iterator<JsonNode> frames) {
        return new Iterator<FramePair>() {
            JsonNode prev = frames.next();
            @Override
            public boolean hasNext() {
                return frames.hasNext();
            }

            @Override
            public FramePair next() {
                JsonNode nextFrame = frames.next();
                FramePair fp = new FramePair(prev, nextFrame);
                this.prev = nextFrame;
                return fp;

            }
        };
    }

    static class FramePair {
        public final JsonNode before, after;
        public FramePair(JsonNode b, JsonNode a) {
            this.before = b;
            this.after = a;
        }
    }

}
