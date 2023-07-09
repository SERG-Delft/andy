package unit.execution.Context;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.execution.Context.Context;
import nl.tudelft.cse1110.andy.execution.Context.ContextBuilder;
import nl.tudelft.cse1110.andy.execution.Context.ContextDirector;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ContextDirectorTest {

    private static ClassLoader classLoader;
    private static DirectoryConfiguration directoryConfiguration;
    private static Action action;
    private static List<String> librariesToBeIncludedInCompilation;

    @BeforeAll
    public static void initializeParameters() {
        classLoader = Thread.currentThread().getContextClassLoader();
        directoryConfiguration = new DirectoryConfiguration("working", "reports");
        action = Action.META_TEST;
        librariesToBeIncludedInCompilation = List.of("library1", "library2");
    }

    @Test
    void constructBase() {
        ContextBuilder builder = new ContextBuilder();
        ContextDirector director = new ContextDirector(builder);
        Context context = director.constructBase(action, directoryConfiguration);

        boolean hasBaseContextProperties = isBaseContext(context);
        boolean restIsNull = restIsNull(context);

        assertThat(hasBaseContextProperties).isTrue();
        assertThat(context.getLibrariesToBeIncludedInCompilation()).isNull();
        assertThat(restIsNull).isTrue();
    }

    @Test
    void constructWithLibraries() {
        ContextBuilder builder = new ContextBuilder();
        ContextDirector director = new ContextDirector(builder);
        Context context = director.constructWithLibraries(action, directoryConfiguration,
                librariesToBeIncludedInCompilation);

        boolean hasBaseContextProperties = isBaseContext(context);
        boolean restIsNull = restIsNull(context);

        assertThat(hasBaseContextProperties).isTrue();
        assertThat(context.getLibrariesToBeIncludedInCompilation()).isEqualTo(librariesToBeIncludedInCompilation);
        assertThat(restIsNull).isTrue();

    }


    @Test
    void constructWithLibrariesAndClassLoader() {
        ContextBuilder builder = new ContextBuilder();
        ContextDirector director = new ContextDirector(builder);
        Context context = director.constructWithLibraries(action, directoryConfiguration,
                librariesToBeIncludedInCompilation);

        boolean hasBaseContextProperties = isBaseContext(context);
        boolean restIsNull = restIsNull(context);

        assertThat(hasBaseContextProperties).isTrue();
        assertThat(context.getCleanClassloader()).isEqualTo(classLoader);
        assertThat(context.getLibrariesToBeIncludedInCompilation()).isEqualTo(librariesToBeIncludedInCompilation);
        assertThat(restIsNull).isTrue();

    }

    private static boolean isBaseContext(Context context) {
        return context != null
                && context.getAction().equals(action)
                && context.getDirectoryConfiguration().getWorkingDir().equals(directoryConfiguration.getWorkingDir())
                && context.getDirectoryConfiguration().getOutputDir().equals(directoryConfiguration.getOutputDir())
                && context.getCleanClassloader() != null
                && context.getExternalProcess() != null;
    }

    private static boolean restIsNull(Context context) {
        return context.getFlow() == null
                && context.getRunConfiguration() == null
                && context.getNewClassNames() == null
                && context.getClassloaderWithStudentsCode() == null
                && context.getJacocoData() == null
                && context.getJacocoRuntime() == null;
    }
}