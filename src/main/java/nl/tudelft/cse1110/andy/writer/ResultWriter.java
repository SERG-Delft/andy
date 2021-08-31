package nl.tudelft.cse1110.andy.writer;

import nl.tudelft.cse1110.andy.result.Result;

public interface ResultWriter {

    public void write(Result result);

    public void uncaughtError(Throwable t);
}
