package nl.tudelft.cse1110.andy.writer.weblab;

import nl.tudelft.cse1110.andy.result.Result;

public class Submission {

    private final SubmissionMetaData metaData;
    private final Result result;

    public Submission(SubmissionMetaData metaData, Result result) {
        this.metaData = metaData;
        this.result = result;
    }

}
