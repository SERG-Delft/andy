package nl.tudelft.cse1110.codechecker.integration;

import nl.tudelft.cse1110.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.codechecker.engine.TestUtils;

public class IntegrationTestBase {

    protected CheckScript script(String fileName) {
        return new TestUtils().getYamlConfig(fileName);
    }

}
