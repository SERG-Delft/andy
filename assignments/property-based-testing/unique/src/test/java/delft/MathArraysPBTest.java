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

    /** Use this method to convert a list of integers to an array */
    private double[] convertListToArray(List<Integer> numbers) {
        double[] array = numbers.stream().mapToDouble(x -> x).toArray();
        return array;
    }
}