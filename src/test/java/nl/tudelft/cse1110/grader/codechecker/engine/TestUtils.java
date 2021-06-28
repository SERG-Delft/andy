package nl.tudelft.cse1110.grader.codechecker.engine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestUtils {
    public String getFixtureFilePath(String fixtureName) {
        return resourcePathRoot() + "../../src/test/java/fixtures/" + fixtureName;
    }

    private String resourcePathRoot() {
        return this.getClass().getResource("/").getPath();
    }

    public CheckScript getYamlConfig(String cfgFile) {
        String fullYamlPath = resourcePathRoot() + "../../src/test/java/fixtures/" + cfgFile;
        try {
            String yaml = new String(Files.readAllBytes(Paths.get(fullYamlPath)));
            return new ScriptParser().parse(yaml);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
