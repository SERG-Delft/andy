package nl.tudelft.cse1110.andy.execution;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.externalprocess.EmptyExternalProcess;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.execution.mode.ModeActionSelector;
import nl.tudelft.cse1110.andy.execution.externalprocess.ExternalProcess;
import org.jacoco.core.runtime.IRuntime;
import org.jacoco.core.runtime.RuntimeData;

import java.util.List;

public class Context {

    private final ClassLoader cleanClassloader;
    private DirectoryConfiguration directoryConfiguration = null;
    private RunConfiguration runConfiguration;
    private List<String> fullClassNames;
    private ExecutionFlow flow;
    private final Action action;
    private ModeActionSelector modeActionSelector;
    private ExternalProcess externalProcess;
    private ClassLoader classloaderWithStudentsCode;
    private List<String> librariesToBeIncludedInCompilation;
    private IRuntime jacocoRuntime;
    private RuntimeData jacocoData;

    public Context(Action action) {
        this(Thread.currentThread().getContextClassLoader(), action);
    }

    public Context(ClassLoader cleanClassloader, Action action) {
        this.cleanClassloader = cleanClassloader;
        this.action = action;
        this.externalProcess = new EmptyExternalProcess();
    }

    public void setFlow(ExecutionFlow flow) {this.flow = flow;}

    public ExecutionFlow getFlow() {
        return flow;
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

    public ClassLoader getCleanClassloader() {
        return cleanClassloader;
    }

    public void setNewClassNames(List<String> fullClassNames) {
        this.fullClassNames = fullClassNames;
    }

    public List<String> getNewClassNames() {
        return fullClassNames;
    }

    public Action getAction() {
        return action;
    }

    public void setModeSelector(ModeActionSelector modeActionSelector) {
        this.modeActionSelector = modeActionSelector;
    }

    public ModeActionSelector getModeActionSelector() {
        return modeActionSelector;
    }

    public ExternalProcess getExternalProcess() {
        return externalProcess;
    }

    public void setExternalProcess(ExternalProcess externalProcess) {
        this.externalProcess = externalProcess;
    }

    public void killExternalProcess() {
        // Retrieve error messages before killing process
        externalProcess.extractErrorMessages();

        externalProcess.kill();
    }

    public void setClassloaderWithStudentsCode(ClassLoader classloaderWithStudentsCode) {
        this.classloaderWithStudentsCode = classloaderWithStudentsCode;
    }

    public ClassLoader getClassloaderWithStudentsCode() {
        return classloaderWithStudentsCode;
    }

    public void setLibrariesToBeIncludedInCompilation(List<String> librariesToBeIncludedInCompilation) {
        this.librariesToBeIncludedInCompilation = librariesToBeIncludedInCompilation;
    }

    public List<String> getLibrariesToBeIncludedInCompilation() {
        return librariesToBeIncludedInCompilation;
    }

    public boolean hasLibrariesToBeIncluded() {
        return librariesToBeIncludedInCompilation!=null && !librariesToBeIncludedInCompilation.isEmpty();
    }

    public void setJacocoObjects(IRuntime runtime, RuntimeData data) {
        jacocoRuntime = runtime;
        jacocoData = data;
    }

    public boolean hasJacocoRuntime() {
        return jacocoRuntime != null && jacocoData != null;
    }

    public IRuntime getJacocoRuntime() {
        return jacocoRuntime;
    }

    public RuntimeData getJacocoData() {
        return jacocoData;
    }
}
