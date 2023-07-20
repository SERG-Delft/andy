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

class ArrayUtilsSimpleTests {

    /** Use this method to convert a list of integers to an array */
    private int[] convertListToArray(List<Integer> numbers) {
        int[] array = numbers.stream().mapToInt(x -> x).toArray();
        return array;
    }

    /** Test when the element specified is certainly not in the array.
     *  The assumption we make is pretty unreasonable and this property
     *  will be reported as 'exhausted' by jqwik.
     */
    @Property
    void testNoElementInWholeArray(
            @ForAll @Size(value=10) List<@IntRange(min = -2100000000, max = 2100000000) Integer> numbers,
            @ForAll int valueToFind) {

        Assume.that(valueToFind > 2100000000 || valueToFind < -2100000000);

        int[] arr = convertListToArray(numbers);
        assertEquals(-1, ArrayUtils.indexOf(arr, valueToFind, 0));
    }

}