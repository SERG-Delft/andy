package nl.tudelft.cse1110.grader.execution.step;

import nl.tudelft.cse1110.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import java.io.File;

import static nl.tudelft.cse1110.ExecutionStepHelper.onlyCompilation;
import static nl.tudelft.cse1110.grader.util.FileUtils.concatenateDirectories;
import static nl.tudelft.cse1110.ResultTestAssertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CompilationStepTest extends IntegrationTestBase {

    @Test
    void compilationFails() {
        String result = run(onlyCompilation(), "ArrayUtilsIsSortedLibrary", "ArrayUtilsIsSortedWithCompilationError");
        assertThat(result)
                .has(compilationFailure())
                .has(compilationErrorOnLine(29))
                .has(compilationErrorType("not a statement"))
                .has(compilationErrorType("';' expected"))
                .doesNotHave(compilationErrorMoreTimes("cannot find symbol", 2));;
    }


    @Test
    void compilationOk() {
        String result = run(onlyCompilation(),  "ListUtilsLibrary", "ListUtilsCompilationSuccess");
        assertThat(result)
                .has(compilationSuccess());
    }


    @Test
    void compilationDifferentFailures() {
        String result = run(onlyCompilation(), "MathArraysLibrary","MathArraysDifferentCompilationErrors");
        assertThat(result)
                .has(compilationFailure())
                .has(compilationErrorOnLine(21))
                .has(compilationErrorOnLine(25))
                .has(compilationErrorOnLine(33))
                .has(compilationErrorMoreTimes("cannot find symbol", 3));
    }

    @Test
    void highlightsGeneratedWhenCompilationFails(){
        run(onlyCompilation(), "ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfImportListCommented");

        File highlights = new File(concatenateDirectories(workDir.toString(), "highlights.json"));
        assertThat(highlights).exists().isFile();

        String expected = "{\"Error List\":[{\"Line\":40,\"Message\":\"cannot find symbol\\n  symbol:   class List\\n  location: class delft.ArrayUtilsTests\",\"Color\":\"red\"},{\"Line\":69,\"Message\":\"cannot find symbol\\n  symbol:   class List\\n  location: class delft.ArrayUtilsTests\",\"Color\":\"red\"}]}";
        assertThat(highlights).hasContent(expected);
    }

    @Test
    void configurationFileCompilationFails(){
        String result = run(onlyCompilation(), "NumberUtilsAddLibrary","NumberUtilsAddAllTestsPass","NumberUtilsAddTypoConfiguration");

        assertThat(result)
                .has(configurationFailMessage());
    }
}
