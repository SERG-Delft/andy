package nl.tudelft.cse1110.grader.config;

public class Configuration {

    private DirectoryConfiguration directoryConfiguration = null;
    private RunConfiguration runConfiguration = null;
    private long startTime;

    public Configuration(long startTime) {
        this.startTime = startTime;
    }

    public DirectoryConfiguration getDirectoryConfiguration() {
        return directoryConfiguration;
    }

    public RunConfiguration getRunConfiguration() {
        return runConfiguration;
    }

    public void setDirectoryConfiguration(DirectoryConfiguration directoryConfiguration) {
        this.directoryConfiguration = directoryConfiguration;
    }

    public void setRunConfiguration(RunConfiguration runConfiguration) {
        this.runConfiguration = runConfiguration;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

}
