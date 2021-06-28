package nl.tudelft.cse1110.grader.config;

import java.util.List;

public interface Configuration {
    String getWorkingDir();
    String getLibrariesDir();
    String getReportsDir();

    void setNewClassNames(List<String> fullClassNames);
    List<String> getNewClassNames();
}
