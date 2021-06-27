package nl.tudelft.cse1110.grader.config;

import java.util.List;

public class DefaultConfiguration implements Configuration {

    private final String sourceCodeDir;
    private final String cpLibraries;
    private List<String> fullClassNames;

    public DefaultConfiguration(String sourceCodeDir, String cpLibraries) {
        this.sourceCodeDir = sourceCodeDir;
        this.cpLibraries = cpLibraries;
    }

    @Override
    public String getSourceCodeDir() {
        return this.sourceCodeDir;
    }

    @Override
    public String getLibrariesDir() {
        return this.cpLibraries;
    }

    @Override
    public void setNewClassNames(List<String> fullClassNames) {
        this.fullClassNames = fullClassNames;

    }

    @Override
    public List<String> getNewClassNames() {
        return fullClassNames;
    }
}
