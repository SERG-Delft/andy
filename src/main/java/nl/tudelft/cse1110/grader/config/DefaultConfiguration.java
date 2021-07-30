package nl.tudelft.cse1110.grader.config;

import nl.tudelft.cse1110.codechecker.engine.CheckScript;

import java.util.List;

public class DefaultConfiguration implements Configuration {

    private final String workingDir;
    private final String librariesDir;
    private final String reportsDir;
    private final CheckScript codeCheckerScript;
    private List<String> fullClassNames;
    private String mainLibraryClass;
    private String metaDir;

    public DefaultConfiguration(String mainLibraryClass, String workingDir, String librariesDir, String reportsDir, String metaDir, CheckScript codeCheckerScript) {
        this.mainLibraryClass = mainLibraryClass;
        this.workingDir = workingDir;
        this.librariesDir = librariesDir;
        this.reportsDir = reportsDir;
        this.codeCheckerScript = codeCheckerScript;
        this.metaDir = metaDir;
    }

    public DefaultConfiguration(String mainLibraryClass, String workingDir, String librariesDir, String reportsDir, CheckScript codeCheckerScript) {
        this(mainLibraryClass, workingDir, librariesDir, reportsDir, null, codeCheckerScript);
    }

    public DefaultConfiguration(String workingDir, String librariesDir, String reportsDir, CheckScript codeCheckerScript) {
        this(null, workingDir, librariesDir, reportsDir, null, codeCheckerScript);
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
    public CheckScript getCodeCheckerScript() {
        return this.codeCheckerScript;
    }

    @Override
    public void setNewClassNames(List<String> fullClassNames) {
        this.fullClassNames = fullClassNames;
    }

    @Override
    public List<String> getNewClassNames() {
        return fullClassNames;
    }

    @Override
    public String getMainLibraryClass() {
        return this.mainLibraryClass;
    }

    @Override
    public String getMetaDir() {
        return this.metaDir;
    }
}
