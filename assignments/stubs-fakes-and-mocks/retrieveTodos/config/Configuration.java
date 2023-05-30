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
            put("coverage", 0.0f); // was 1.0
            put("mutation", 0.2f);
            put("meta", 0.3f);
            put("codechecks", 0.5f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.TodoApplication");
    }

    @Override
    public CheckScript checkScript() {
        return new CheckScript(List.of(
                new SingleCheck("PersonService should be mocked",
                        new MockClass("PersonService")),
                new SingleCheck("TodoService should be mocked",
                        new MockClass("TodoService")),
                new SingleCheck("TodoApplication should not be mocked", true,
                        new MockClass("TodoApplication")),
                new SingleCheck("Spies should not be used", true,
                        new MockitoSpy()),
                new SingleCheck("findUsernameById should be set up",
                        new MockitoWhen("findUsernameById", Comparison.GTE, 1)),
                new SingleCheck("retrieveTodos should be set up",
                        new MockitoWhen("retrieveTodos", Comparison.GTE, 1)),
                new SingleCheck("findUsernameById should not be verified", true,
                        new MockitoVerify("findUsernameById", MockitoVerify.MethodType.TEST,
                                Comparison.GTE, 1)),
                new SingleCheck("retrieveTodos should not be verified", true,
                        new MockitoVerify("retrieveTodos", MockitoVerify.MethodType.TEST,
                                Comparison.GTE, 1)),
                new SingleCheck("tests should have assertions",
                        new TestMethodsHaveAssertions())
        ));
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.withStringReplacement(3, "does not filter notes",
                        "return allTodos.stream().filter(t -> t.contains(keyword)).collect(Collectors.toList());",
                        "return allTodos.stream().collect(Collectors.toList());"
                ),
                MetaTest.withStringReplacement("does not find username correctly",
                        "String user = personService.findUsernameById(userId);",
                        "String user = null;"
                )
        );
    }

}
