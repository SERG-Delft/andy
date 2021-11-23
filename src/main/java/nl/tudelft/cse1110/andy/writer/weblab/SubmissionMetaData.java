package nl.tudelft.cse1110.andy.writer.weblab;

import nl.tudelft.cse1110.andy.execution.mode.Action;

public class SubmissionMetaData {
    private final String course;
    private final String studentId;
    private final String exercise;
    private final Action action;

    public SubmissionMetaData(String course, String studentId, String exercise, Action action) {
        this.course = course;
        this.studentId = studentId;
        this.exercise = exercise;
        this.action = action;
    }
}