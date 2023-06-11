package nl.tudelft.cse1110.andy.codechecker.checks;

import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;

public class AnnotatedMethod extends Check {

    private boolean annotationIdentified = false;
    private String annotation;

    public AnnotatedMethod(String annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean visit(MarkerAnnotation node) {
        checkIfThisIsTheAnnotation(node.getTypeName());
        return true;
    }

    @Override
    public boolean visit(NormalAnnotation node) {
        checkIfThisIsTheAnnotation(node.getTypeName());
        return true;
    }

    @Override
    public boolean visit(SingleMemberAnnotation node) {
        checkIfThisIsTheAnnotation(node.getTypeName());
        return true;
    }

    private void checkIfThisIsTheAnnotation(Name name) {
        if (name.getFullyQualifiedName().equals(annotation))
            annotationIdentified = true;
    }

    @Override
    public boolean result() {
        return annotationIdentified;
    }
}
