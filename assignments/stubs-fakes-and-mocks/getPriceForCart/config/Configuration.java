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
            put("coverage", 0.2f);
            put("mutation", 0.2f);
            put("meta", 0.35f);
            put("codechecks", 0.25f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.BookStore");
    }

    @Override
    public int numberOfMutationsToConsider() {
        return 9;
    }

    @Override
    public CheckScript checkScript() {
        return new CheckScript(List.of(
                new SingleCheck("BookRepository should be mocked",
                        new MockClass("BookRepository")),
                new SingleCheck("BuyBookProcess should be mocked",
                        new MockClass("BuyBookProcess")),
                new SingleCheck("Overview should not be mocked", true,
                        new MockClass("Overview")),
                new SingleCheck("HashMap should not be mocked", true,
                        new MockClass("HashMap")),
                new SingleCheck("Book should not be mocked", true,
                        new MockClass("Book")),
                new SingleCheck("Spies should not be used", true,
                        new MockitoSpy()),
                new SingleCheck("findByISBN should be set up",
                        new MockitoWhen("findByISBN", Comparison.GTE, 1)),
                new SingleCheck("buyBook should be verified",
                        new MockitoVerify("buyBook", MockitoVerify.MethodType.TEST,
                                Comparison.GTE, 1)),
                new SingleCheck("getPriceForCart is called in the tests",
                        new MethodCalledInTestMethod("getPriceForCart")),
                new SingleCheck("tests should have assertions",
                        new TestMethodsHaveAssertions())

        ));
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.insertAt("empty order", 82,
                        "if(order.isEmpty()) throw new RuntimeException(\"killed the mutant\");"),
                MetaTest.withStringReplacement("null order",
                        "return null;",
                        "throw new RuntimeException(\"killed the mutant\");"),
                MetaTest.insertAt(2, "quantity equals available", 68,
                        "if(book.getAmount() == amount) throw new RuntimeException(\"killed the mutant\");"),
                MetaTest.insertAt("quantity greater than available", 68,
                        "if(book.getAmount() > amount) throw new RuntimeException(\"killed the mutant\");")
        );
    }

}