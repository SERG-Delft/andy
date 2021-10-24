package nl.tudelft.cse1110.andy.result;

public class GenericFailure {
    private final String genericFailureMessage;
    private final String stepName;
    private final String exceptionMessage;
    private final Integer externalProcessExitCode;
    private final String externalProcessErrorMessages;

    private GenericFailure(String genericFailureMessage, String stepName, String exceptionMessage, Integer externalProcessExitCode, String externalProcessErrorMessages) {
        this.genericFailureMessage = genericFailureMessage;
        this.stepName = stepName;
        this.exceptionMessage = exceptionMessage;
        this.externalProcessExitCode = externalProcessExitCode;
        this.externalProcessErrorMessages = externalProcessErrorMessages;
    }

    public static GenericFailure build(String genericFailure, String stepName, String exceptionMessage, Integer externalProcessExitCode, String externalProcessErrorMessages) {
        return new GenericFailure(genericFailure, stepName, exceptionMessage, externalProcessExitCode, externalProcessErrorMessages);
    }

    public String getGenericFailureMessage() {
        return genericFailureMessage;
    }

    public String getStepName() {
        return stepName;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public Integer getExternalProcessExitCode() {
        return externalProcessExitCode;
    }

    public String getExternalProcessErrorMessages() {
        return externalProcessErrorMessages;
    }

    @Override
    public String toString() {
        return "GenericFailure{" +
               "genericFailureMessage='" + genericFailureMessage + '\'' +
               ", stepName='" + stepName + '\'' +
               ", exceptionMessage='" + exceptionMessage + '\'' +
               ", externalProcessExitCode=" + externalProcessExitCode +
               ", externalProcessErrorMessages='" + externalProcessErrorMessages + '\'' +
               '}';
    }
}
