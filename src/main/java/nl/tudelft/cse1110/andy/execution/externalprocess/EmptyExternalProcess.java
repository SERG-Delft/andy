package nl.tudelft.cse1110.andy.execution.externalprocess;

public class EmptyExternalProcess implements ExternalProcess {
    @Override
    public void launch() {

    }

    @Override
    public void awaitInitialization() {

    }

    @Override
    public void kill() {

    }

    @Override
    public String getErr() {
        return null;
    }
}
