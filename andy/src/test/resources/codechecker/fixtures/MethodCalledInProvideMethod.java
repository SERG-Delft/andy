package delft;

import net.jqwik.api.Property;
import net.jqwik.api.Provide;

public class MethodCalledInProvideMethod {
    @Property
    void t1() {
        Integer.sum(1, 2);
    }

    @Provide("a")
    void p1() {
        Integer.valueOf("1");
    }
    @Provide void p2() {}
}
