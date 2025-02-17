package delft;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MethodCalled {

    SomeRepo repo2 = new SomeRepo();

    class SomeRepo {
        void update(String x) {}
        boolean save() { return true; }
        boolean persist() { return true; }
        void retrieve(){}
    }

    class Utils {
        int fromString(String x) { return 1; }
    }

    @Test void t1() {

        SomeRepo repo = new SomeRepo();
        repo.update("a");

    }
    @Test void t2() {
        assertThat(repo2.save()).isTrue();
    }

    @Test void t3() {
        assertEquals(true, repo2.persist());
    }

    void t4() {
        SomeRepo repo = new SomeRepo();
        repo.retrieve();
    }

    @Test void t5() {
        assertEquals(1, Utils.fromString("1"));
        assertEquals(1, MethodCalled.Utils.fromString("1"));
    }
}
