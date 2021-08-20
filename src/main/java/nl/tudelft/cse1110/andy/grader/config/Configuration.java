package nl.tudelft.cse1110.andy.grader.config;

public class Configuration {

    private final ClassLoader cleanClassloader;
    private DirectoryConfiguration directoryConfiguration = null;
    private RunConfiguration runConfiguration = null;
    private long startTime;

    public Configuration() {
        this.cleanClassloader = Thread.currentThread().getContextClassLoader();
        this.startTime = System.nanoTime();
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

    public ClassLoader getCleanClassloader() {
        return cleanClassloader;
    }
}
