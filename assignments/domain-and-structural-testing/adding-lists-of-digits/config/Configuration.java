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
            put("codechecks", 0.0f);
            put("meta", 0.8f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.NumberUtils");
    }

    @Override
    public int numberOfMutationsToConsider() {
        return 28;
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.withLineReplacement(4, "applies multiple carries wrongly", 56, 65,
                        """
                        int carry = 0;
                        for (int i = 0; i < Math.max(reversedLeft.size(), reversedRight.size()); i++) {
                                        
                            int leftDigit = reversedLeft.size() > i ? reversedLeft.get(i) : 0;
                            int rightDigit = reversedRight.size() > i ? reversedRight.get(i) : 0;
                                        
                            if (leftDigit < 0 || leftDigit > 9 || rightDigit < 0 || rightDigit > 9)
                                throw new IllegalArgumentException();
                                        
                            int sum;
                                        
                            if (leftDigit + rightDigit >= 10) {
                                sum = leftDigit + rightDigit;
                                carry = 1;
                            }
                            else {
                                sum = leftDigit + rightDigit + carry;
                                carry = 0;
                            }
                            result.addFirst(sum % 10);
                        }
                        """),
                MetaTest.withLineReplacement(4, "does not apply carry at all", 56, 70,
                        """
                        for (int i = 0; i < Math.max(reversedLeft.size(), reversedRight.size()); i++) {
                                                  
                             int leftDigit = reversedLeft.size() > i ? reversedLeft.get(i) : 0;
                             int rightDigit = reversedRight.size() > i ? reversedRight.get(i) : 0;
                                                  
                             if (leftDigit < 0 || leftDigit > 9 || rightDigit < 0 || rightDigit > 9)
                                 throw new IllegalArgumentException();
                                                  
                             int sum = leftDigit + rightDigit;
                                                 
                             result.addFirst(sum % 10);
                         }
                                                  
                         // remove leading zeroes from the result
                         while (result.size() > 1 && result.get(0) == 0)
                             result.remove(0);
                                                  
                         if (result.isEmpty()) {
                             result.addFirst(0);
                         }
                         """),
                MetaTest.withLineReplacement(4, "does not apply last carry", 67, 70,
                        """
                        while (result.size() > 1 && result.get(0) == 0)
                            result.remove(0);
                                                
                        if (result.isEmpty()) {
                            result.addFirst(0);
                        }
                        """),
                MetaTest.withStringReplacement(2, "does not check null left",
                        "if (left == null || right == null)",
                        "if (right == null)"),
                MetaTest.withStringReplacement(2, "does not check null right",
                        "if (left == null || right == null)",
                        "if (left == null)"),
                MetaTest.withStringReplacement(1, "does not check number range left high",
                        "if (leftDigit < 0 || leftDigit > 9 || rightDigit < 0 || rightDigit > 9)",
                        "if (leftDigit < 0 || rightDigit < 0 || rightDigit > 9)"),
                MetaTest.withStringReplacement(1, "does not check number range left negative",
                        "if (leftDigit < 0 || leftDigit > 9 || rightDigit < 0 || rightDigit > 9)",
                        "if (leftDigit > 9 || rightDigit < 0 || rightDigit > 9)"),
                MetaTest.withStringReplacement(1, "does not check number range right high",
                        "if (leftDigit < 0 || leftDigit > 9 || rightDigit < 0 || rightDigit > 9)",
                        "if (leftDigit < 0 || leftDigit > 9 || rightDigit < 0)"),
                MetaTest.withStringReplacement(1, "does not check number range right negative",
                        "if (leftDigit < 0 || leftDigit > 9 || rightDigit < 0 || rightDigit > 9)",
                        "if (leftDigit < 0 || leftDigit > 9 || rightDigit > 9)"),
                MetaTest.withLineReplacement(4, "does not remove leading zeroes", 66, 70,
                        """
                        // if there's some leftover carry, add it to the final number
                        if (carry > 0)
                            result.addFirst(carry);
                                                        
                        if (result.isEmpty()) {
                            result.addFirst(0);
                        }
                        """),
                MetaTest.withLineReplacement(2, "fails when left is longer than right", 57, 59,
                        """
                        for (int i = 0; i < reversedRight.size(); i++) {
                                                
                            int leftDigit = reversedLeft.size() > i ? reversedLeft.get(i) : 0;
                            int rightDigit = reversedRight.get(i);
                        """),
                MetaTest.withLineReplacement(2, "fails when right is longer than left", 57, 59,
                        """
                        for (int i = 0; i < reversedLeft.size(); i++) {
                                          
                            int leftDigit = reversedLeft.get(i);
                            int rightDigit = reversedRight.size() > i ? reversedRight.get(i) : 0;
                        """),
                MetaTest.withLineReplacement(4, "only works for single digits", 48, 70,
                        """
                        LinkedList<Integer> result = new LinkedList<>();
                                                
                        int leftDigit = left.isEmpty() ? 0 : left.get(0);
                        int rightDigit = right.isEmpty() ? 0 : right.get(0);
                                                
                        if (leftDigit < 0 || leftDigit > 9 || rightDigit < 0 || rightDigit > 9)
                            throw new IllegalArgumentException();
                                                
                        int sum = leftDigit + rightDigit;
                                                
                        result.addFirst(sum % 10);
                        int carry = sum / 10;
                                                
                        if (carry > 0)
                            result.addFirst(carry);
                        """),
                MetaTest.withStringReplacement(2, "returns null for empty left",
                        "if (left == null || right == null)",
                        "if (left == null || right == null || left.isEmpty())"),
                MetaTest.withStringReplacement(2, "returns null for empty right",
                        "if (left == null || right == null)",
                        "if (left == null || right == null || right.isEmpty())"),
                MetaTest.withStringReplacement(2, "skips first element",
                        "int i = 0;",
                        "int i = 1;"),
                MetaTest.withStringReplacement(2, "skips last element",
                        "i < Math.max(reversedLeft.size(), reversedRight.size());",
                        "i < Math.max(reversedLeft.size(), reversedRight.size())-1;")
        );
    }

}