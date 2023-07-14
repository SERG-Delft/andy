package delft;

import nl.tudelft.cse1110.andy.config.MetaTest;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.mode.Mode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration extends RunConfiguration {

    @Override
    public Mode mode() {
        return Mode.PRACTICE;
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of();
    }

    @Override
    public Map<String, Float> weights() {
        return new HashMap<>() {{
            put("coverage", 0.0f);
            put("mutation", 0.0f);
            put("meta", 1.0f);
            put("codechecks", 0.0f);
        }};
    }
    
    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.withStringReplacement(2, "[1] Event is not added",
                    "/TodaysEvents/index.html",
                    "/config/metatest-1/index.html"),
                MetaTest.withStringReplacement(2, "[2] Description is not set",
                    "/TodaysEvents/index.html",
                    "/config/metatest-2/index.html"),
                MetaTest.withStringReplacement(2, "[3] Start and end time are swapped",
                    "/TodaysEvents/index.html",
                    "/config/metatest-3/index.html"),
                MetaTest.withStringReplacement("[4] Events are duplicated",
                    "/TodaysEvents/index.html",
                    "/config/metatest-4/index.html"),
                MetaTest.withStringReplacement("[5] Error is shown even when adding an event succeeds",
                    "/TodaysEvents/index.html",
                    "/config/metatest-5/index.html"),
                MetaTest.withStringReplacement("[6] Adding a second event deletes the first one",
                    "/TodaysEvents/index.html",
                    "/config/metatest-6/index.html"),
                MetaTest.withStringReplacement("[7] Cannot add a second event",
                    "/TodaysEvents/index.html",
                    "/config/metatest-7/index.html"),
                // MetaTest.withStringReplacement("[8] Allows left overlap",
                //     "/TodaysEvents/index.html",
                //     "/config/metatest-8/index.html"),
                MetaTest.withStringReplacement(2, "[8] Allows right overlap",
                    "/TodaysEvents/index.html",
                    "/config/metatest-9/index.html")
                // MetaTest.withStringReplacement("[10] Allows inner overlap",
                //     "/TodaysEvents/index.html",
                //     "/config/metatest-10/index.html"),
                // MetaTest.withStringReplacement("[11] Allows outer overlap",
                //     "/TodaysEvents/index.html",
                //     "/config/metatest-11/index.html")
        );
    }

    @Override
    public boolean skipJacoco() {
        return true;
    }

    @Override
    public boolean skipPitest() {
        return true;
    }
}
