package nl.tudelft.cse1110.andy.grader.execution.step;

import nl.tudelft.cse1110.andy.grader.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.grader.execution.Context;
import nl.tudelft.cse1110.andy.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.grader.result.Highlight;
import nl.tudelft.cse1110.andy.grader.result.ResultBuilder;
import nl.tudelft.cse1110.andy.grader.util.FilesUtils;
import nl.tudelft.cse1110.andy.grader.util.ImportUtils;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementScanner9;
import javax.tools.*;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static javax.tools.Diagnostic.Kind.ERROR;

/**
 * This step compiles the student code and the library code.
 * It makes use of the Java Compiler API.
 */
public class CompilationStep implements ExecutionStep {


    @Override
    public void execute(Context ctx, ResultBuilder result) {
        DirectoryConfiguration dirCfg = ctx.getDirectoryConfiguration();

        /*
         * creates the java compiler and diagnostic collector object
         * using just the standard configuration. Nothing to optimize here, I believe.
         */
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager manager = compiler.getStandardFileManager(
                diagnostics, null, null );

        /*
         * Create a compilation task with the list of files to compile.
         * The compiler uses the classpath of the current JVM.
         * So, all the libraries available (JUnit, etc) will be reused here
         */
        Collection<File> listOfFiles = FilesUtils.getAllJavaFiles(dirCfg.getWorkingDir());
        result.debug(this, String.format("Files for compilation: %d (%s)", listOfFiles.size(),
                listOfFiles.stream().map(c -> c.getName()).collect(Collectors.joining(","))));

        Iterable<? extends JavaFileObject > sources =
                manager.getJavaFileObjectsFromFiles(listOfFiles);

        ClassNameScanner scanner = new ClassNameScanner();
        ClassNameProcessor processor = new ClassNameProcessor(scanner);

        JavaCompiler.CompilationTask task = compiler.getTask(null, manager, diagnostics, null, null, sources);
        task.setProcessors(Arrays.asList(processor));

        /*
         * Compiles. The .class files will be saved in the same directory
         * as the java files.
         */
        try {
            boolean compilationResult = task.call();

            if(compilationResult) {
                ctx.setNewClassNames(scanner.getFullClassNames());
                compilationSuccess(result);
            }
            else {
                compilationFail(result, diagnostics.getDiagnostics());
            }

            manager.close();
        } catch(Exception e) {
            result.genericFailure(this, e);
        }
    }

    private void compilationFail(ResultBuilder result, List<Diagnostic<? extends JavaFileObject>> compilationErrors) {
        result.message("We could not compile the code. See the compilation errors below:");
        for(Diagnostic diagnostic: compilationErrors) {
            if (diagnostic.getKind() == ERROR) {

                String message = diagnostic.getMessage(null);
                long lineNumber = diagnostic.getLineNumber();
                result.message(String.format("- line %d: %s",
                        lineNumber,
                        message));

                Optional<String> importLog = ImportUtils.checkMissingImport(message);
                importLog.ifPresent(result::message);

                result.highlight(new Highlight(lineNumber, message, Highlight.HighlightLocation.SOLUTION, Highlight.HighlightPurpose.COMPILATION_ERROR));
            }
        }

        if(anyOfTheErrorsAreCausedDueToBadConfiguration(compilationErrors)) {
            result.message("\n**WARNING:** There might be a problem with this exercise. "+
                    "Please contact the teaching staff so they can fix this as quickly" +
                    "as possible. Thank you for your help! :)");
        }

        result.failed();
    }

    private boolean anyOfTheErrorsAreCausedDueToBadConfiguration(List<Diagnostic<? extends JavaFileObject>> compilationErrors) {
        return compilationErrors
                .stream()
                .anyMatch(c -> c.getKind() == ERROR && c.getSource().getName().endsWith("Configuration.java"));
    }

    private void compilationSuccess(ResultBuilder result) {
        result.message("--- Compilation\nSuccess");
    }

    @SupportedSourceVersion(SourceVersion.RELEASE_11)
    @SupportedAnnotationTypes("*")
    public class ClassNameProcessor extends AbstractProcessor {
        private final ClassNameScanner scanner;

        public ClassNameProcessor(ClassNameScanner scanner) {
            this.scanner = scanner;
        }

        public boolean process(final Set< ? extends TypeElement> types,
                               final RoundEnvironment environment) {

            if(!environment.processingOver()) {
                for(final Element element: environment.getRootElements()) {
                    scanner.scan(element);
                }
            }

            return true;
        }
    }

    public class ClassNameScanner extends ElementScanner9< Void, Void > {
        private List<String> fullClassNames = new ArrayList<>();

        public Void visitType(final TypeElement type, final Void p) {
            fullClassNames.add(type.getQualifiedName().toString());
            return super.visitType(type, p);
        }

        public List<String> getFullClassNames() {
            return Collections.unmodifiableList(fullClassNames);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof CompilationStep;
    }

}