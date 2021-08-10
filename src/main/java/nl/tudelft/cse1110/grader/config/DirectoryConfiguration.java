package nl.tudelft.cse1110.grader.config;

import nl.tudelft.cse1110.codechecker.engine.CheckScript;

import java.util.List;

public class DirectoryConfiguration {

    private final String workingDir;
    private final String librariesDir;
    private final String reportsDir;
    private List<String> fullClassNames;

    public DirectoryConfiguration(String workingDir, String librariesDir, String reportsDir) {
        this.workingDir = workingDir;
        this.librariesDir = librariesDir;
        this.reportsDir = reportsDir;
    }

    public String getWorkingDir() {
        return this.workingDir;
    }

    public String getLibrariesDir() {
        return this.librariesDir;
    }

    public String getReportsDir() {
        return reportsDir;
    }

    public void setNewClassNames(List<String> fullClassNames) {
        this.fullClassNames = fullClassNames;
    }

    public List<String> getNewClassNames() {
        return fullClassNames;
    }
}
