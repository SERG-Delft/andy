package unit.codechecker;

import static testutils.TestResourceUtils.resourceFolder;

public class CodeCheckerTestUtils {
    public String getTestResource(String fixtureName) {
        return resourceFolder("/codechecker/fixtures/") + fixtureName;
    }

}
