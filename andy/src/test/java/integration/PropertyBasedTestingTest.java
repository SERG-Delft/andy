package integration;

import nl.tudelft.cse1110.andy.result.Result;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PropertyBasedTestingTest extends IntegrationTestBase {

    @Test
    void slow_student_solution_in_2023_midterm() {
        Result result = run("Midterm2023PBTLibrary", "Midterm2023PBTTest", "Midterm2023PBTConfiguration");

        assertThat(result.hasFailed()).isFalse();
        assertThat(result.isFullyCorrect()).isFalse();
        assertThat(result.getFinalGrade()).isEqualTo(84);

        // this one is running in around 31-35 seconds locally, and 71-75 in CI
        // let's use this test as a way to ensure we have no regressions
        // let's keep it at least under 80 seconds
        assertThat(result.getTimeInSeconds()).isLessThan(80);
    }

}
