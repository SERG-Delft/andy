package delft;

import nl.tudelft.cse1110.andy.codechecker.checks.*;
import nl.tudelft.cse1110.andy.codechecker.engine.*;
import nl.tudelft.cse1110.andy.config.MetaTest;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.mode.Mode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Configuration extends RunConfiguration {

    @Override
    public Mode mode() {
        return Mode.PRACTICE;
    }

    @Override
    public Map<String, Float> weights() {
        return new HashMap<>() {{
            put("coverage", 0.05f);
            put("mutation", 0.10f);
            put("meta", 0.85f);
            put("codechecks", 0.0f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.SpaceSignals");
    }

    @Override
    public CheckScript checkScript() {
        return new CheckScript(List.of(
        ));
    }


    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.withStringReplacement("Never returns -1 if there are matches",
                        "return sequenceCount >= 3 ? sequenceCount : -1",
                        "return sequenceCount > 0 ? sequenceCount : -1"),
                MetaTest.withStringReplacement("Does not return -1 if there are 0 matches",
                        "return sequenceCount >= 3 ? sequenceCount : -1",
                        "return (sequenceCount >= 3 || sequenceCount == 0) ? sequenceCount : -1"),
                MetaTest.withStringReplacement("Returns -1 if there are 3 matches",
                        "return sequenceCount >= 3 ? sequenceCount : -1",
                        "return sequenceCount > 3 ? sequenceCount : -1"),
                MetaTest.withStringReplacement("Does not detect match at the end of the list",
                        "for (int j = 0; j < pattern.size(); j++)",
                        "for (int j = 0; j < pattern.size() - 1; j++)"),
                MetaTest.withStringReplacement("Does not detect match at the beginning of the list",
                        "for (int j = 0; j < pattern.size(); j++)",
                        "for (int j = 1; j < pattern.size(); j++)"),
                MetaTest.insertAt(2, "Does not work correctly with large lists (i.e., avoid doing it manually)",
                        38,
                        "if (signals.size() > 20) throw new RuntimeException();"),
                MetaTest.insertAt(2, "Returns wrong amount if there are more than 5 matches  (i.e., avoid doing it manually)",
                        53,
                        "if (sequenceCount > 5) sequenceCount++;")
        );
    }

}
