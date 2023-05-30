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
    public List<String> classesUnderTest() {
        return List.of("delft.SportsHallPlanner");
    }

    @Override
    public Map<String, Float> weights() {
        return new HashMap<>() {{
            put("coverage", 0.2f);
            put("mutation", 0.2f);
            put("meta", 0.6f);
            put("codechecks", 0.0f);
        }};
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
            MetaTest.withLineReplacement("does not assert correct type of exception", 41, 41, "throw new RuntimeException(\"There should be no duplicate elements in the halls list.\");"),
            MetaTest.withLineReplacement("does not test order of sports hall assignment", 47, 47, "Request current = nextRequests.remove(nextRequests.size() - 1);"),
            MetaTest.withLineReplacement("does not test unsuitable halls", 50, 50, "if (hall.canFulfillRequest(current) || true) {"),
            MetaTest.withLineReplacement("does not test earlier suitable hall needed by later one (backtracking goes wrong)", 46, 46, "List<Request> nextRequests = requests;"),
            MetaTest.withLineReplacement("does not catch that a hall is used multiple times", 52, 52, ""),
            MetaTest.withLineReplacement("does not test unfeasible requests list", 62, 62, "return new HashMap<>();"),
            MetaTest.withLineReplacement("does not test empty request list", 44, 44, "return null;"),
            MetaTest.withLineReplacement("does not test list with single element", 45, 45, "} if(requests.size() == 1) throw new RuntimeException();")
        );
    }
}

