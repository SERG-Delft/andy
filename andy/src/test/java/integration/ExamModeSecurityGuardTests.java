package integration;

import nl.tudelft.cse1110.andy.execution.ExecutionFlow;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.execution.step.SourceCodeSecurityCheckStep;
import nl.tudelft.cse1110.andy.result.Result;
import nl.tudelft.cse1110.andy.utils.FilesUtils;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;


public class ExamModeSecurityGuardTests extends IntegrationTestBase {

    @Test
    void nothingChangesWhenExamModeButRunningTests() {
        Result result = run(Action.TESTS, "NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass", "NumberUtilsAddSecurityExamConfiguration");

        // everything still works in the solution
        assertThat(result.getCompilation().successful()).isTrue();
        assertThat(result.getGenericFailure().hasFailure()).isFalse();
        assertThat(result.getTests().getTestsSucceeded()).isEqualTo(result.getTests().getTestsRan());
    }

    @Test
    void nothingChangesInSeleniumTest() {
        assertTimeoutPreemptively(ofSeconds(5), () -> {
            Result result = run(Action.TESTS, "EmptyLibrary", "SeleniumOnePassingOneFailingSolution",
                    "SeleniumSimpleWebpageExamConfiguration");

            AssertionsForClassTypes.assertThat(result.hasGenericFailure()).isFalse();
            AssertionsForClassTypes.assertThat(result.getTests().getTestsRan()).isEqualTo(2);
            AssertionsForClassTypes.assertThat(result.getTests().getTestsSucceeded()).isEqualTo(1);
        });
    }

    @Test
    void nothingChangesInMutationTestWithoverriddenNumberOfTotalMutantsAllKilled() {
        Result result = run("ZagZigLibrary", "ZagZigAllMutantsKilled", "ZagZigDifferentTotalMutantsExamConfiguration");

        AssertionsForClassTypes.assertThat(result.getMutationTesting().getKilledMutants()).isEqualTo(25);
        AssertionsForClassTypes.assertThat(result.getMutationTesting().getTotalNumberOfMutants()).isEqualTo(25);
    }

    @Test
    void noHintsIfExam() {
        Result result = run(Action.FULL_WITH_HINTS, "NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass", "NumberUtilsAddSecurityExamConfiguration");

        assertThat(result.getFinalGrade()).isEqualTo(0);
        assertThat(result.getMetaTests().wasExecuted()).isFalse();
        assertThat(result.getCodeChecks().wasExecuted()).isFalse();
    }

    @Test
    void filesGetDeleted() {
        run(Action.TESTS, "NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass", "NumberUtilsAddSecurityExamConfiguration");

        Collection<String> javaFiles = FilesUtils.getAllFiles(workDir.getAbsolutePath(), "java").stream().map(f -> f.getName()).toList();
        Collection<String> bytecodeFiles = FilesUtils.getAllFiles(workDir.getAbsolutePath(), "class").stream().map(f -> f.getName()).toList();

        assertThat(javaFiles)
                .contains("Solution.java", "Library.java")
                .noneMatch(t -> t.contains("Configuration"));

        assertThat(bytecodeFiles)
                .isNotEmpty()
                .noneMatch(t -> t.contains("Configuration"));
    }

    @Test
    void noWayToReadTheFile() {

        /**
         * An attacker could look for all the directories in the machine,
         * but I can't simulate that in a test as our repository has many configuration files.
         *
         * We know that Andy runs on a docker image containing only a single
         * Configuration.java file, so in the test, I just inject the work directory to the test,
         * and see if the file is visible from the test execution.
         *
         * The test should fail if it finds the file. (It does fail when the security guard is off)
         */
        System.setProperty("workDir", workDir.getAbsolutePath());

        Result result = run(Action.TESTS, "NumberUtilsAddLibrary", "NumberUtilsSecurityHack", "NumberUtilsAddSecurityExamConfiguration");

        assertThat(result.getTests().getTestsRan()).isEqualTo(1);
        assertThat(result.getTests().getTestsSucceeded()).isEqualTo(1);
    }

    @Test
    void reflectionWouldntWork() {

        // Let's disable the source code security check step, just to see if we could force
        // loading the configuration via reflection.
        // this way we know that even if an attacker is able to bypass our basic security check,
        // the class wouldn't be loaded
        List<ExecutionStep> steps = ExecutionFlow.basicSteps()
                .stream()
                .filter(f -> !f.getClass().equals(SourceCodeSecurityCheckStep.class))
                .toList();

        Result result = run(Action.TESTS, "NumberUtilsAddLibrary", "NumberUtilsSecurityHack2", "NumberUtilsAddSecurityExamConfiguration", steps);

        assertThat(result.getTests().getTestsRan()).isEqualTo(1);
        assertThat(result.getTests().getTestsSucceeded()).isEqualTo(1);
    }

}
