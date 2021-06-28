package nl.tudelft.cse1110.grader.codechecker.checks;

import java.util.List;

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

    public JQWikProvide(List<String> params) {
        super(params);
    }

    @Override
    protected String annotationName() {
        return "Provide";
    }


}
