package nl.tudelft.cse1110.grader.config;

import nl.tudelft.cse1110.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.codechecker.engine.SingleCheck;
import nl.tudelft.cse1110.grader.util.ClassUtils;

import java.util.Arrays;
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
            put("coverage", 0.25f);
            put("mutation", 0.25f);
            put("meta", 0.25f);
            put("codechecks", 0.25f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return ClassUtils.allClassesButTestingAndConfigOnes(this.dirCfg.getNewClassNames());
    }

    @Override
    public CheckScript checkScript() {
        return new CheckScript(Arrays.asList());
    }
}
