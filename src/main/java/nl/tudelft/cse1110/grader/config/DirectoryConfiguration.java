package nl.tudelft.cse1110.grader.config;

import java.util.List;

public class DirectoryConfiguration {

    private final String workingDir;
    private final String reportsDir;
    private List<String> fullClassNames;

    public DirectoryConfiguration(String workingDir, String reportsDir) {
        this.workingDir = workingDir;
        this.reportsDir = reportsDir;
    }

    public String getWorkingDir() {
        return this.workingDir;
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
