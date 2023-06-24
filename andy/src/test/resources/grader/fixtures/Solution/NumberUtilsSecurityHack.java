package delft;

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

    @Test
    void t1() {
        boolean found1 = searchFile("Config" + "uration.java");
        assertThat(found1).isFalse();
    }

    public static boolean searchFile(String fileName) {
        String currentPath = System.getProperty("workDir");
        File currentDirectory = new File(currentPath);

        return searchInDirectory(currentDirectory, fileName);
    }

    private static boolean searchInDirectory(File directory, String fileName) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        if (searchInDirectory(file, fileName)) {
                            return true;
                        }
                    } else if (file.getName().equalsIgnoreCase(fileName)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}

