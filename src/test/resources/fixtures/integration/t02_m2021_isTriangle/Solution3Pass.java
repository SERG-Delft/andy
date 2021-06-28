package fixtures.integration.t02_m2021_isTriangle;

import net.jqwik.api.*;
import net.jqwik.api.arbitraries.IntegerArbitrary;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Solution3Pass {

    // use the ABC class below.
    class ABC {

        int a;

        int b;

        int c;

        public ABC(int a, int b, int c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }

    @Property
    void oneSideBiggerThanSumOfOthers(@ForAll("oneBigger") ABC input) {
        assertFalse(Triangle.isTriangle(input.a, input.b, input.c));
    }

    @Property
    void allSidesSmallerThanSumOfOthers(@ForAll("allSmaller") ABC input) {
        assertTrue(Triangle.isTriangle(input.a, input.b, input.c));
    }

    @Provide
    private Arbitrary<ABC> oneBigger() {
        IntegerArbitrary a = Arbitraries.integers().greaterOrEqual(1);
        IntegerArbitrary b = Arbitraries.integers().greaterOrEqual(1);
        IntegerArbitrary c = Arbitraries.integers().greaterOrEqual(1);
        return Combinators.combine(a,b,c).as(ABC::new)
                .filter(i -> (i.a >= i.b + i.c) || (i.b >= i.a + i.c) || (i.c >= i.a + i.b));
    }

    @Provide
    private Arbitrary<ABC> allSmaller() {
        IntegerArbitrary a = Arbitraries.integers().greaterOrEqual(1);
        IntegerArbitrary b = Arbitraries.integers().greaterOrEqual(1);
        IntegerArbitrary c = Arbitraries.integers().greaterOrEqual(1);
        return Combinators.combine(a,b,c).as(ABC::new)
                .filter(i -> (i.a < i.b + i.c) && (i.b < i.a + i.c) && (i.c < i.a + i.b));
    }
}
