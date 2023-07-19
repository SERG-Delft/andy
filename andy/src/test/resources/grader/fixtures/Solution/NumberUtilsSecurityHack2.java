package delft;

import nl.tudelft.cse1110.andy.config.RunConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import java.io.File;


//  --- JUnit execution
//      4/4 passed
class NumberUtilsTests {

    private static Class aClass = null;
    private static boolean found = false;

    private static List<String> classesUnderTest = new ArrayList<>();

    static {
        // trying to load even before the test is executed, but when the
        // test file is loaded by the JVM
        try {
            aClass = Class.forName("delft.Configuration");
            RunConfiguration runCfg = (RunConfiguration) aClass.newInstance();
            classesUnderTest = runCfg.classesUnderTest();
            found = true;
        } catch(Exception e) {
            found = false;
        }
    }
    @Test
    void t1() {
        assertThat(classesUnderTest).isEmpty();
        assertThat(found).isFalse();
    }
}

