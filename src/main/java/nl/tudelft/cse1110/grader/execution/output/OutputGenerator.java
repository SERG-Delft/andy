package nl.tudelft.cse1110.grader.execution.output;

import nl.tudelft.cse1110.grader.config.Configuration;
import nl.tudelft.cse1110.grader.result.ResultBuilder;
import nl.tudelft.cse1110.grader.util.FileUtils;

import java.io.FileWriter;
import java.io.IOException;

public class OutputGenerator {

    public static void writeOutputFile(Configuration cfg, ResultBuilder result) {
        try(FileWriter fw = new FileWriter(FileUtils.fileWithParentDirCreated(
                cfg.getDirectoryConfiguration().getReportsDir(), "stdout.txt"))){
            fw.write(result.buildEndUserResult());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeXLMFile(Configuration cfg, ResultBuilder result) {
        try(FileWriter fw = new FileWriter(FileUtils.fileWithParentDirCreated(
                cfg.getDirectoryConfiguration().getReportsDir(), "results.xml"))){

            fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<testsuites>\n\t<testsuite>\n");

            int score = result.getFinalScore();
            String failed = "\t\t<testcase><failure></failure></testcase>\n";
            String passed = "\t\t<testcase/>\n";

            //score = -1 means compilation error
            if(score >= 0){
                fw.write(failed.repeat(100 - score));
                fw.write(passed.repeat(score));
            } else{
                fw.write(failed.repeat(100));
            }

            fw.write("\t</testsuite>\n</testsuites>\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
