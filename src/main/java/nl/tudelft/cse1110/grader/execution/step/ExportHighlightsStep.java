package nl.tudelft.cse1110.grader.execution.step;

import nl.tudelft.cse1110.grader.config.Configuration;
import nl.tudelft.cse1110.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.grader.result.ResultBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static javax.tools.Diagnostic.Kind.ERROR;

public class ExportHighlightsStep implements ExecutionStep {

    //Compilation step takes precedence to exportHighlightsStep
    public static boolean compilationPassed;
    public static List<Diagnostic<? extends JavaFileObject>> diagnostics;

    private String highlightColour = "red";

    @Override
    public void execute(Configuration cfg, ResultBuilder result) {
        if (compilationPassed == true) {
            //optional: Show a message of "good job!" or "No compilation errors encountered"
            //possibly with colors
            //anyways this if branch is discardable
        } else {

            //--Creating the JSON Object-----
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
            //----Creating JSON file-------
            try {
                FileWriter fw = new FileWriter("src/main/java/nl/tudelft/cse1110/grader/result/highlight.json");
                fw.write(obj.toJSONString());
                fw.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            //----Set the compilation result to failed-------
            result.failed();
        }
    }
}
