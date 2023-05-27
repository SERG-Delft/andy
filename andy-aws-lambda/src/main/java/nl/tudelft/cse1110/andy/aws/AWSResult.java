package nl.tudelft.cse1110.andy.aws;

public class AWSResult {

    private final String textOutput;
    private final String weblab;
    private final String highlights;

    public AWSResult(String textOutput, String weblab, String highlights) {
        this.textOutput = textOutput;
        this.weblab = weblab;
        this.highlights = highlights;
    }

    public String getTextOutput() {
        return textOutput;
    }

    public String getWeblab() {
        return weblab;
    }

    public String getHighlights() {
        return highlights;
    }
}
