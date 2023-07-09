package nl.tudelft.cse1110.andy.execution.Context;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.ExecutionFlow;
import nl.tudelft.cse1110.andy.execution.externalprocess.ExternalProcess;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.execution.mode.ModeActionSelector;
import org.jacoco.core.runtime.IRuntime;
import org.jacoco.core.runtime.RuntimeData;

import java.util.List;

public class ContextBuilder {
    private ClassLoader cleanClassloader;
    private DirectoryConfiguration directoryConfiguration;
    private RunConfiguration runConfiguration;
    private List<String> fullClassNames;
    private ExecutionFlow flow;
    private Action action;
    private ModeActionSelector modeActionSelector;
    private ExternalProcess externalProcess;
    private ClassLoader classloaderWithStudentsCode;
    private List<String> librariesToBeIncludedInCompilation;
    private IRuntime jacocoRuntime;
    private RuntimeData jacocoData;

    public void setCleanClassloader(ClassLoader cleanClassloader) {
        this.cleanClassloader = cleanClassloader;
    }

    public void setDirectoryConfiguration(DirectoryConfiguration directoryConfiguration) {
        this.directoryConfiguration = directoryConfiguration;
    }

    public void setRunConfiguration(RunConfiguration runConfiguration) {
        this.runConfiguration = runConfiguration;
    }

    public void setFlow(ExecutionFlow flow) {
        this.flow = flow;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setExternalProcess(ExternalProcess externalProcess) {
        this.externalProcess = externalProcess;
    }

    public void setLibrariesToBeIncludedInCompilation(List<String> librariesToBeIncludedInCompilation) {
        this.librariesToBeIncludedInCompilation = librariesToBeIncludedInCompilation;
    }

    public Context buildContext() {
        return new Context(cleanClassloader, directoryConfiguration, runConfiguration, fullClassNames, flow,
                action, modeActionSelector, externalProcess, classloaderWithStudentsCode,
                librariesToBeIncludedInCompilation, jacocoRuntime, jacocoData);
    }
}
