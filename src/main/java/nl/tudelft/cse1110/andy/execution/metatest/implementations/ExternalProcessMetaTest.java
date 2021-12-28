package nl.tudelft.cse1110.andy.execution.metatest.implementations;

import nl.tudelft.cse1110.andy.execution.externalprocess.ExternalProcess;
import nl.tudelft.cse1110.andy.execution.metatest.AbstractMetaTest;

import java.io.IOException;

public class ExternalProcessMetaTest extends AbstractMetaTest {
    private final ExternalProcess externalProcess;

    public ExternalProcessMetaTest(int weight, String name, ExternalProcess externalProcess) {
        super(weight, name);
        this.externalProcess = externalProcess;
    }

    public void startExternalProcess() throws IOException {
        this.externalProcess.launch();
        this.externalProcess.awaitInitialization();
    }

    public void killExternalProcess() {
        this.externalProcess.kill();
    }
}
