package delft;

import nl.tudelft.cse1110.andy.config.MetaTest;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.mode.Mode;
import nl.tudelft.cse1110.andy.execution.externalprocess.CommandExternalProcess;
import nl.tudelft.cse1110.andy.execution.externalprocess.ExternalProcess;

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
                MetaTest.withStringReplacement("[1] Possible to select more seats than are on the plane",
                        "/MauricioAirways/index.html",
                        "/config/metatest-1/index.html"),
                MetaTest.withStringReplacement("[2] Number of free seats does not get updated in the UI after booking",
                        "/MauricioAirways/index.html",
                        "/config/metatest-2/index.html"),
                MetaTest.withStringReplacement("[3] Reserved ticket isn't appended to table",
                        "/MauricioAirways/index.html",
                        "/config/metatest-3/index.html"),
                MetaTest.withStringReplacement("[4] Bookings are made for the wrong flight",
                        "/MauricioAirways/index.html",
                        "/config/metatest-4/index.html")

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
