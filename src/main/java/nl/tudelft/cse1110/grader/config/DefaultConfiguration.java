package nl.tudelft.cse1110.grader.config;

import java.util.List;

public class DefaultConfiguration implements Configuration {

    private final String workingDir;
    private final String librariesDir;
    private final String reportsDir;
    private List<String> fullClassNames;

    public DefaultConfiguration(String workingDir, String librariesDir, String reportsDir) {
        this.workingDir = workingDir;
        this.librariesDir = librariesDir;
        this.reportsDir = reportsDir;
    }

    @Override
    public String getWorkingDir() {
        return this.workingDir;
    }

    @Override
    public String getLibrariesDir() {
        return this.librariesDir;
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
