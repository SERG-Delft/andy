package fixtures.integration.t02_m2021_isTriangle;

import net.jqwik.api.*;
import net.jqwik.api.arbitraries.IntegerArbitrary;
import org.junit.jupiter.api.Assertions;


class Solution1Pass {
    //Assume all values are greater than zero.
    Triangle obj = new Triangle();

    /** Method that asserts all values provided by generator with condition
     * a>=(b+c)
     * Expected false.
     */
    @Property
    void testAGreaterBAndC(@ForAll("aGreaterThanBPlusC") Input input) {
        Assertions.assertEquals(false, obj.isTriangle(input.a,input.b, input.c));
    }

    /** Method that asserts all values provided by generator with condition
     * c>=(b+a)
     * Expected false.
     */
    @Property
    void testCGreaterAAndB(@ForAll("cGreaterThanAPlusB") Input input) {
        Assertions.assertEquals(false, obj.isTriangle(input.a,input.b, input.c));
    }

    /** Method that asserts all values provided by generator with condition
     * b>=(a+c)
     * Expected false.
     */
    @Property
    void testBGreaterAAndC(@ForAll("bGreaterThanAPlusC") Input input) {
        Assertions.assertEquals(false, obj.isTriangle(input.a,input.b, input.c));
    }

    /** Method that asserts all values provided by generator with condition
     * !a>=(b+c) && !b>=(a+c) && !c>(a+b)
     * Expected true.
     */
    @Property
    void testNoneOfTheAbove(@ForAll("returnsTrue") Input input) {
        Assertions.assertEquals(true, obj.isTriangle(input.a,input.b, input.c));
    }

    /** Generator returns arbitrary values where a>=(a+b) holds
     * @return inputs for method to test
     */
    @Provide
    private Arbitrary<Input> aGreaterThanBPlusC() {
        IntegerArbitrary a = Arbitraries.integers().greaterOrEqual(1);
        IntegerArbitrary b = Arbitraries.integers().greaterOrEqual(1);
        IntegerArbitrary c = Arbitraries.integers().greaterOrEqual(1);
        return Combinators.combine(a,b,c).as(Input::new)
                .filter(k -> (k.a >= (k.b+k.c)));
    }

    /** Generator returns arbitrary values where c>=(a+b) holds
     * @return inputs for method to test
     */
    @Provide
    private Arbitrary<Input> cGreaterThanAPlusB() {
        IntegerArbitrary a = Arbitraries.integers().greaterOrEqual(1);
        IntegerArbitrary b = Arbitraries.integers().greaterOrEqual(1);
        IntegerArbitrary c = Arbitraries.integers().greaterOrEqual(1);
        return Combinators.combine(a,b,c).as(Input::new)
                .filter(k -> (k.c >= (k.a+k.b)));
    }

    /** Generator returns arbitrary values where b>=(a+c) holds
     * @return inputs for method to test
     */
    @Provide
    private Arbitrary<Input> bGreaterThanAPlusC() {
        IntegerArbitrary a = Arbitraries.integers().greaterOrEqual(1);
        IntegerArbitrary b = Arbitraries.integers().greaterOrEqual(1);
        IntegerArbitrary c = Arbitraries.integers().greaterOrEqual(1);
        return Combinators.combine(a,b,c).as(Input::new)
                .filter(k -> (k.b >= (k.a+k.c)));
    }

    /** Generator returns all values that don't satisfy any of the conditions
     * specified above.
     * @return inputs for method to test.
     */
    @Provide
    private Arbitrary<Input> returnsTrue() {
        IntegerArbitrary a = Arbitraries.integers().greaterOrEqual(1);
        IntegerArbitrary b = Arbitraries.integers().greaterOrEqual(1);
        IntegerArbitrary c = Arbitraries.integers().greaterOrEqual(1);
        return Combinators.combine(a,b,c).as(Input::new)
                .filter(k -> (!(k.b >= (k.a+k.c))) && !(k.c >= (k.a+k.b)) && !(k.a >= (k.b+k.c)));
    }

}

/**
 * Class container for input parameters.
 */
class Input {
    public int a;
    public int b;
    public int c;

    public Input(int a, int b, int c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
}
