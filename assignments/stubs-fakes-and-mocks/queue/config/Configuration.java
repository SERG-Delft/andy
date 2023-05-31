package delft;

import nl.tudelft.cse1110.andy.codechecker.checks.*;
import nl.tudelft.cse1110.andy.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.andy.codechecker.engine.SingleCheck;
import nl.tudelft.cse1110.andy.config.MetaTest;
import nl.tudelft.cse1110.andy.config.RunConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration extends RunConfiguration {

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
        return List.of("delft.TheQueue");
    }

    @Override
    public CheckScript checkScript() {
        return new CheckScript(List.of(
                new SingleCheck("Request Service should be mocked",
                        new MockClass("RequestService")),
                new SingleCheck( "TheQueue should not be mocked", true,
                        new MockClass("TheQueue")),
                new SingleCheck( "getRequestsByCourse should be set up",
                        new MockitoWhen("getRequestsByCourse", Comparison.GTE, 1)),
                new SingleCheck("getRequestsByCourse should not be verified", true,
                        new MockitoVerify("getRequestsByCourse", MockitoVerify.MethodType.TEST,
                                Comparison.GTE, 1)),
                new SingleCheck("tests should have assertions",
                        new TestMethodsHaveAssertions()),
                new SingleCheck("Spies should not be used", true,
                        new MockitoSpy())
        ));
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.withLineReplacement("does not check if course is not there", 35, 36, ""),
                MetaTest.withLineReplacement("does not check if course has no requests", 37, 38, "")
        );
    }

}


