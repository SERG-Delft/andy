package nl.tudelft.cse1110.codechecker;

import nl.tudelft.cse1110.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.codechecker.engine.ScriptParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CodeCheckerTestUtils {
    public String getTestResource(String fixtureName) {
        return resourcePathRoot() + "fixtures/" + fixtureName;
    }

    private String resourcePathRoot() {
        return this.getClass().getResource("/codechecker/").getPath();
    }

    public CheckScript getYamlConfig(String cfgFile) {
        String fullYamlPath = getTestResource(cfgFile);
        try {
            String yaml = new String(Files.readAllBytes(Paths.get(fullYamlPath)));
            return new ScriptParser().parse(yaml);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
