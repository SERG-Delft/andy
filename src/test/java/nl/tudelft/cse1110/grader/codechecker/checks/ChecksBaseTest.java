package nl.tudelft.cse1110.grader.codechecker.checks;

import nl.tudelft.cse1110.grader.codechecker.JDTParser;
import nl.tudelft.cse1110.grader.codechecker.engine.TestUtils;

public class ChecksBaseTest {

//    private String filePathFor(String className) {
//        try {
//            return new File(this.getClass().getResource("/").getPath() + "../../src/test/java/fixtures/" + className).getCanonicalPath();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    protected void run(String className, Check check) {
        new JDTParser().run(new TestUtils().getFixtureFilePath(className), check);
    }

}
