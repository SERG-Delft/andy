package nl.tudelft.cse1110.andy.config;

import com.google.common.collect.ImmutableMap;
import nl.tudelft.cse1110.andy.execution.externalprocess.ExternalProcess;
import nl.tudelft.cse1110.andy.execution.mode.Mode;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecureExamRunConfiguration extends RunConfiguration {

    private final List<String> classesUnderTest;
    private final String successMessage;
    private final List<String> listOfMutants;
    private final int numberOfMutationsToConsider;
    private final ExternalProcess externalProcess;

    public SecureExamRunConfiguration(RunConfiguration runConfigurationToClone) {
        this.classesUnderTest = runConfigurationToClone.classesUnderTest();
        this.listOfMutants = runConfigurationToClone.listOfMutants();
        this.numberOfMutationsToConsider = runConfigurationToClone.numberOfMutationsToConsider();
        this.externalProcess = runConfigurationToClone.externalProcess();
        this.successMessage = runConfigurationToClone.successMessage();
    }

    public Mode mode() {
        return Mode.EXAM;
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

    public List<MetaTest> metaTests() {
        return Collections.emptyList();
    }

    @Override
    public List<String> classesUnderTest() {
        return Collections.unmodifiableList(classesUnderTest);
    }

    public List<String> listOfMutants() {
        return Collections.unmodifiableList(listOfMutants);
    }

    public int numberOfMutationsToConsider() {
        return numberOfMutationsToConsider;
    }

    public ExternalProcess externalProcess() {
        return externalProcess;
    }

    public String successMessage() {
        return successMessage;
    }


}
