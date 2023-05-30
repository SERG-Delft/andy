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
            put("coverage", 0.0f);
            put("mutation", 0.3f);
            put("meta", 0.6f);
            put("codechecks", 0.1f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.EnrollParticipantInOfferingService");
    }

    @Override
    public CheckScript checkScript() {
        return new CheckScript(List.of(
                new SingleCheck("Activity should not be mocked", true, new MockClass("Activity")),
                new SingleCheck("Offering should not be mocked", true, new MockClass("Offering")),
                new SingleCheck("Enrollment should not be mocked", true, new MockClass("Enrollment")),
                new SingleCheck("Participant should not be mocked", true, new MockClass("Participant"))
        ));
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
            MetaTest.withStringReplacement(
                "Enrollment created with wrong offering",
                "Enrollment newEnrollment = new Enrollment(this, participant, ZonedDateTime.now());",
                "Enrollment newEnrollment = new Enrollment(null, participant, ZonedDateTime.now());"
            ),
            MetaTest.withStringReplacement(
                "Enrollment created with wrong participant",
                "Enrollment newEnrollment = new Enrollment(this, participant, ZonedDateTime.now());",
                "Enrollment newEnrollment = new Enrollment(this, null, ZonedDateTime.now());"
            ),
            MetaTest.withStringReplacement(
                "Enrollment created with wrong date",
                "Enrollment newEnrollment = new Enrollment(this, participant, ZonedDateTime.now());",
                "Enrollment newEnrollment = new Enrollment(this, participant, null);"
            ),
            MetaTest.withStringReplacement(
                "Enrollment created with wrong date 2",
                "Enrollment newEnrollment = new Enrollment(this, participant, ZonedDateTime.now());",
                "Enrollment newEnrollment = new Enrollment(this, participant, ZonedDateTime.now().plusHours(1));"
            ),

            MetaTest.withStringReplacement(
                "Don't verity email builder",
                "emailBuilder.createEnrollmentEmail(activity, offering, newEnrollment);",
                ""
            ),

            MetaTest.withStringReplacement(
                "Don't verity offerings save",
                "offerings.save(offering);",
                ""
            ),

            MetaTest.withStringReplacement(
                "Don't verity validation",
                "validation.backToUserInCaseOfErrors();",
                ""
            )
        );
    }

}
