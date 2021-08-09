package nl.tudelft.cse1110.grader.config;

import nl.tudelft.cse1110.grader.util.ClassUtils;

import java.util.List;

public class DefaultRunConfiguration extends RunConfiguration {

    private DirectoryConfiguration dirCfg;

    public DefaultRunConfiguration(DirectoryConfiguration dirCfg) {
        this.dirCfg = dirCfg;
    }

    @Override
    public List<String> classesUnderTest() {
        return ClassUtils.allClassesButTestingAndConfigOnes(this.dirCfg.getNewClassNames());
    }
}
