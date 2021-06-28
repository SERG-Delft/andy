package nl.tudelft.cse1110.grader.config;

import java.util.List;

public class DefaultConfiguration implements Configuration {

    private final String sourceCodeDir;
    private final String cpLibraries;
    private final String reportsDir;
    private List<String> fullClassNames;

    public DefaultConfiguration(String sourceCodeDir, String librariesDir, String reportsDir) {
        this.sourceCodeDir = sourceCodeDir;
        this.cpLibraries = librariesDir;
        this.reportsDir = reportsDir;
    }

    @Override
    public String getSourceCodeDir() {
        return this.sourceCodeDir;
    }

    @Override
    public String getLibrariesDir() {
        return this.cpLibraries;
    }

    public String getReportsDir() {
        return reportsDir;
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
