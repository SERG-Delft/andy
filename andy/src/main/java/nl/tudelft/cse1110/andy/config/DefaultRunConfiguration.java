package nl.tudelft.cse1110.andy.config;

import com.google.common.collect.ImmutableMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultRunConfiguration extends RunConfiguration {

    private final List<String> classesUnderTest;

    public DefaultRunConfiguration(List<String> classesUnderTest) {
        this.classesUnderTest = classesUnderTest;
    }

    @Override
    public Map<String, Float> weights() {
        return new HashMap<>(ImmutableMap.of(
            "coverage", 0.25f,
            "mutation", 0.25f,
            "meta", 0.25f,
            "codechecks", 0.25f
        ));
    }

    @Override
    public List<String> classesUnderTest() {
        return Collections.unmodifiableList(classesUnderTest);
    }

}
