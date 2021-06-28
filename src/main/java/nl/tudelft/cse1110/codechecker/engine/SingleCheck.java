package nl.tudelft.cse1110.codechecker.engine;

import nl.tudelft.cse1110.codechecker.checks.Check;
import nl.tudelft.cse1110.codechecker.checks.CheckFactory;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SingleCheck extends CheckType {

    private String rule;
    private boolean flip=false;
    private String params;

    private Check check = null;

    private CheckFactory checkFactory = new CheckFactory();

    @Deprecated // for jackson
    public SingleCheck(){
        super(1, null);
    }

    public SingleCheck(int weight, String rule, String description, boolean flip, String params, CheckFactory factory) {
        super(weight, description);

        this.rule = rule;
        this.flip = flip;
        this.params = params;

        this.checkFactory = factory;
    }

    public SingleCheck(int weight, String rule, String description, boolean flip, String params) {
        this(weight, rule, description, flip, params, new CheckFactory());
    }

    public SingleCheck(String rule, String params) {
        this(1, rule, null, false, params, new CheckFactory());
    }

    public SingleCheck(String rule) {
        this(rule, null);
    }

    @Override
    public boolean getFinalResult() {
        assert check != null : "You have to run the check first";

        // this is basically the same as return flip ? !result : result
        // in other words, if the flip boolean is active, it flips the result of the checker
        // but it's not every day that you can write an xor, is it?
        return flip ^ check.result();
    }

    @Override
    public void runCheck(CompilationUnit cu) {
        this.check = checkFactory.build(rule, getParamsAsList());
        this.check.check(cu);
    }

    private List<String> getParamsAsList() {
        if (params == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(params.split(" "));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SingleCheck that = (SingleCheck) o;
        return flip == that.flip && rule.equals(that.rule) && Objects.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rule, flip, params);
    }
}
