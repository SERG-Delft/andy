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
        return List.of("delft.ReadingProgress");
    }

    @Override
    public CheckScript checkScript() {
        return new CheckScript(List.of(
                new SingleCheck("EmailService should be mocked",
                        new MockClass("EmailService")),
                new SingleCheck("UserService should be mocked",
                        new MockClass("UserService")),
                new SingleCheck("BookService should be mocked",
                        new MockClass("BookService")),
                new SingleCheck("ReadingProgress should not be mocked", true,
                        new MockClass("ReadingProgress")),
                new SingleCheck("Spies should not be used", true,
                        new MockitoSpy()),
                new SingleCheck(2, "isMarketingAuthorised should not be verified", true,
                        new MockitoVerify("isMarketingAuthorised", MockitoVerify.MethodType.TEST,
                                Comparison.GTE, 1)),
                // There should exist tests where all methods in EmailService are called, respectively.
                // It should be verified that only the appropriate method was called.
                new SingleCheck("sendKeepUpGoodWorkEmail should be verified",
                        new MockitoVerify("sendKeepUpGoodWorkEmail", MockitoVerify.MethodType.TEST,
                                Comparison.GTE, 3, true)),
                new SingleCheck("sendAlmostThereEmail should be verified",
                        new MockitoVerify("sendAlmostThereEmail", MockitoVerify.MethodType.TEST,
                        Comparison.GTE, 3, true)),
                new SingleCheck("sendCongratsYouHaveMadeItEmail should be verified",
                        new MockitoVerify("sendCongratsYouHaveMadeItEmail", MockitoVerify.MethodType.TEST,
                        Comparison.GTE, 3, true))
        ));
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.withStringReplacement("does not update last visualization",
                        "bookService.updateLastVisualization(bookId, percentageRead);",
                        ""),
                MetaTest.withLineReplacement("does not send email if percentage is 100.0", 37, 47,
                        """
		                bookService.updateLastVisualization(bookId, percentageRead);
		                boolean mkt = userService.isMarketingAuthorised(userId);
		                if (mkt) {
			                if (percentageRead < 50.0) {
				                emailService.sendKeepUpGoodWorkEmail(bookId);
			                } else if (percentageRead < 99.0) {
				                emailService.sendAlmostThereEmail(bookId);
			                }
                        }
                        """
                ),
                MetaTest.withLineReplacement("does not send email if percentage is higher than 50.0", 37, 47,
                        """
		                bookService.updateLastVisualization(bookId, percentageRead);
		                boolean mkt = userService.isMarketingAuthorised(userId);
		                if (mkt) {
			                if (percentageRead < 50.0) {
				                emailService.sendKeepUpGoodWorkEmail(bookId);
			                }
                        }
                        """
                ),
                MetaTest.withLineReplacement("check boundary - 50%", 37, 47,
                        """
		                bookService.updateLastVisualization(bookId, percentageRead);
		                boolean mkt = userService.isMarketingAuthorised(userId);
		                if (mkt) {
			                if (percentageRead <= 50.0) {
				                emailService.sendKeepUpGoodWorkEmail(bookId);
			                } else if (percentageRead < 99.0) {
				                emailService.sendAlmostThereEmail(bookId);
			                } else {
				                emailService.sendCongratsYouHaveMadeItEmail(bookId);
			                }
		                }
                        """
                ),
                MetaTest.withLineReplacement("check boundary - 90%", 37, 47,
                        """
		                bookService.updateLastVisualization(bookId, percentageRead);
		                boolean mkt = userService.isMarketingAuthorised(userId);
		                if (mkt) {
			                if (percentageRead < 50.0) {
				                emailService.sendKeepUpGoodWorkEmail(bookId);
			                } else if (percentageRead <= 99.0) {
				                emailService.sendAlmostThereEmail(bookId);
			                } else {
				                emailService.sendCongratsYouHaveMadeItEmail(bookId);
			                }
		                }
                        """
                )
        );
    }

}


