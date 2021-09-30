package nl.tudelft.cse1110.andy.execution.externalprocess;

import java.io.IOException;

public interface ExternalProcess {
    void launch() throws IOException;

    void awaitInitialization();

    void kill();

    String getErrorMessages();

    boolean hasExitedNormally();

    int getExitCode();
}
