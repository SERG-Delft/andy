package delft;

import net.jqwik.api.*;

import java.util.*;
import java.util.stream.*;

import static org.assertj.core.api.Assertions.*;

class SummerPBTest {

    @Property
    public void testSummer(@ForAll("summerTemperatures") List<Double> temperatures) {
        boolean result = Summer.isItSummer(temperatures);
        assertThat(result).isTrue();
    }

    @Property
    public void testNotSummer(@ForAll("nonSummerTemperatures") List<Double> temperatures) {
        boolean result = Summer.isItSummer(temperatures);
        assertThat(result).isFalse();
    }

    @Provide
    private Arbitrary<List<Double>> summerTemperatures() {
        // At least 75 temperature values >= 20
        var greaterOrEqualTo20 = Arbitraries.doubles()
                .greaterOrEqual(20)
                .list()
                .ofMinSize(75);

        // At most 25 temperature values < 20
        var below20 = Arbitraries.doubles()
                .lessThan(20)
                .list()
                .ofMaxSize(25);

        // Combine them together and shuffle them
        return Combinators.combine(greaterOrEqualTo20, below20)
                .as((l1, l2) -> {
                    var result = Stream.concat(l1.stream(), l2.stream()).collect(Collectors.toList());
                    Collections.shuffle(result);
                    return result;
                });
    }

    @Provide
    private Arbitrary<List<Double>> nonSummerTemperatures() {
        // Less than 75 temperature values >= 20
        var greaterOrEqualTo20 = Arbitraries.doubles()
                .greaterOrEqual(20)
                .list()
                .ofMaxSize(74);

        // At least 25 temperature values < 20
        var below20 = Arbitraries.doubles()
                .lessThan(20)
                .list()
                .ofMinSize(25);

        // Combine them together and shuffle them
        return Combinators.combine(greaterOrEqualTo20, below20)
                .as((l1, l2) -> {
                    var result = Stream.concat(l1.stream(), l2.stream()).collect(Collectors.toList());
                    Collections.shuffle(result);
                    return result;
                });
    }
}
