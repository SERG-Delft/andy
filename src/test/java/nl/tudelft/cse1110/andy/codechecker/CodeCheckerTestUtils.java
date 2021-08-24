package nl.tudelft.cse1110.andy.codechecker;

import static nl.tudelft.cse1110.andy.TestResourceUtils.resourceFolder;

public class CodeCheckerTestUtils {
    public String getTestResource(String fixtureName) {
        return resourceFolder("/codechecker/fixtures/") + fixtureName;
    }

}
