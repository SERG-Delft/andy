package nl.tudelft.cse1110.andy.execution.mode;

// CUSTOM mode is to be used during tests and meta tests.
// This action means that the steps for the execution flow have already been set.
public enum Action {
    FULL_WITH_HINTS, FULL_WITHOUT_HINTS, COVERAGE, TESTS, CUSTOM
}
