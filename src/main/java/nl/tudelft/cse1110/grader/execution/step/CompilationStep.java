package nl.tudelft.cse1110.grader.execution.step;

import nl.tudelft.cse1110.grader.config.Configuration;
import nl.tudelft.cse1110.grader.config.DirectoryConfiguration;
import nl.tudelft.cse1110.grader.result.ResultBuilder;
import nl.tudelft.cse1110.grader.util.ClassUtils;
import nl.tudelft.cse1110.grader.util.FileUtils;
import nl.tudelft.cse1110.grader.execution.ExecutionStep;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static javax.tools.Diagnostic.Kind.ERROR;

/**
 * This step compiles the student code and the library code.
 * It makes use of the Java Compiler API.
 */
public class CompilationStep implements ExecutionStep {

    public static String highlightColour = "red";

    @Override
    public void execute(Configuration cfg, ResultBuilder result) {
        DirectoryConfiguration dirCfg = cfg.getDirectoryConfiguration();

        /**
         * creates the java compiler and diagnostic collector object
         * using just the standard configuration. Nothing to optimize here, I believe.
         */
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager manager = compiler.getStandardFileManager(
                diagnostics, null, null );

        /**
         * Create a compilation task with the list of files to compile.
         * Also pass the classpath with the libraries, e.g., JUnit, JQWik, etc.
         */
        Collection<File> listOfFiles = FileUtils.getAllJavaFiles(dirCfg.getWorkingDir());
        Iterable<? extends JavaFileObject > sources =
                manager.getJavaFileObjectsFromFiles(listOfFiles);

        ClassNameScanner scanner = new ClassNameScanner();
        ClassNameProcessor processor = new ClassNameProcessor(scanner);

        JavaCompiler.CompilationTask task = compiler.getTask(null, manager, diagnostics,
                Arrays.asList(
                        /* classpath */
                        "-cp", ClassUtils.asClassPath(dirCfg.getLibrariesDir()) +
                                ClassUtils.classSeparator() +
                                System.getProperty("java.class.path")), null, sources);
        task.setProcessors(Arrays.asList(processor));

        /**
         * Compiles. The .class files will be saved in the same directory
         * as the java files.
         */
        try {
            boolean compilationResult = task.call();

            if(compilationResult) {
                dirCfg.setNewClassNames(scanner.getFullClassNames());
                result.compilationSuccess();
            }
            else {
                result.compilationFail(diagnostics.getDiagnostics());
                exportCompilationErrors(diagnostics.getDiagnostics());
            }

            manager.close();
        } catch(Exception e) {
            result.genericFailure(this, e);
        }
    }

    private void exportCompilationErrors(List<Diagnostic<? extends JavaFileObject>> diagnostics){
        //--Creating the JSON Object-----
        JSONObject obj = new JSONObject();
        JSONArray errors = new JSONArray();
        for (Diagnostic diagnostic : diagnostics) {
            if (diagnostic.getKind() == ERROR) {
                JSONObject temp = new JSONObject();
                temp.put("Line", diagnostic.getLineNumber());
                temp.put("Color", highlightColour);
                temp.put("Message", diagnostic.getMessage(null));
                errors.add(temp);
            }
        }
        obj.put("Error List", errors);

        //----Creating JSON file-------
        try {
            FileWriter fw = new FileWriter("src/main/java/nl/tudelft/cse1110/grader/result/highlight.json");
            fw.write(obj.toJSONString());
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
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

}
