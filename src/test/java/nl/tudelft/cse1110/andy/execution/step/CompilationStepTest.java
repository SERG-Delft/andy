package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.IntegrationTestBase;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import org.junit.jupiter.api.Test;

import java.io.File;

import static nl.tudelft.cse1110.andy.ExecutionStepHelper.onlyBasic;
import static nl.tudelft.cse1110.andy.ExecutionStepHelper.onlyCompilation;
import static nl.tudelft.cse1110.andy.ResultTestAssertions.*;
import static nl.tudelft.cse1110.andy.utils.FilesUtils.concatenateDirectories;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CompilationStepTest extends IntegrationTestBase {


    @Test
    void compilationFails() {
        String result = run(onlyCompilation(), "ArrayUtilsIsSortedLibrary", "ArrayUtilsIsSortedWithCompilationError");

        assertThat(result)
                .has(finalGrade(workDir.toString(), 0))
                .has(compilationFailure())
                .has(compilationErrorOnLine(29))
                .has(compilationErrorType("not a statement"))
                .has(compilationErrorType("';' expected"))
                .doesNotHave(compilationErrorMoreTimes("cannot find symbol", 2));;
    }

    @Test
    void compilationFailsDuringGradingMeans0() {
        String result = run(Action.FULL_WITH_HINTS, onlyBasic(), "ArrayUtilsIsSortedLibrary", "ArrayUtilsIsSortedWithCompilationError", "ArrayUtilsInGradingMode");

        assertThat(result)
                .has(finalGrade(workDir.toString(), 0))
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
                .has(finalGrade(workDir.toString(), 0))
                .has(compilationFailure())
                .has(compilationErrorOnLine(21))
                .has(compilationErrorOnLine(25))
                .has(compilationErrorOnLine(33))
                .has(compilationErrorMoreTimes("cannot find symbol", 3));
    }

    @Test
    void highlightsGeneratedWhenCompilationFails(){
        String result = run(onlyCompilation(), "ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfImportListCommented");

        File highlights = new File(concatenateDirectories(workDir.toString(), "highlights.json"));
        assertThat(highlights).exists().isFile();

        String expected = "[{\"line\":40,\"message\":\"cannot find symbol\\n  symbol:   class List\\n  location: class delft.ArrayUtilsTests\",\"location\":\"SOLUTION\",\"purpose\":\"COMPILATION_ERROR\"},{\"line\":69,\"message\":\"cannot find symbol\\n  symbol:   class List\\n  location: class delft.ArrayUtilsTests\",\"location\":\"SOLUTION\",\"purpose\":\"COMPILATION_ERROR\"}]";
        assertThat(highlights).hasContent(expected);
        assertThat(result).has(finalGrade(workDir.toString(), 0));
    }

    @Test
    void configurationFileCompilationFails(){
        String result = run(onlyCompilation(), "NumberUtilsAddLibrary","NumberUtilsAddAllTestsPass","NumberUtilsAddTypoConfiguration");

        assertThat(result)
                .has(finalGrade(workDir.toString(), 0))// not the student's problem, giving a zero is much easier now
                .has(compilationFailure())
                .has(failDueToBadConfigurationMessage());
    }
}
