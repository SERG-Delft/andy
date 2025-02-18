package delft;

import net.jqwik.api.Example;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.api.Test;

public class MixOfJqwikAndExampleBasedTests {
    @Property void t1() {}
    @Property void t2() {}
    @Property void t3() {}

    @Example void t4() {}
    @ParameterizedTest void t5() {}
    @Test void t6() {}

    @Provide("a") void p1() {}
    @Provide void p2() {}
    @Provide void p3() {}
}
