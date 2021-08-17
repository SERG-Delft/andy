package nl.tudelft.cse1110.codechecker;

import static nl.tudelft.cse1110.ResourceUtils.resourceFolder;

public class CodeCheckerTestUtils {
    public String getTestResource(String fixtureName) {
        return resourceFolder("/codechecker/fixtures/") + fixtureName;
    }

}
