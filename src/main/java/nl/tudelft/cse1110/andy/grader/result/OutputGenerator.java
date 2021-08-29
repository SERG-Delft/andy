package nl.tudelft.cse1110.andy.grader.result;

import com.google.gson.Gson;
import nl.tudelft.cse1110.andy.grader.execution.Context;

import java.io.File;

import static nl.tudelft.cse1110.andy.grader.util.FilesUtils.concatenateDirectories;
import static nl.tudelft.cse1110.andy.grader.util.FilesUtils.writeToFile;

public class OutputGenerator {

    private final Context ctx;

    public OutputGenerator(Context ctx) {

        this.ctx = ctx;
    }

    public void exportOutputFile(ResultBuilder result) {
        File stdoutTxt = new File(concatenateDirectories(ctx.getDirectoryConfiguration().getOutputDir(), "stdout.txt"));
        writeToFile(stdoutTxt, isDebug() ? result.buildDebugResult() : result.buildEndUserResult());
    }

    private boolean isDebug() {
        /* run configuration might be null in case we stop the pipeline sooner, during to a compilation error */
        return ctx.getRunConfiguration()!=null && ctx.getRunConfiguration().debug();
    }

    public void exportXMLFile(ResultBuilder result) {
        String xml = buildResultsXml(result);

        File resultsXml = new File(concatenateDirectories(ctx.getDirectoryConfiguration().getOutputDir(), "results.xml"));
        writeToFile(resultsXml, xml);
    }

    private String buildResultsXml(ResultBuilder result) {
        StringBuilder xml = new StringBuilder();

        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<testsuites>\n\t<testsuite>\n");
        int score = result.finalGrade();
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

    public void exportHighlights(ResultBuilder result){

        String json = new Gson().toJson(result.getHighlights());

        File highlightsJson = new File(concatenateDirectories(ctx.getDirectoryConfiguration().getOutputDir(), "highlights.json"));
        writeToFile(highlightsJson, json);
    }
}
