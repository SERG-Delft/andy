package nl.tudelft.cse1110.grader.execution;

import nl.tudelft.cse1110.grader.execution.step.CompilationStep;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.util.List;

public class ResultBuilder {
    public void compilationFail(List<Diagnostic<? extends JavaFileObject>> diagnostics) {
//        for( final Diagnostic< ? extends JavaFileObject > diagnostic:
//                diagnostics.getDiagnostics() ) {
//
//            if(diagnostic.getKind()== Diagnostic.Kind.WARNING) continue;
//            System.out.format("%s, line %d in %s",
//                    diagnostic.getMessage( null ),
//                    diagnostic.getLineNumber(),
//                    diagnostic.getSource()!=null?diagnostic.getSource().getName():"" );
//        }
    }

    public void logSuccess(ExecutionStep step) {

    }

    public void genericFailure(Exception e) {
    }
}
