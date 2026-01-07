package integration.quality;

import integration.BaseMetaTestsTest;
import nl.tudelft.cse1110.andy.result.Result;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LimitedNumberOfMetaTestsTest extends BaseMetaTestsTest {

    /**
     * For an overview of the results. To be used in production only.
     */
    @ParameterizedTest
    @MethodSource("testSuitesDST")
    void metaTestQualityIsAcceptable(
            String libraryFile,
            String solutionFile,
            String configurationFile
    ) {
        Result result = run(libraryFile, solutionFile, configurationFile);

        System.out.println("File: " +  libraryFile +
                "\nQuality score: " + result.getQualityResult().getScore() + "\n\n");
    }

    static Stream<Arguments> testSuitesDST() {
        return Stream.of(
                Arguments.of("NumberUtilsAddLibrary", "NumberUtilsAddOfficialSolution", "NumberUtilsAddConfiguration"),
                Arguments.of("NumberUtilsAddLibrary", "NumberUtilsAddOfficialSolution", "NumberUtilsAddConfiguration")
        );
    }
}
