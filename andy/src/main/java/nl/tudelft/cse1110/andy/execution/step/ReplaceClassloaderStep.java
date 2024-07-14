package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.execution.Context.Context;
import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.result.ResultBuilder;
import org.objenesis.ObjenesisStd;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This step replaces the classloader with one that sees the folder
 * with the compiled classes.
 */
public class ReplaceClassloaderStep implements ExecutionStep {

    @Override
    public void execute(Context ctx, ResultBuilder result) {
        DirectoryConfiguration dirCfg = ctx.getDirectoryConfiguration();

        try {
            String pathToAddToClassloader = dirCfg.getWorkingDir();
            replaceClassloader(ctx, pathToAddToClassloader);
            ctx.setClassloaderWithStudentsCode(Thread.currentThread().getContextClassLoader());
            clearMockitoObjenesisInstantiatorCache();
        } catch (Exception e) {
            result.genericFailure(this, e);
        }
    }

    private void replaceClassloader(Context ctx, String pathToAddToClassloader) {
        List<Path> additionalClasspathEntries = Arrays.asList(Paths.get(pathToAddToClassloader));
        URL[] urls = additionalClasspathEntries.stream().map(this::toURL).toArray(URL[]::new);
        ClassLoader currentClassloader = ctx.getCleanClassloader();
        ClassLoader customClassLoader = URLClassLoader.newInstance(urls, currentClassloader);

        Thread.currentThread().setContextClassLoader(customClassLoader);
    }

    private void clearMockitoObjenesisInstantiatorCache() throws ReflectiveOperationException {
            Class<?> defaultInstantiatorProviderClass = Class.forName(
                    "org.mockito.internal.creation.instance.DefaultInstantiatorProvider");
            Field objenesisInstantiatorField = defaultInstantiatorProviderClass.getDeclaredField("INSTANCE");
            objenesisInstantiatorField.setAccessible(true);

            // org.mockito.internal.creation.instance.ObjenesisInstantiator
            Object objenesisInstantiator = objenesisInstantiatorField.get(null);
            Field objenesisStdField = objenesisInstantiator.getClass().getDeclaredField("objenesis");
            objenesisStdField.setAccessible(true);

            // org.objenesis.ObjenesisStd extends ObjenesisBase
            ObjenesisStd objenesisStd = (ObjenesisStd) objenesisStdField.get(objenesisInstantiator);
            Field objenesisBaseCacheField = objenesisStd.getClass().getSuperclass().getDeclaredField("cache");
            objenesisBaseCacheField.setAccessible(true);

            // clear instantiator cache
            ConcurrentHashMap<?, ?> cache = (ConcurrentHashMap<?, ?>) objenesisBaseCacheField.get(objenesisStd);
            cache.clear();
    }

    private URL toURL(Path path) {
        try {
            return path.toUri().toURL();
        }
        catch (Exception ex) {
            throw new RuntimeException("Invalid classpath entry: " + path, ex);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ReplaceClassloaderStep;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
