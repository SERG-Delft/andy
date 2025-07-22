package delft;

import java.util.*;
import java.util.stream.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import net.jqwik.api.*;
import net.jqwik.api.Tuple.*;
import net.jqwik.api.arbitraries.*;
import net.jqwik.api.constraints.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ConditionalExpressionUsed {
    private final List<Integer> primesUnder30 = List.of(2, 3, 5, 7, 11, 13, 17, 19, 23, 29);

    public boolean primeFactorsCorrect(List<Integer> factors, int k) {
        boolean foundWrongOne = false;
        int total = 1;
        for (int v : factors) {
            foundWrongOne = (!primesUnder30.contains(v)) ? true : foundWrongOne;
            total *= v;
        }
        return (!foundWrongOne && total == k);
    }
}
