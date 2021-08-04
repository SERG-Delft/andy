package nl.tudelft.cse1110.grader.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javax.tools.Diagnostic.Kind.ERROR;

public class CompilationUtils {

    private static HashMap<String, String> importDictionary = new HashMap<>();
    //temporary
    private static String highlightColour = "red";
    //Will remake this as a .txt / .csv file
    static {
        importDictionary.put("List", "import java.util.List;");
        importDictionary.put("Collection", "import java.util.Collection;");
        importDictionary.put("stream", "import java.util.stream.*;");
        importDictionary.put("LocalDate", "import java.time.LocalDate;");
        importDictionary.put("Mockito", "import static org.mockito.Mockito.*;");
        importDictionary.put("ParameterizedTest", "import org.junit.jupiter.params.ParameterizedTest;");
        importDictionary.put("Arguments", "import org.junit.jupiter.params.provider.Arguments;");
        importDictionary.put("MethodSource", "import org.junit.jupiter.params.provider.MethodSource;");
        importDictionary.put("Stream", "import java.util.stream.Stream;");
        importDictionary.put("ArrayList", "import java.util.ArrayList;");
        importDictionary.put("LinkedList", "import java.util.LinkedList;");
        importDictionary.put("assertThat", "import static org.assertj.core.api.Assertions.assertThat;");
        importDictionary.put("assertThatThrownBy", "import static org.assertj.core.api.Assertions.assertThatThrownBy;");
        importDictionary.put("BeforeEach", "import org.junit.jupiter.api.BeforeEach;");
        importDictionary.put("Test", "import org.junit.jupiter.api.Test;");
        importDictionary.put("CsvSource", "import org.junit.jupiter.params.provider.CsvSource;");
        importDictionary.put("ForAll","import net.jqwik.api.ForAll;");
        importDictionary.put("Property","import net.jqwik.api.Property;");
        importDictionary.put("IntRange","import net.jqwik.api.constraints.IntRange;");
        importDictionary.put("Size","import net.jqwik.api.constraints.Size;");
    }

    public static Optional<String> checkMissingImport(String message) {
        if(message.startsWith("cannot find symbol")){

            //We parse the diagnostic message and use regex to find the correct token
            //we search for the first word after first "class" instance

            Pattern p = Pattern.compile("class\\W+(\\w+)");
            Matcher m = p.matcher(message);
            String token = m.find() ? m.group(1) : null;

            if(importDictionary.containsKey(token)) {
                return Optional.of(String.format("Maybe you missed the import for %s?\nTry adding this: %s\n",
                        token,
                        importDictionary.get(token)));
            }
        }
        return Optional.empty();
    }

    public static JSONObject generateHighlights(List<Diagnostic<? extends JavaFileObject>> diagnostics) {
        //errors as JSON root element
        JSONObject obj = new JSONObject();
        JSONArray errors = new JSONArray();
        for(Diagnostic diagnostic: diagnostics) {
            if (diagnostic.getKind() == ERROR) {
                JSONObject temp = new JSONObject();
                temp.put("Line", diagnostic.getLineNumber());
                temp.put("Color", highlightColour);
                temp.put("Message", diagnostic.getMessage(null));
                errors.add(temp);
            }
        }
        obj.put("Error List", errors);
        return obj;
    }
}
