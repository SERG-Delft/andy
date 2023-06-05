package nl.tudelft.cse1110.andy.aws;

import nl.tudelft.cse1110.andy.result.Result;

public class AWSResult {

    private final String textOutput;
    private final Result result;

    public AWSResult(String textOutput, Result result) {
        this.textOutput = textOutput;
        this.result = result;
    }

    public String getTextOutput() {
        return textOutput;
    }

    public Result getResult() {
        return result;
    }
}
