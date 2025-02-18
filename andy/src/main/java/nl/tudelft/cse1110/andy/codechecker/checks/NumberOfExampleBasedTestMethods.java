package nl.tudelft.cse1110.andy.codechecker.checks;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

public class NumberOfExampleBasedTestMethods extends UsageOfAnAnnotationTemplate {

    public NumberOfExampleBasedTestMethods(Comparison comparison, int minimumNumberOfUsage) {
        super(comparison, minimumNumberOfUsage);
    }

    @Override
    protected Set<String> annotationNames() {
        return ImmutableSet.of("Test", "ParameterizedTest", "Example");
    }

    public String toString() {
        return "Number of tests " + comparison + " " + minimumNumberOfUsage;
    }
}
