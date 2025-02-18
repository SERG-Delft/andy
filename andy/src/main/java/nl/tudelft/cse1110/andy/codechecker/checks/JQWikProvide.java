package nl.tudelft.cse1110.andy.codechecker.checks;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

/**
 * Counts the number of JQWik's @Provide.
 *
 * Parameters:
 * - operator (LT, LTE, GT, GTE, EQ)
 * - the minimum number of expected provides
 *
 * Output: true if number of @Provide in the file is >= passed value; false otherwise.
 */
public class JQWikProvide extends UsageOfAnAnnotationTemplate {

    public JQWikProvide(Comparison comparison, int minimumNumberOfUsage) {
        super(comparison, minimumNumberOfUsage);
    }

    @Override
    protected Set<String> annotationNames() {
        return ImmutableSet.of("Provide");
    }

    public String toString() {
        return "Number of JQWik provider methods is " + comparison.toString() + " " + minimumNumberOfUsage;
    }


}
