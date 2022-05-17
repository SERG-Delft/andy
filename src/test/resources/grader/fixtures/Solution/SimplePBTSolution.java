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

class SimplePBTLibrarySolution {

    static int i = 0;

    @Property
    void testPositive(
            @ForAll @Positive int number) {
        System.out.println(i++);

        int result = new SimplePBTLibrary().calculate(number);
        assertThat(result).isPositive();
    }
}

