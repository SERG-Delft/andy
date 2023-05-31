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
            put("coverage", 0.2f);
            put("mutation", 0.2f);
            put("meta", 0.5f);
            put("codechecks", 0.1f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.GradeEmailService");
    }

    @Override
    public CheckScript checkScript() {
        return new CheckScript(List.of(
                new SingleCheck("StudentRepository should be mocked",
                        new MockClass("StudentRepository")),
                new SingleCheck("EmailService should be mocked",
                        new MockClass("EmailService")),
                new SingleCheck(2, "ErrorBuilder should not be mocked", true,
                        new MockClass("ErrorBuilder")),
                new SingleCheck("GradeEmailService should not be mocked", true,
                        new MockClass("GradeEmailService"))
        ));
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
            MetaTest.withLineReplacement(
                "does not check whether email is null",
                174, 181,
                "emails.add(new GradeEmail(address, course, grades.get(student)));"
            ),
            MetaTest.withLineReplacement(
                "does not add non-sendable email errors",
                187, 190,
                ""
            ),
            MetaTest.withLineReplacement(
                "does not include student number in error string",
                179, 179,
                "\"\""
            ),
            MetaTest.withLineReplacement(
                "does not add no matching email errors",
                177, 180,
                ""
            ),
            MetaTest.withLineReplacement(
                "does not include email in error string",
                189, 189,
                "e.getAddresses().stream().map(a -> \"\").collect(Collectors.toList())"
            ),
            MetaTest.withLineReplacement(
                "only executes loop once",
                181, 181,
                "} break;"
            ),
            MetaTest.withLineReplacement(
                "zero iterations in the loop",
                165, 165,
                "if(studentNumbers.isEmpty()) throw new RuntimeException();"
            ),
            MetaTest.withStringReplacement(
                "does not call sendEmails with the actual emails",
                "emailService.sendEmails(emails);",
                "emailService.sendEmails(List.of());"
            )
        );
    }

}
