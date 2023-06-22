package delft;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;
import net.jqwik.api.*;
import net.jqwik.api.arbitraries.*;
import net.jqwik.api.constraints.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class MathArraysPBTest {

    @Property
    // an array of size 100 with integers between [1,20] will definitely have repeated numbers
    // if you use only @ForAll, you are likely to never have repeated elements in the array, and thus,
    // never exercising the full property!
    void unique( 
            @ForAll
            @Size(value = 100)
            List<@IntRange(min = 1, max = 20) Integer> numbers) {

        double[] doubles = convertListToArray(numbers);
        double[] result = MathArraysPBT.unique(doubles);

        assertThat(result)
                .contains(doubles) // contains all the elements
                .doesNotHaveDuplicates() // no duplicates
                .isSortedAccordingTo(Comparator.reverseOrder()); // in descending order
    }

    /** Use this method to convert a list of integers to an array */
    private double[] convertListToArray(List<Integer> numbers) {
        double[] array = numbers.stream().mapToDouble(x -> x).toArray();
        return array;
    }
}
