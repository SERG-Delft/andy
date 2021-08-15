package nl.tudelft.cse1110.grader.execution.output;

import nl.tudelft.cse1110.grader.config.Configuration;
import nl.tudelft.cse1110.grader.result.ResultBuilder;

import java.io.File;

import static nl.tudelft.cse1110.grader.util.FileUtils.concatenateDirectories;
import static nl.tudelft.cse1110.grader.util.FileUtils.writeToFile;

public class OutputGenerator {

    public static void writeOutputFile(Configuration cfg, ResultBuilder result) {
        File stdoutTxt = new File(concatenateDirectories(cfg.getDirectoryConfiguration().getReportsDir(), "stdout.txt"));
        writeToFile(stdoutTxt, result.buildEndUserResult());
    }

    public static void writeXLMFile(Configuration cfg, ResultBuilder result) {
        String xml = buildResultsXml(result);

        File resultsXml = new File(concatenateDirectories(cfg.getDirectoryConfiguration().getReportsDir(), "results.xml"));
        writeToFile(resultsXml, xml);
    }

    private static String buildResultsXml(ResultBuilder result) {
        StringBuilder xml = new StringBuilder();

        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<testsuites>\n\t<testsuite>\n");
        int score = result.getFinalScore();
        String failed = "\t\t<testcase><failure></failure></testcase>\n";
        String passed = "\t\t<testcase/>\n";

        //score = -1 means compilation error
        if(score >= 0){
            xml.append(failed.repeat(100 - score));
            xml.append(passed.repeat(score));
        } else{
            xml.append(failed.repeat(100));
        }

        xml.append("\t</testsuite>\n</testsuites>\n");
        return xml.toString();
    }
}
