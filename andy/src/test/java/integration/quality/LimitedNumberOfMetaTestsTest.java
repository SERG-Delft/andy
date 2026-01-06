package integration.quality;

import integration.BaseMetaTestsTest;
import nl.tudelft.cse1110.andy.result.Result;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LimitedNumberOfMetaTestsTest extends BaseMetaTestsTest {

    @Test
    void allMetaTestsPassing() {
        Result result = run("NumberUtilsAddLibrary", "NumberUtilsAddOfficialSolution", "NumberUtilsAddConfiguration");

        assertThat(result.getQualityResult().getScore()).isEqualTo(1);
        assertThat(result.getQualityResult().getMetaTestReports().size()).isEqualTo(4);
    }
}
