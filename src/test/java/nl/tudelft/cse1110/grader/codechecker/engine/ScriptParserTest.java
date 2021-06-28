package nl.tudelft.cse1110.grader.codechecker.engine;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;


public class ScriptParserTest {

    @Test
    void parseYaml() {
        String exampleYaml =
                "checks:\n" +
                "  -  rule: MockClass\n" +
                "     description: d1\n" +
                "     params: UserRepository\n" +
                "     flip: true\n" +
                "     weight: 1\n" +
                "  -  type: single\n" +
                "     rule: MockitoWhen\n" +
                "     description: d2\n" +
                "     params: pointsForFeaturedPost EQ 1\n" +
                "     flip: false\n" +
                "     weight: 2\n" +
                "  - type: or\n" +
                "    weight: 7\n" +
                "    description: or d\n" +
                "    checks:\n" +
                "      -  type: single\n" +
                "         rule: MockClass\n" +
                "         description: d3\n" +
                "         params: UserRepository2\n" +
                "         flip: true\n" +
                "      -  type: single\n" +
                "         rule: MockitoWhen\n" +
                "         description: d4\n" +
                "         params: pointsForFeaturedPost EQ 12\n" +
                "         flip: false\n";

        CheckScript cfg = new ScriptParser().parse(exampleYaml);

        assertThat(cfg.getChecks()).containsExactly(
                new SingleCheck(1, "MockClass", "d1", true, "UserRepository"),
                new SingleCheck(2, "MockitoWhen", "d2", false, "pointsForFeaturedPost EQ 1"),
                new OrCheck(7, "or d",
                        Arrays.asList(
                                new SingleCheck(1, "MockClass", "d3", true, "UserRepository2"),
                                new SingleCheck(1, "MockitoWhen", "d4", false, "pointsForFeaturedPost EQ 12")
                        )
                )
        );
    }

    @Test
    void nestedOrsAndAnds_m2021isTriangle() {
        CheckScript cfg = new TestUtils().getYamlConfig("integration/parser/m2021-isTriangle.yml");

        assertThat(cfg.getChecks()).containsExactly(
                new OrCheck(1, "either use [properties, providers, and arbitrary<ABC>] or [>3 properties and IntRange]",
                        Arrays.asList(
                                new AndCheck(1, null, Arrays.asList(
                                        new SingleCheck("JQWikProperty", "GTE 1"),
                                        new SingleCheck("JQWikProvide", "GTE 1"),
                                        new SingleCheck("JQWikArbitrary", "ABC")
                                )),
                                new AndCheck(1, null, Arrays.asList(
                                        new SingleCheck("JQWikProperty", "GTE 3"),
                                        new SingleCheck("JQWikProvide", "EQ 0"),
                                        new SingleCheck("JQWikProvideAnnotations")
                                ))
                        ))
        );
    }
}
