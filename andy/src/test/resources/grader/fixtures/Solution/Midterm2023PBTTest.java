package delft;

import net.jqwik.api.*;
import net.jqwik.api.constraints.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class StringUtilsTests {
    @Property
    public void test1(@ForAll("good") @Size(5) List<String> goodList, @ForAll("bad") @Size(5) List<String> badList){
        List<String> total = new ArrayList<>();
        total.addAll(goodList);
        total.addAll(badList);
        Collections.shuffle(total);
        List<Integer> indiceList = StringUtils.indices(total);
        List<String> result = new ArrayList<>();
        for(Integer i : indiceList){
            result.add(total.get(i));
        }
        assertThat(result).containsExactlyInAnyOrderElementsOf(goodList);
    }

    @Provide
    public Arbitrary<List<String>> good() {
        Arbitrary<String> words = Arbitraries.strings().alpha().ofLength(5).excludeChars('a', 'b', 'c', 'd', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');
        return words.list();
    }

    @Provide
    public Arbitrary<List<String>> bad() {
        Arbitrary<String> words = Arbitraries.strings().alpha().ofLength(5).excludeChars('e', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');
        return words.list();
    }
}
