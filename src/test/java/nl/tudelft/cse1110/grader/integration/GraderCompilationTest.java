package nl.tudelft.cse1110.grader.integration;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestAssertions.*;
import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestHelper.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GraderCompilationTest extends GraderIntegrationTestBase {

    @Test
    void compilationFailure() {
        String result = run(justCompilation(), "ArrayUtilsIsSortedLibrary", "ArrayUtilsIsSortedWithCompilationError");
        assertThat(result)
                .has(GraderIntegrationTestAssertions.compilationFailure())
                .has(GraderIntegrationTestAssertions.compilationErrorOnLine(29))
                .has(GraderIntegrationTestAssertions.compilationErrorType("not a statement"))
                .has(GraderIntegrationTestAssertions.compilationErrorType("';' expected"))
                .doesNotHave(compilationErrorMoreTimes("cannot find symbol", 2));;
    }


    @Test
    void compilationSuccess() {
        String result = run(justCompilation(),  "ListUtilsLibrary", "ListUtilsCompilationSuccess");
        assertThat(result)
                .has(GraderIntegrationTestAssertions.compilationSuccess());
    }


    @Test
    void compilationDifferentFailures() {
        String result = run(justCompilation(), "MathArraysLibrary","MathArraysDifferentCompilationErrors");
        assertThat(result)
                .has(GraderIntegrationTestAssertions.compilationFailure())
                .has(GraderIntegrationTestAssertions.compilationErrorOnLine(21))
                .has(GraderIntegrationTestAssertions.compilationErrorOnLine(25))
                .has(GraderIntegrationTestAssertions.compilationErrorOnLine(33))
                .has(GraderIntegrationTestAssertions.compilationErrorMoreTimes("cannot find symbol", 3));
    }

    @Test
    void compilationFailureJSONfileCreatedSuccesfully(){
        String result = run(justCompilation(), "ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfImportListCommented");
        File highlights = new File("src/main/java/nl/tudelft/cse1110/grader/result/highlight.json");
        assertThat(highlights.exists() && highlights.isFile()).isTrue();

        String output = "";
        String expected = "{\"Error List\":[{\"Line\":40,\"Message\":\"cannot find symbol\\n  symbol:   class List\\n  location: class delft.ArrayUtilsTests\",\"Color\":\"red\"},{\"Line\":69,\"Message\":\"cannot find symbol\\n  symbol:   class List\\n  location: class delft.ArrayUtilsTests\",\"Color\":\"red\"}]}";

        try (FileReader reader = new FileReader(highlights))
        {
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(reader);
            JSONObject errorList = (JSONObject) obj;
            output = errorList.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThat(output).isEqualTo(expected);
    }
}
