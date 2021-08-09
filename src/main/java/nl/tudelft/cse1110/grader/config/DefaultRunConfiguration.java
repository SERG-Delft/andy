package nl.tudelft.cse1110.grader.config;

import nl.tudelft.cse1110.grader.util.ClassUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultRunConfiguration extends RunConfiguration {

    private DirectoryConfiguration dirCfg;

    public DefaultRunConfiguration(DirectoryConfiguration dirCfg) {
        this.dirCfg = dirCfg;
    }

    @Override
    public Map<String, Float> weights() {
        return new HashMap<>() {{
            put("coverage", 0.5f);
            put("mutation", 0.3f);
            put("meta", 0.1f);
            put("codechecks", 0.1f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return ClassUtils.allClassesButTestingAndConfigOnes(this.dirCfg.getNewClassNames());
    }
}
