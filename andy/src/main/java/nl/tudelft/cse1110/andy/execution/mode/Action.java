package nl.tudelft.cse1110.andy.execution.mode;

public enum Action {
    FULL_WITH_HINTS, /* Runs all analysis, and provides hints to students */
    FULL_WITHOUT_HINTS, /* Runs the full analysis, gives the grade, but no hints */
    COVERAGE, /* Runs mutation testing besides coverage */
    TESTS, /* Runs the tests with coverage */
    META_TEST /* Internal use only */
}
