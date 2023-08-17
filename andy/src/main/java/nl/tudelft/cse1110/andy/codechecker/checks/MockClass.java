package nl.tudelft.cse1110.andy.codechecker.checks;

import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.FieldDeclaration;


/**
 * Checks whether the class was mocked via Mockito.mock(..).
 *
 * Parameters:
 * - The name of the class (e.g., "List")
 *
 * Output:
 * - true if the class was mocked at least once.
 */
public class MockClass extends Check {

    private final String classToBeMocked;
    private boolean classWasMocked = false;

    public MockClass(String classToBeMocked) {
        this.classToBeMocked = classToBeMocked;
    }

    @Override
    public boolean visit(MethodInvocation mi) {

        // we only keep looking at method invocations if we did not find the mock.
        if(!classWasMocked) {
            boolean mockMethodCalled = "mock".equals(mi.getName().toString());
            boolean paramsIsAClass = mi.arguments() != null && mi.arguments().size() == 1 && mi.arguments().get(0).toString().endsWith(".class");

            // we are almost certain this is a call to Mockito.
            if (mockMethodCalled && paramsIsAClass) {
                String className = mi.arguments().get(0).toString().replace(".class", "");
                classWasMocked = className.equals(classToBeMocked);
            }
        }

        return super.visit(mi);
    }

    @Override
    public boolean visit(FieldDeclaration fd) {
        // added this if-check to avoid overriding classWasMocked erroneously.
        if(!classWasMocked) {
            boolean hasMockAnnotation = fd.modifiers().stream()
                    .anyMatch(m -> m instanceof Annotation &&
                            ((Annotation) m).getTypeName().getFullyQualifiedName().equals("Mock"));

            // If the field is annotated with @Mock, check if it's the class we are interested in
            if (hasMockAnnotation) {
                String className = fd.getType().toString();
                classWasMocked = className.contains(classToBeMocked);
            }
        }
        return super.visit(fd);
    }


    @Override
    public boolean result() {
        return classWasMocked;
    }

    public String toString() {
        return classToBeMocked + " is mocked";
    }
}
