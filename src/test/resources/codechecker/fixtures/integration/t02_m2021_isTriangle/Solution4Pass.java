package fixtures.integration.t02_m2021_isTriangle;

import net.jqwik.api.*;
import net.jqwik.api.arbitraries.IntegerArbitrary;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Solution4Pass {

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

    @Provide
    Arbitrary<ABC> notProperTriangle() {
        IntegerArbitrary a = Arbitraries.integers().greaterOrEqual(1);
        IntegerArbitrary b = Arbitraries.integers().greaterOrEqual(1);
        IntegerArbitrary c = Arbitraries.integers().greaterOrEqual(1);
        return Combinators.combine(a,b,c).as(ABC::new)
                .filter(x -> x.a >= x.b + x.c || x.b >= x.a + x.c  || x.c >= x.b + x.a);
    }

    @Provide
    Arbitrary<ABC> properTriangle() {
        IntegerArbitrary a = Arbitraries.integers().greaterOrEqual(1);
        IntegerArbitrary b = Arbitraries.integers().greaterOrEqual(1);
        IntegerArbitrary c = Arbitraries.integers().greaterOrEqual(1);
        return Combinators.combine(a,b,c).as(ABC::new)
                .filter(x -> x.a < x.b + x.c && x.b < x.a + x.c && x.c < x.b + x.a);
    }

    @Property
    void isTriangle(@ForAll("properTriangle") ABC t) {
        assertTrue(Triangle.isTriangle(t.a, t.b, t.c));
    }

    @Property
    void isNotTriangle(@ForAll("notProperTriangle") ABC t) {
        assertFalse(Triangle.isTriangle(t.a, t.b, t.c));
    }
}
