package delft;

import net.jqwik.api.*;
import net.jqwik.api.constraints.*;

import java.util.*;
import java.util.stream.*;

import static org.assertj.core.api.Assertions.*;

class SpaceSignalsPBTest {

    @Property
    public void testRelevant(
            @ForAll("sequenceWithoutPattern") List<SignalType> signals,
            @ForAll @Size(min = 3) List<@IntRange(min = 0) Integer> locationsToInsertPattern
    ) {
        insertPattern(signals, locationsToInsertPattern);
        assertThat(SpaceSignals.numberOfOccurrences(signals)).isEqualTo(locationsToInsertPattern.size());
    }

    @Property
    public void testIrrelevant(
            @ForAll("sequenceWithoutPattern") List<SignalType> signals,
            @ForAll @Size(max = 2) List<@IntRange(min = 0) Integer> locationsToInsertPattern
    ) {
        insertPattern(signals, locationsToInsertPattern);
        assertThat(SpaceSignals.numberOfOccurrences(signals)).isEqualTo(-1);
    }

    @Provide
    private Arbitrary<List<SignalType>> sequenceWithoutPattern() {
        return Arbitraries.of(SignalType.class)
                .list()
                .filter(x -> Collections.indexOfSubList(x, SpaceSignals.pattern) == -1);
    }

    private void insertPattern(List<SignalType> signals, List<Integer> locations) {
        for (int location : locations.stream()
                .map(x -> signals.size() > 0 ? (x % signals.size()) : 0)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList())
        ) {
            signals.addAll(location, SpaceSignals.pattern);
        }
    }
}
