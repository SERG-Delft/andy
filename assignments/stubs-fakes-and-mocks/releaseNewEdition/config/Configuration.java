package delft;

import nl.tudelft.cse1110.andy.codechecker.checks.*;
import nl.tudelft.cse1110.andy.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.andy.codechecker.engine.SingleCheck;
import nl.tudelft.cse1110.andy.codechecker.engine.OrCheck;
import nl.tudelft.cse1110.andy.config.MetaTest;
import nl.tudelft.cse1110.andy.config.RunConfiguration;

import java.util.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration extends RunConfiguration {

    @Override
    public Map<String, Float> weights() {
        return new HashMap<>() {{
            put("coverage", 0.25f);
            put("mutation", 0.25f);
            put("meta", 0.25f);
            put("codechecks", 0.25f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.ReleaseEditions");
    }

    @Override
    public CheckScript checkScript() {
        return new CheckScript(List.of(
                new SingleCheck("EmailService should be mocked",
                        new MockClass("EmailService")),
                new SingleCheck("BookService should be mocked",
                        new MockClass("BookService")),
                new SingleCheck("BusinessImpl should not be mocked", true,
                        new MockClass("BusinessImpl")),
                new SingleCheck("Spies should not be used", true,
                        new MockitoSpy()),
                new SingleCheck(2, "retrieveBooks should not be verified", true,
                        new MockitoVerify("retrieveBooks", MockitoVerify.MethodType.TEST,
                                Comparison.GTE, 1)),
                new SingleCheck("addBook should be verified",
                        new MockitoVerify("addBook", MockitoVerify.MethodType.TEST,
                                Comparison.GTE, 1, true))
        ));
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.withLineReplacement("change condition", 45, 50,
                        """
		                List<String> allBooks = bookService.retrieveBooks(author);
		                for (String bookTitle : allBooks) {
			                if (!bookTitle.contains(keyword)) {
				                bookService.addBook(author, bookTitle + " - edition " + edition);
			                }
		                }
                        """
                )
        );
    }

}

