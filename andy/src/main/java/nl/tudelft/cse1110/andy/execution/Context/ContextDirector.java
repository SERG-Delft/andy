package nl.tudelft.cse1110.andy.execution.Context;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.execution.externalprocess.EmptyExternalProcess;
import nl.tudelft.cse1110.andy.execution.mode.Action;

import java.util.List;

public class ContextDirector {
    private ContextBuilder contextBuilder;

    public ContextDirector(ContextBuilder contextBuilder) {
        this.contextBuilder = contextBuilder;
    }

    public Context constructBase(Action action, DirectoryConfiguration directoryConfiguration) {
        this.setBase(action, directoryConfiguration);

        Context ctx = contextBuilder.buildContext();

        // reset builder
        contextBuilder = new ContextBuilder();
        return ctx;
    }

    public Context constructWithLibraries(Action action, DirectoryConfiguration directoryConfiguration,
                                          List<String> librariesToBeIncludedInCompilation) {
        this.setBase(action, directoryConfiguration);
        contextBuilder.setLibrariesToBeIncludedInCompilation(librariesToBeIncludedInCompilation);

        Context ctx = contextBuilder.buildContext();

        // reset builder
        contextBuilder = new ContextBuilder();
        return ctx;
    }

    public Context constructWithLibrariesAndClassLoader(ClassLoader cleanClassloader, Action action,
                                                        DirectoryConfiguration directoryConfiguration,
                                                        List<String> librariesToBeIncludedInCompilation) {
        this.setBase(action, directoryConfiguration);
        contextBuilder.setCleanClassloader(cleanClassloader);
        contextBuilder.setLibrariesToBeIncludedInCompilation(librariesToBeIncludedInCompilation);

        Context ctx = contextBuilder.buildContext();

        // reset builder
        contextBuilder = new ContextBuilder();
        return ctx;
    }

    private void setBase(Action action, DirectoryConfiguration directoryConfiguration) {
        contextBuilder.setCleanClassloader(Thread.currentThread().getContextClassLoader());
        contextBuilder.setAction(action);
        contextBuilder.setDirectoryConfiguration(directoryConfiguration);
        contextBuilder.setExternalProcess(new EmptyExternalProcess());
    }
}
