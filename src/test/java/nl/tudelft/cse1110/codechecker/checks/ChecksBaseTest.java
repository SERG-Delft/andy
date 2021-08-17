package nl.tudelft.cse1110.codechecker.checks;

import nl.tudelft.cse1110.codechecker.JDTParser;
import nl.tudelft.cse1110.codechecker.CodeCheckerTestUtils;

public class ChecksBaseTest {

    protected void run(String className, Check check) {
        new JDTParser().run(new CodeCheckerTestUtils().getTestResource(className), check);
    }

}
