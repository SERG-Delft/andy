package nl.tudelft.cse1110.andy.config;

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

    public SecureExamRunConfiguration(RunConfiguration runConfigurationToClone) {
        this.classesUnderTest = runConfigurationToClone.classesUnderTest();
        this.listOfMutants = runConfigurationToClone.listOfMutants();
        this.numberOfMutationsToConsider = runConfigurationToClone.numberOfMutationsToConsider();
        this.successMessage = runConfigurationToClone.successMessage();
    }

    public Mode mode() {
        return Mode.EXAM;
    }

    @Override
    @SuppressWarnings("DoubleBraceInitialization")
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
        return Collections.unmodifiableList(classesUnderTest);
    }

    public List<String> listOfMutants() {
        return Collections.unmodifiableList(listOfMutants);
    }

    public int numberOfMutationsToConsider() {
        return numberOfMutationsToConsider;
    }

    public String successMessage() {
        return successMessage;
    }


}
