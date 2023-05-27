package nl.tudelft.cse1110.andy.aws;

class AWSInput {
    private String action;

    private String solution;
    private String library;
    private String configuration;

    public AWSInput() {}

    public AWSInput(String action, String solution, String library, String configuration) {
        this.action = action;
        this.solution = solution;
        this.library = library;
        this.configuration = configuration;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public void setLibrary(String library) {
        this.library = library;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public String getAction() {
        return action;
    }

    public String getSolution() {
        return solution;
    }

    public String getLibrary() {
        return library;
    }

    public String getConfiguration() {
        return configuration;
    }
}
