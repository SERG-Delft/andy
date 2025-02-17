package nl.tudelft.cse1110.andy.codechecker.checks;

import org.eclipse.jdt.core.dom.*;

import java.util.Set;

public abstract class WithinAnnotatedMethod extends Check {

    private boolean annotationFound = false;
    protected boolean inAnonymousClass = false;

    protected abstract Set<String> annotations();

    @Override
    public boolean visit(MarkerAnnotation node) {
        checkIfThisIsATestAnnotation(node.getTypeName());
        return false;
    }

    @Override
    public boolean visit(NormalAnnotation node) {
        checkIfThisIsATestAnnotation(node.getTypeName());
        return false;
    }

    @Override
    public boolean visit(SingleMemberAnnotation node) {
        checkIfThisIsATestAnnotation(node.getTypeName());
        return false;
    }


    private void checkIfThisIsATestAnnotation(Name name) {
        if (inAnonymousClass) {
            // Ignore all annotations within anonymous classes
            return;
        }

        if (annotations().contains(name.getFullyQualifiedName()))
            annotationFound = true;
    }

    protected boolean isInTheAnnotatedMethod() {
        return annotationFound;
    }

    @Override
    public void endVisit(MethodDeclaration md) {
        /*
         * whenever we finish visiting a method, we remove the flag,
         * unless we're in an anonymous class (because then we haven't reached the end of the annotated method)
         */
        if (!inAnonymousClass) {
            annotationFound = false;
        }
    }

    @Override
    public boolean visit(AnonymousClassDeclaration classDeclaration) {
        inAnonymousClass = true;
        return super.visit(classDeclaration);
    }

    @Override
    public void endVisit(AnonymousClassDeclaration classDeclaration) {
        inAnonymousClass = false;
    }
}
