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
        return List.of("delft.StackOverflow");
    }

    @Override
    public CheckScript checkScript() {
        return new CheckScript(List.of(
                new SingleCheck("UserRepository should be mocked", 
                        new MockClass("UserRepository")),
                new SingleCheck("Scoring should be mocked", 
                        new MockClass("Scoring")),
                new SingleCheck("StackOverflow should not be mocked", true, 
                        new MockClass("StackOverflow")),
                new SingleCheck("Post should not be mocked", true, 
                        new MockClass("Post")),
                new SingleCheck("Spies should not be used", true, 
                        new MockitoSpy()),
                new SingleCheck(2, "pointsForFeaturedPost should be set up", 
                        new MockitoWhen("pointsForFeaturedPost", 
                                Comparison.GTE, 1)),
                new SingleCheck(2, "pointsForNormalPost should be set up", 
                        new MockitoWhen("pointsForNormalPost", 
                                Comparison.GTE, 1)),
                new SingleCheck("set up pointsForFeaturedPost just once", 
                        new MockitoWhen("pointsForFeaturedPost", 
                                Comparison.EQ, 1)),
                new SingleCheck("set up pointsForNormalPost just once", 
                        new MockitoWhen("pointsForNormalPost", 
                                Comparison.EQ, 1)),
                new OrCheck(3, "update should be verified in both tests", Arrays.asList(
                        new SingleCheck(new MockitoVerify("update", MockitoVerify.MethodType.TEST, 
                                Comparison.EQ, 2)),
                        new SingleCheck(new MockitoVerify("update", MockitoVerify.MethodType.AFTEREACH, 
                                Comparison.EQ, 1))
                )),
                new SingleCheck("tests should have assertions", 
                        new TestMethodsHaveAssertions()),
                new SingleCheck(3, "getPoints should have an assertion", 
                        new MethodCalledInTestMethod("getPoints"))
        ));
    }


    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.withStringReplacement("does not update",
                        "userRepository.update(author);",
                        ""),
                MetaTest.withLineReplacement("does not add points if post is not featured", 15, 21,
                        """
                         User author = post.getAuthor();
                         if (post.isFeatured()) {
                            author.addPoints(scoring.pointsForFeaturedPost());
                         } 
                         userRepository.update(author);
                         """),
                MetaTest.withLineReplacement("change condition", 15, 21,
                        """
                        User author = post.getAuthor();
                        if (!post.isFeatured()) {
                            author.addPoints(scoring.pointsForFeaturedPost());
                        } else {
                            author.addPoints(scoring.pointsForNormalPost());
                        }
                        userRepository.update(author);       
                        """)
                );
    }

}
