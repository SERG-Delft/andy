package com.github.cse1110.andy;

import com.google.common.io.Files;
import nl.tudelft.cse1110.andy.Andy;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.Collection;

import static nl.tudelft.cse1110.andy.utils.FilesUtils.*;

@Mojo(name = "andy", requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class AndyMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    protected MavenProject project;

    @Parameter(property = "action", defaultValue = "FULL_WITHOUT_HINTS")
    private String action;

    @Override
    public void execute() {
        File basedir = project.getBasedir();

        File workDir = null;
        try {
            /**
             * We should first create a temporary work directory and
             * copy the student solution, the config file, and the library code
             * to it.
             */
            workDir = Files.createTempDir();

            Collection<File> javaFiles = getAllJavaFiles(basedir.getAbsolutePath());
            for (File javaFile : javaFiles) {
                copyFile(javaFile.getAbsolutePath(), workDir.getAbsolutePath());
            }

            /**
             * Create an output directory where we generate all the reports.
             * We delete it first, because there might be one already, from a
             * previous execution.
             */
            File outputDir = new File(concatenateDirectories(basedir.getAbsolutePath(), "output"));
            if(outputDir.exists()) {
                deleteDirectory(outputDir);
            }
            createDirIfNeeded(outputDir.getAbsolutePath());

            /* Run Andy! */
            new Andy(action, workDir.getAbsolutePath(), outputDir.getAbsolutePath()).assess();

            /* Read output file */
            String output = readFile(new File(concatenateDirectories(outputDir.getAbsolutePath(), "stdout.txt")));
            System.out.println(output);

        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            System.exit(-1);
        } finally {
            // Delete the work dir as it's not needed anymore
            if(workDir!=null)
                deleteDirectory(workDir);
        }
    }

}
