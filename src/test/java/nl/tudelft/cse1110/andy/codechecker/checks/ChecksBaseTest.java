package nl.tudelft.cse1110.andy.codechecker.checks;

import nl.tudelft.cse1110.andy.codechecker.CodeCheckerTestUtils;
import nl.tudelft.cse1110.andy.codechecker.JDTParser;

public class ChecksBaseTest {

    protected void run(String className, Check check) {
        new JDTParser().run(new CodeCheckerTestUtils().getTestResource(className), check);
    }

}
