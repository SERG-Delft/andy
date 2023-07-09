package nl.tudelft.cse1110.andy.execution;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.execution.mode.Action;

import java.util.List;

public class ContextDirector {
    private ContextBuilder contextBuilder;

    public ContextDirector(ContextBuilder contextBuilder) {
        this.contextBuilder = contextBuilder;
    }

    public Context constructWithLibraries(Action action, DirectoryConfiguration directoryConfiguration,
                                          List<String> librariesToBeIncludedInCompilation) {
        contextBuilder.setCleanClassloader(Thread.currentThread().getContextClassLoader());
        contextBuilder.setAction(action);
        contextBuilder.setDirectoryConfiguration(directoryConfiguration);
        contextBuilder.setLibrariesToBeIncludedInCompilation(librariesToBeIncludedInCompilation);
        Context ctx = contextBuilder.buildContext();

        // reset builder
        contextBuilder = new ContextBuilder();
        return ctx;
    }

    public Context constructWithDirectoryConfig(Action action, DirectoryConfiguration directoryConfiguration) {
        contextBuilder.setCleanClassloader(Thread.currentThread().getContextClassLoader());
        contextBuilder.setAction(action);
        contextBuilder.setDirectoryConfiguration(directoryConfiguration);
        Context ctx = contextBuilder.buildContext();

        // reset builder
        contextBuilder = new ContextBuilder();
        return ctx;
    }

    public Context constructWithClassLoader(ClassLoader cleanClassloader, Action action,
                                            DirectoryConfiguration directoryConfiguration,
                                            List<String> librariesToBeIncludedInCompilation) {
        contextBuilder.setCleanClassloader(cleanClassloader);
        contextBuilder.setAction(action);
        contextBuilder.setDirectoryConfiguration(directoryConfiguration);
        contextBuilder.setLibrariesToBeIncludedInCompilation(librariesToBeIncludedInCompilation);
        Context ctx = contextBuilder.buildContext();

        // reset builder
        contextBuilder = new ContextBuilder();
        return ctx;
    }
}
