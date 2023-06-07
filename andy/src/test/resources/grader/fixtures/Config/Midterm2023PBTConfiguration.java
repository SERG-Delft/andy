package delft;

import nl.tudelft.cse1110.andy.codechecker.checks.*;
import nl.tudelft.cse1110.andy.codechecker.engine.*;
import nl.tudelft.cse1110.andy.config.MetaTest;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.mode.Mode;

import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Configuration extends RunConfiguration {

    @Override
    public Mode mode() {
        return Mode.GRADING;
    }

    @Override
    public Map<String, Float> weights() {
        return new HashMap<>() {{
            put("coverage", 0.1f);
            put("mutation", 0.15f);
            put("meta", 0.7f);
            put("codechecks", 0.05f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.StringUtils");
    }

    @Override
    public CheckScript checkScript() {
        return new CheckScript(List.of(
                new SingleCheck("Does not filter unnecessarily", true, new MethodCalledInProvideMethod("filter"))
                // new AndCheck(0, "Does not use disallowed methods (flag for manual review)", List.of(
                //     new SingleCheck(true, new MethodCalledInAnyMethod("startsWith")),
                //     new SingleCheck(true, new MethodCalledInAnyMethod("substring")),
                //     new SingleCheck(true, new MethodCalledInAnyMethod("charAt")),
                //     new SingleCheck(true, new MethodCalledInAnyMethod("toCharArray")),
                //     new SingleCheck(true, new MethodCalledInAnyMethod("indexOf")),
                //     new SingleCheck(true, new MethodCalledInAnyMethod("charAt")),
                //     new SingleCheck(true, new MethodCalledInAnyMethod("matches")),
                //     new SingleCheck(true, new MethodCalledInAnyMethod("regionMatches")),
                //     new SingleCheck(true, new MethodCalledInAnyMethod("subSequence")),
                //     new SingleCheck(true, new MethodCalledInAnyMethod("next")), // iterators
                //     new SingleCheck(true, new MethodCalledInAnyMethod("findFirst")) // streams
                // )),
                // new AndCheck(0, "Does not use example-based tests (flag for manual review)", List.of(
                //     new SingleCheck(true, new AnnotatedMethod("Test")),
                //     new SingleCheck(true, new AnnotatedMethod("ParameterizedTest")),
                //     new SingleCheck(true, new AnnotatedMethod("Example")),
                //     new SingleCheck(true, new MethodCalledInAnyMethod("of"))
                // ))
        ));
    }


    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.withStringReplacement("Only checks the first element",
                        "strings.size()",
                        "1"),
                MetaTest.withStringReplacement("Only checks the first element starting with e",
                        "result.add(i);",
                        "result.add(i); break;"),
                MetaTest.withStringReplacement("Checks that the first letter is \"a\"",
                        ".startsWith(\"e\")",
                        ".startsWith(\"a\")"),
                MetaTest.withStringReplacement("Uses .contains() instead of .startsWith()",
                        "strings.get(i).startsWith(\"e\")",
                        "strings.get(i).contains(\"e\")"),
                MetaTest.withStringReplacement("Indices are off by one",
                        "result.add(i);",
                        "if (i + 1 < strings.size()) result.add(i+1); else result.add(0, 0);"),
                MetaTest.withStringReplacement("All indices are returned",
                        "return result;",
                        "return result.isEmpty() ? result : IntStream.range(0, strings.size()).boxed().collect(Collectors.toList());"),
                MetaTest.withStringReplacement("Ignores the first word",
                        "int i = 0",
                        "int i = 1"),
                MetaTest.withStringReplacement("Ignores the last word",
                        "strings.size()",
                        "strings.size()-1"),
                MetaTest.withStringReplacement("Does not work with an empty list",
                        "strings.size()",
                        "Math.max(strings.size(), 1)")

        );
    }

    // This class checks if certain methods have been used in methods
    static class MethodCalledInAnyMethod extends MethodCalledInTestMethod {

        public MethodCalledInAnyMethod(String methodToBeCalled) {
            super(methodToBeCalled);
        }

        @Override
        protected Set<String> annotations() {
            return Set.of("Provide",
                    "Property",
                    "Test",
                    "ParameterizedTest",
                    "Example",
                    "BeforeEach",
                    "BeforeAll",
                    "AfterEach",
                    "AfterAll");
        }
    }

    // This class checks if certain methods have been used in the "@Provide" methods
    static class MethodCalledInProvideMethod extends MethodCalledInTestMethod {

        public MethodCalledInProvideMethod(String methodToBeCalled) {
            super(methodToBeCalled);
        }

        @Override
        protected Set<String> annotations() {
            return Set.of("Provide");
        }
    }

    static class AnnotatedMethod extends Check {

        private boolean annotationIdentified = false;
        private String annotation;

        public AnnotatedMethod(String annotation){
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

}