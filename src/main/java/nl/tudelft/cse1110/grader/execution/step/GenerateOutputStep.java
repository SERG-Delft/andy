package nl.tudelft.cse1110.grader.execution.step;

import nl.tudelft.cse1110.grader.config.Configuration;
import nl.tudelft.cse1110.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.grader.result.ResultBuilder;
import nl.tudelft.cse1110.grader.util.FileUtils;

import java.io.FileWriter;
import java.io.IOException;

public class GenerateOutputStep implements ExecutionStep {

    @Override
    public void execute(Configuration cfg, ResultBuilder result){

        writeOutputFile(result);
        writeXLMFile(result);
    }

    private void writeOutputFile(ResultBuilder result) {
        try{
            FileWriter fw = new FileWriter(FileUtils.fileWithParentDirCreated(
                    "src/main/java/nl/tudelft/cse1110/grader/output", "stdout.txt"));

            fw.write(result.buildEndUserResult());
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeXLMFile(ResultBuilder result) {
        try{
            FileWriter fw = new FileWriter(FileUtils.fileWithParentDirCreated(
                    "src/main/java/nl/tudelft/cse1110/grader/output", "results.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
