package nl.tudelft.cse1110.codechecker.integration;

import nl.tudelft.cse1110.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.codechecker.CodeCheckerTestUtils;

public class IntegrationTestBase {

    protected CheckScript script(String fileName) {
        return new CodeCheckerTestUtils().getYamlConfig(fileName);
    }

}
