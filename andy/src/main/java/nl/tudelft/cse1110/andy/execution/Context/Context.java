package nl.tudelft.cse1110.andy.execution.Context;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.ExecutionFlow;
import nl.tudelft.cse1110.andy.execution.externalprocess.EmptyExternalProcess;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.execution.mode.ModeActionSelector;
import nl.tudelft.cse1110.andy.execution.externalprocess.ExternalProcess;
import org.jacoco.core.runtime.IRuntime;
import org.jacoco.core.runtime.RuntimeData;

import java.util.List;

public class Context {

    private final ClassLoader cleanClassloader;
    private final DirectoryConfiguration directoryConfiguration;
    private RunConfiguration runConfiguration;
    private List<String> fullClassNames;
    private ExecutionFlow flow;
    private final Action action;
    private ModeActionSelector modeActionSelector;
    private ExternalProcess externalProcess;
    private ClassLoader classloaderWithStudentsCode;
    private final List<String> librariesToBeIncludedInCompilation;
    private IRuntime jacocoRuntime;
    private RuntimeData jacocoData;

    public Context(ClassLoader cleanClassloader, DirectoryConfiguration directoryConfiguration,
                   RunConfiguration runConfiguration, List<String> fullClassNames, ExecutionFlow flow,
                   Action action, ModeActionSelector modeActionSelector, ExternalProcess externalProcess,
                   ClassLoader classloaderWithStudentsCode, List<String> librariesToBeIncludedInCompilation,
                   IRuntime jacocoRuntime, RuntimeData jacocoData) {
        this.cleanClassloader = cleanClassloader;
        this.directoryConfiguration = directoryConfiguration;
        this.runConfiguration = runConfiguration;
        this.fullClassNames = fullClassNames;
        this.flow = flow;
        this.action = action;
        this.modeActionSelector = modeActionSelector;
        this.externalProcess = externalProcess;
        this.classloaderWithStudentsCode = classloaderWithStudentsCode;
        this.librariesToBeIncludedInCompilation = librariesToBeIncludedInCompilation;
        this.jacocoRuntime = jacocoRuntime;
        this.jacocoData = jacocoData;
    }

    public void setFlow(ExecutionFlow flow) {this.flow = flow;}

    public void setRunConfiguration(RunConfiguration runConfiguration) {
        this.runConfiguration = runConfiguration;
    }

    public void setNewClassNames(List<String> fullClassNames) {
        this.fullClassNames = fullClassNames;
    }

    public void setModeSelector(ModeActionSelector modeActionSelector) {
        this.modeActionSelector = modeActionSelector;
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

    public void setJacocoObjects(IRuntime runtime, RuntimeData data) {
        jacocoRuntime = runtime;
        jacocoData = data;
    }

    public boolean hasJacocoRuntime() {
        return jacocoRuntime != null && jacocoData != null;
    }

    public ExecutionFlow getFlow() {
        return flow;
    }

    public DirectoryConfiguration getDirectoryConfiguration() {
        return directoryConfiguration;
    }

    public RunConfiguration getRunConfiguration() {
        return runConfiguration;
    }

    public ClassLoader getCleanClassloader() {
        return cleanClassloader;
    }

    public List<String> getNewClassNames() {
        return fullClassNames;
    }

    public Action getAction() {
        return action;
    }

    public ModeActionSelector getModeActionSelector() {
        return modeActionSelector;
    }

    public ExternalProcess getExternalProcess() {
        return externalProcess;
    }

    public ClassLoader getClassloaderWithStudentsCode() {
        return classloaderWithStudentsCode;
    }

    public List<String> getLibrariesToBeIncludedInCompilation() {
        return librariesToBeIncludedInCompilation;
    }

    public boolean hasLibrariesToBeIncluded() {
        return librariesToBeIncludedInCompilation!=null && !librariesToBeIncludedInCompilation.isEmpty();
    }

    public IRuntime getJacocoRuntime() {
        return jacocoRuntime;
    }

    public RuntimeData getJacocoData() {
        return jacocoData;
    }

}
