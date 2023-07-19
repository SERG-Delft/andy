package nl.tudelft.cse1110.andy.codechecker.checks;

import com.google.common.collect.ImmutableSet;

import java.util.HashSet;
import java.util.Set;

public abstract class WithinTestMethod extends WithinAnnotatedMethod {

    public static Set<String> TEST_ANNOTATIONS =
            new HashSet<>(ImmutableSet.of(
                "Test", // junit
                "ParameterizedTest", // junit
                "Property" // jqwik
            ));

    protected Set<String> annotations() {
        return TEST_ANNOTATIONS;
    }

}
