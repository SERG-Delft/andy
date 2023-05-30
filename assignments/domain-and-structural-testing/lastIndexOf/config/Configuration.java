package delft;

import nl.tudelft.cse1110.andy.config.MetaTest;
import nl.tudelft.cse1110.andy.config.RunConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration extends RunConfiguration {

    @Override
    public Map<String, Float> weights() {
        return new HashMap<>() {{
            put("coverage", 0.1f); 
            put("mutation", 0.1f);
            put("meta", 0.8f);
            put("codechecks", 0.0f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.ArrayUtils");
    }

    @Override
    public int numberOfMutationsToConsider() {
        return 12;
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.insertAt("the startIndex is negative", 42,
                        """
                        if (startIndex < 0) {
                            throw new RuntimeException("killed the mutant");
                        }
                        """),
                MetaTest.insertAt("the startIndex is higher than the last position in the array", 42,
                        """
                        if (startIndex >= array.length) {
                            throw new RuntimeException("killed the mutant");
                        }
                        """),
                MetaTest.insertAt("the array contains the value to find one time, array length > 1", 42,
                        """
                        int count = 0;
                        for (int j = 0; j < array.length; j++) {
                            if (array[j] == valueToFind) count++;
                        }
                        if (count == 1 && array.length > 1) {
                            throw new RuntimeException("killed the mutant");
                        }
                        """),
                MetaTest.insertAt("the array does not contain the value to find, array length > 1", 42,
                        """
                        int count = 0;
                        for (int j = 0; j < array.length; j++) {
                            if (array[j] == valueToFind) count++;
                        }
                        if (count == 1 && array.length > 1) {
                            throw new RuntimeException("killed the mutant");
                        }
                        """),
                MetaTest.insertAt("the array contains the value to find many times, array length > 1", 42,
                        """
                        int count = 0;
                        for (int j = 0; j < array.length; j++) {
                            if (array[j] == valueToFind) count++;
                        }
                        if (count > 1 && array.length > 1) {
                            throw new RuntimeException("killed the mutant");
                        }
                        """),
                MetaTest.insertAt("the array contains one element which is the value to find", 42,
                        """
                        int count = 0;
                        for (int j = 0; j < array.length; j++) {
                            if (array[j] == valueToFind) count++;
                        }
                        if (count != 0 && array.length == 1) {
                            throw new RuntimeException("killed the mutant");
                        }
                        """),
                MetaTest.insertAt("the array contains one element which is not the value to find", 42,
                        """
                        int count = 0;
                        for (int j = 0; j < array.length; j++) {
                            if (array[j] == valueToFind) count++;
                        }
                        if (count == 0 && array.length == 1) {
                            throw new RuntimeException("killed the mutant");
                        }
                        """),
                MetaTest.insertAt("value to find is at the position of the start index", 42,
                        """
                        if (valueToFind == array[startIndex]) {
                            throw new RuntimeException("killed the mutant");
                        }
                        """),
                MetaTest.withStringReplacement("does not handle the case when the index is higher than the array length - 1", 
                        """
                        else if (startIndex >= array.length) {
                            startIndex = array.length - 1;
                        }
                        """, ""
                ),
                MetaTest.withLineReplacement("does not handle invalid index positions", 45, 49,
                        ""
                ),
                MetaTest.withStringReplacement("negates condition", 
                        "valueToFind == array[i]", "valueToFind != array[i]"),
                MetaTest.withStringReplacement("finds the first occurrence of the value",
                        "int i = startIndex; i >= 0; i--", "int i = 0; i <= startIndex; i++"),
                MetaTest.insertAt("the value to find is the first element of the array", 49,
                        """
                        if (valueToFind == array[0])
                            throw new RuntimeException("killed the mutant");
                        """),
                MetaTest.withStringReplacement("does not work correctly when index is found",
                        "return i;", "return INDEX_NOT_FOUND;")
        );
    }

}
