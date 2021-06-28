package nl.tudelft.cse1110.grader.codechecker.checks;

import java.util.List;

/**
 * Counts the number of JQWik's @Property.
 *
 * Parameters:
 * - operator (LT, LTE, GT, GTE, EQ)
 * - the minimum number of expected properties
 *
 * Output: true if number of @Property in the file is >= passed value; false otherwise.
 */
public class JQWikProperty extends UsageOfAnAnnotationTemplate {

    public JQWikProperty(List<String> params) {
        super(params);
    }

    @Override
    protected String annotationName() {
        return "Property";
    }


}
