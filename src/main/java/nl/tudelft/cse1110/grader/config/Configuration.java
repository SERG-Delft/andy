package nl.tudelft.cse1110.grader.config;

import nl.tudelft.cse1110.codechecker.engine.CheckScript;

import java.util.List;

public interface Configuration {
    String getWorkingDir();
    String getLibrariesDir();
    String getReportsDir();
    CheckScript getCodeCheckerScript();

    void setNewClassNames(List<String> fullClassNames);
    List<String> getNewClassNames();

    String getMainLibraryClass();
}
