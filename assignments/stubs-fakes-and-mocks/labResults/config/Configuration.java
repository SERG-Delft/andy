package delft;

import nl.tudelft.cse1110.andy.codechecker.checks.*;
import nl.tudelft.cse1110.andy.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.andy.codechecker.engine.SingleCheck;
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
    public Map<String, Float> weights() {
        return new HashMap<>() {{
            put("coverage", 0.25f);
            put("mutation", 0.25f);
            put("meta", 0.25f);
            put("codechecks", 0.25f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.MyHealth");
    }

    @Override
    public CheckScript checkScript() {
        return new CheckScript(List.of(
                new SingleCheck("Auth Service should be mocked",
                        new MockClass("AuthService")),
                new SingleCheck("Lab Result Repository should be mocked",
                        new MockClass("LabResultRepository")),
                new SingleCheck("Log should be mocked",
                        new MockClass("Log")),
                new SingleCheck( "MyHealth should not be mocked", true,
                        new MockClass("MyHealth")),
                new SingleCheck("tests should have assertions",
                        new TestMethodsHaveAssertions()),
                new SingleCheck("Spies should not be used", true,
                        new MockitoSpy())
        ));
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.withLineReplacement("does not verify logging if unauthorised", 64, 64, ""),
                MetaTest.withLineReplacement("does not verify logging if not present", 68, 70, ""),
                MetaTest.withLineReplacement("provides result even when not authorised", 65, 65, ""),
                MetaTest.withLineReplacement("does not check if user is authenticated", 63, 66, ""),
                MetaTest.withStringReplacement("does not check if null is returned", "return null", "return \"\"")
        );
    }

}
