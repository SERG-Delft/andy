package delft;

import nl.tudelft.cse1110.andy.codechecker.checks.*;
import nl.tudelft.cse1110.andy.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.andy.codechecker.engine.SingleCheck;
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
        return List.of("delft.NumberUtils");
    }

    @Override
    public int numberOfMutationsToConsider() {
        return 32;
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.withStringReplacement(4, "applies carry wrongly for equal digits",
                        """
                        int subtraction = leftDigit - carry - rightDigit;
                        if (subtraction < 0) {
                            subtraction += 10;
                            carry = 1;
                        } else {
                            carry = 0;
                        }
                        """,
                        """
                        int subtraction = (leftDigit + (leftDigit < rightDigit ? 10 : 0)) - rightDigit - carry;
                        carry = leftDigit < rightDigit ? 1 : 0;
                        """),
                
                MetaTest.withStringReplacement(4, "applies multiple carries wrongly",
                        """
                        int subtraction = leftDigit - carry - rightDigit;
                        if (subtraction < 0) {
                            subtraction += 10;
                            carry = 1;
                        } else {
                            carry = 0;
                        }
                        """,
                        """
                        int subtraction;
                        if (leftDigit - rightDigit < 0) {
                            subtraction = leftDigit - rightDigit + 10;
                            carry = 1;
                        } else {
                            subtraction = leftDigit - carry - rightDigit;
                            carry = 0;
                        }
                        """),
                
                MetaTest.withLineReplacement(4, "does not apply carry at all", 57, 75,
                        """
                        for (int i = 0; i < Math.max(reversedLeft.size(), reversedRight.size()); i++) {
                            int leftDigit = reversedLeft.size() > i ? reversedLeft.get(i) : 0;
                            int rightDigit = reversedRight.size() > i ? reversedRight.get(i) : 0;
                        
                            if (leftDigit < 0 || leftDigit > 9 || rightDigit < 0 || rightDigit > 9)
                                throw new IllegalArgumentException();
                        
                            int subtraction = leftDigit - rightDigit;
                            if (subtraction < 0) {
                                subtraction += 10;
                            }
                        
                            result.addFirst(subtraction);
                        }
                        """),
                
                MetaTest.withStringReplacement(4, "does not check left greater than right",
                        """
                        // If there is leftover carry, it means left<right, which is not supported
                        if (carry > 0)
                            throw new IllegalArgumentException();
                        """, ""),
                
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
                
                MetaTest.withStringReplacement(4, "does not remove leading zeroes",
                        """
                        // remove leading zeroes from the result
                        while (result.size() > 0 && result.get(0) == 0)
                            result.remove(0);
                        """, ""),
                
                MetaTest.withLineReplacement(2, "fails when left is longer than right", 59, 61,
                        """
                        for (int i = 0; i < reversedRight.size(); i++) {
                            int leftDigit = reversedLeft.size() > i ? reversedLeft.get(i) : 0;
                            int rightDigit = reversedRight.get(i);
                        """),
                
                MetaTest.withLineReplacement(2, "fails when right is longer than left", 59, 60,
                        """
                        for (int i = 0; i < reversedLeft.size(); i++) {
                            int leftDigit = reversedLeft.get(i);
                        """),
                
                MetaTest.withLineReplacement(4, "only works for single digits", 50, 81,
                        """
                        LinkedList<Integer> result = new LinkedList<>();
                        
                        int leftDigit = left.isEmpty() ? 0 : left.get(0);
                        int rightDigit = right.isEmpty() ? 0 : right.get(0);
                        
                        if (leftDigit < 0 || leftDigit > 9 || rightDigit < 0 || rightDigit > 9)
                            throw new IllegalArgumentException();
                        
                        int subtraction = leftDigit - rightDigit;
                        if (subtraction >= 0) {
                            result.addFirst(subtraction);
                        } else {
                            // left<right, which is not supported
                            throw new IllegalArgumentException();
                        }
                        """),
                
                MetaTest.withStringReplacement(4, "returns empty list instead of zero",
                        """
                        if (result.isEmpty()) {
                            result.addFirst(0);
                        }
                        """, ""),
                
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
                        "i < Math.max(reversedLeft.size(), reversedRight.size()) - 1;")
        );
    }

}