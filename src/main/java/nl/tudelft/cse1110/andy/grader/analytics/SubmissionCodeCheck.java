package nl.tudelft.cse1110.andy.grader.analytics;

public class SubmissionCodeCheck {

    private final String check;
    private final boolean passed;

    public SubmissionCodeCheck(String check, boolean passed) {
        this.check = check;
        this.passed = passed;
    }
}
