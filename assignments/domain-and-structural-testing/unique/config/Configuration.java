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
            put("coverage", 0.2f); 
            put("mutation", 0.2f);
            put("meta", 0.6f);
            put("codechecks", 0.0f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.MathArrays");
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.insertAt("the array is empty", 28,
                        """
                         if (data.length == 0) {
                            throw new RuntimeException("killed the mutant");
                         }
                         """),
                MetaTest.insertAt("the array has one element", 28,
                        """
                         if (data.length == 1) {
                            throw new RuntimeException("killed the mutant");
                         }
                         """),
                MetaTest.insertAt("the array is null", 28,
                        """
                         if (data == null) {
                            throw new RuntimeException("killed the mutant");
                         }
                         """),
                MetaTest.withLineReplacement("sorts the array in ascending order", 32, 38,
                        """
                        final int count = 0;
                        final double[] out = new double[count];
                        Iterator<Double> iterator = values.iterator();
                        int i = 0;
                        while (iterator.hasNext()) {
                            out[count + i++] = iterator.next();
                        }
                        """),
                MetaTest.withStringReplacement("negates condition in while loop",
                        "(iterator.hasNext())",
                        "(!iterator.hasNext())"),
                MetaTest.insertAt("the array has duplicates", 33,
                        """
                        if (count < data.length) {
                            throw new RuntimeException("killed the mutant");
                        }
                        """),
                MetaTest.insertAt("the array does not have duplicates", 33,
                        """
                        if (count == data.length) {
                            throw new RuntimeException("killed the mutant");
                        }
                        """),
                MetaTest.withStringReplacement("skips the last element of the array",
                        "i < data.length",
                        "i < data.length - 1"),
                MetaTest.withStringReplacement("skips the first element of the array",
                        "int i = 0",
                        "int i = 1")
        );
    }

}


