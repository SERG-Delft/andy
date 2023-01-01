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

    public int numberOfMutationsToConsider() {
        return 15;
    }    

    @Override
    public Map<String, Float> weights() {
        return new HashMap<>() {{
            put("coverage", 0.2f);
            put("mutation", 0.2f);
            put("meta", 0.6f);
            put("codechecks", 0f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.AutoAssigner", "delft.Workshop");
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
            MetaTest.withStringReplacement(
                "check that number of spots goes down",
                "spotsPerDate.put(date, spotsPerDate.get(date) - 1);",
                "spotsPerDate.put(date, spotsPerDate.get(date));"
            ),
            MetaTest.withStringReplacement(
                "checks different dates",
                ".findFirst()",
                ".skip(1).findFirst()"
            ),
            MetaTest.withStringReplacement(
                "tries date with single spot",
                "public boolean hasAvailableDate() {",
                "public boolean hasAvailableDate() { if(spotsPerDate.values().stream().anyMatch(v -> v == 1)) throw new RuntimeException();"
            ),
            MetaTest.withStringReplacement(
                "all assignments failed",
                "return assignments;",
                "if(!assignments.getErrors().isEmpty() && assignments.getAssignments().isEmpty()) throw new RuntimeException(); return assignments;"
            ),
            MetaTest.withStringReplacement(
                "check the failed assignments in Assignments",
                "assignments.noAvailableSpots(workshop, student);",
                ""
            ),
            MetaTest.withStringReplacement(
                "check assignments in Assignments",
                "assignments.assign(workshop, student, nextDate);",
                ""
            )
        );
    }

}
