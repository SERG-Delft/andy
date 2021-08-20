package nl.tudelft.cse1110.andy.grader.result;

import nl.tudelft.cse1110.andy.grader.config.Configuration;
import nl.tudelft.cse1110.andy.grader.util.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.File;
import java.util.List;

import static javax.tools.Diagnostic.Kind.ERROR;

public class OutputGenerator {

    private static final String highlightColour = "red";

    public static void exportOutputFile(Configuration cfg, ResultBuilder result) {
        File stdoutTxt = new File(FileUtils.concatenateDirectories(cfg.getDirectoryConfiguration().getOutputDir(), "stdout.txt"));
        FileUtils.writeToFile(stdoutTxt, result.buildEndUserResult());
    }

    public static void exportXMLFile(Configuration cfg, ResultBuilder result) {
        String xml = buildResultsXml(result);

        File resultsXml = new File(FileUtils.concatenateDirectories(cfg.getDirectoryConfiguration().getOutputDir(), "results.xml"));
        FileUtils.writeToFile(resultsXml, xml);
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

    public static void exportCompilationHighlights(Configuration cfg, List<Diagnostic<? extends JavaFileObject>> diagnostics){
        JSONObject obj = new JSONObject();
        JSONArray errors = new JSONArray();
        for (Diagnostic diagnostic : diagnostics) {
            if (diagnostic.getKind() == ERROR) {
                JSONObject temp = new JSONObject();
                temp.put("Line", diagnostic.getLineNumber());
                temp.put("Color", highlightColour);
                temp.put("Message", diagnostic.getMessage(null));
                errors.add(temp);
            }
        }
        obj.put("Error List", errors);

        File highlightsJson = new File(FileUtils.concatenateDirectories(cfg.getDirectoryConfiguration().getOutputDir(), "highlights.json"));
        FileUtils.writeToFile(highlightsJson, obj.toJSONString());
    }
}
