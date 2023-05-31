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
            put("coverage", 0.25f); 
            put("mutation", 0.25f);
            put("meta", 0.5f);
            put("codechecks", 0.0f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.ArrayUtils");
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.insertAt("the array is empty", 19,
                        """
                        if (array != null && array.length == 0) {
                            throw new RuntimeException("killed the mutant");
                        }
                        """),
                MetaTest.insertAt("the array has one element", 19,
                        """
                        if (array != null && array.length == 1) {
                            throw new RuntimeException("killed the mutant");
                        }
                        """),
                MetaTest.insertAt("the array is null", 19,
                        """
                        if (array == null) {
                            throw new RuntimeException("killed the mutant");
                        }
                        """),
                MetaTest.withLineReplacement("does not return true when the array is null or has less than 2 elements", 
                        19, 21, 
                        ""
                ),
                MetaTest.withStringReplacement("iterates from 0 to n - 2",
                        "array[i]", "array[i-1]"),
                MetaTest.withLineReplacement("does not work correctly if previous > current",
                        27, 27,
                        "return true;"
                ),
                MetaTest.withLineReplacement("does not work correctly if the array is sorted",
                        31, 31,
                        "return false;"
                ),
                MetaTest.withLineReplacement("previous is always the first element",
                        29, 29,
                        ""
                ),
                MetaTest.insertAt("the array is sorted", 22,
                        """
                        int j;
                        for(j = 0; j < array.length - 1 && array[j] < array[j+1]; j++) {}
                        boolean sorted = (j == array.length - 1);
                        if (sorted == true) {
                            throw new RuntimeException("killed the mutant");
                        }
                         """),
                MetaTest.insertAt("the array is not sorted", 22,
                        """
                        int j;
                        for(j = 0; j < array.length - 1 && array[j] < array[j+1]; j++) {}
                        boolean sorted = (j == array.length - 1);
                        if (sorted == false) {
                            throw new RuntimeException("killed the mutant");
                        }
                         """)
        );
    }

}
