package io.github.cse1110.andy;

import com.google.common.io.Files;
import nl.tudelft.cse1110.andy.Andy;
import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.execution.Context.Context;
import nl.tudelft.cse1110.andy.execution.Context.ContextBuilder;
import nl.tudelft.cse1110.andy.execution.Context.ContextDirector;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.result.Result;
import nl.tudelft.cse1110.andy.writer.standard.StandardResultWriter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.PrintStream;
import java.util.*;

import static nl.tudelft.cse1110.andy.utils.FilesUtils.*;

@Mojo(name = "run",
        requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME,
        defaultPhase = LifecyclePhase.CLEAN)
public class AndyMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    protected MavenProject project;

    @Parameter(property = "full")
    private boolean full;

    private PrintStream out;

    @Override
    public void execute() {
        out = System.out;

        this.printHeader();

        File basedir = project.getBasedir();

        File workDir = null;
        try {
            /**
             * We should first create a temporary work directory and
             * copy the student solution, the config file, and the library code
             * to it.
             */
            workDir = Files.createTempDir();

            Collection<File> javaFiles = getJavaFiles(basedir);
            for (File javaFile : javaFiles) {
                copyFile(javaFile.getAbsolutePath(), workDir.getAbsolutePath());
            }

            /* We get the list of dependencies, to help the Andy's Java compiler to find them all */
            List<String> compileClasspathElements = project.getCompileClasspathElements();

            /**
             * Create an output directory where we generate all the reports.
             * We delete it first, because there might be one already, from a
             * previous execution.
             */
            File outputDir = new File(concatenateDirectories(basedir.getAbsolutePath(), "reports"));
            if(outputDir.exists()) {
                deleteDirectory(outputDir);
            }
            createDirIfNeeded(outputDir.getAbsolutePath());

            /* Start printing dots */
            Timer workingIndicator = this.startWorkingIndicationTimer();

            /* Run Andy! */
            ContextDirector director = new ContextDirector(new ContextBuilder());
            Context ctx = director.constructWithLibraries(
                    action(),
                    new DirectoryConfiguration(workDir.getAbsolutePath(), outputDir.getAbsolutePath()),
                    compileClasspathElements
            );

            Result result = new Andy().run(ctx);

            new StandardResultWriter().write(ctx, result);

            /* Read output file */
            String output = readFile(new File(concatenateDirectories(outputDir.getAbsolutePath(), "stdout.txt")));

            /* Stop printing dots */
            workingIndicator.cancel();
            out.println("\n\n");

            /* Print output */
            out.println(output);
            out.println("\n\nCheck branch and mutation coverage in the /reports folder!\n\n");

        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            System.exit(-1);
        } finally {
            // Delete the work dir as it's not needed anymore
            if(workDir!=null)
                deleteDirectory(workDir);
        }
    }

    private Action action() {
        if(full)
            return Action.FULL_WITH_HINTS;

        return Action.FULL_WITHOUT_HINTS;
    }

    private Collection<File> getJavaFiles(File basedir) {
        Collection<File> javaFilesInSrc = getAllJavaFiles(basedir.getAbsolutePath() + "/src");
        Collection<File> javaFilesInConfig = getAllJavaFiles(basedir.getAbsolutePath() + "/config");
        Collection<File> javaFiles = new ArrayList<>();
        javaFiles.addAll(javaFilesInSrc);
        javaFiles.addAll(javaFilesInConfig);
        return javaFiles;
    }

    private void printHeader() {
        out.println(
                """
    
         _              _       
        / \\   _ __   __| |_   _ 
       / _ \\ | '_ \\ / _` | | | |
      / ___ \\| | | | (_| | |_| |
     /_/   \\_\\_| |_|\\__,_|\\__, |
                          |___/ 
    
            """);
    }

    private Timer startWorkingIndicationTimer() {
        Timer timer = new Timer(true);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                out.print('.');
            }
        }, 0, 1000);

        return timer;
    }

}