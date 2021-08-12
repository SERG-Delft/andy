package nl.tudelft.cse1110.grader.config;

import kotlin.DeepRecursiveFunction;
import nl.tudelft.cse1110.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.grader.execution.MetaTest;
import nl.tudelft.cse1110.grader.util.ClassUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class RunConfiguration {

    protected static final List<String> OLD_DEFAULTS = List.of("CONDITIONALS_BOUNDARY", "INCREMENTS", "INVERT_NEGS", "MATH",
            "NEGATE_CONDITIONALS", "RETURN_VALS", "VOID_METHOD_CALLS");

    protected static final List<String> DEFAULTS = List.of("CONDITIONALS_BOUNDARY", "INCREMENTS", "INVERT_NEGS", "MATH",
            "NEGATE_CONDITIONALS", "VOID_METHOD_CALLS", "EMPTY_RETURNS", "FALSE_RETURNS", "TRUE_RETURNS",
            "NULL_RETURNS", "PRIMITIVE_RETURNS");

    protected static final List<String> STRONGER = List.of("CONDITIONALS_BOUNDARY", "INCREMENTS", "INVERT_NEGS", "MATH",
            "NEGATE_CONDITIONALS", "VOID_METHOD_CALLS", "EMPTY_RETURNS", "FALSE_RETURNS", "TRUE_RETURNS",
            "NULL_RETURNS", "PRIMITIVE_RETURNS", "REMOVE_CONDITIONALS_EQ_ELSE", "EXPERIMENTAL_SWITCH");

    protected static final List<String> ALL = List.of("CONDITIONALS_BOUNDARY", "INCREMENTS", "INVERT_NEGS", "MATH",
            "NEGATE_CONDITIONALS", "VOID_METHOD_CALLS", "EMPTY_RETURNS", "FALSE_RETURNS", "TRUE_RETURNS",
            "NULL_RETURNS", "PRIMITIVE_RETURNS", "REMOVE_CONDITIONALS", "EXPERIMENTAL_SWITCH",
            "INLINE_CONSTS", "NON_VOID_METHOD_CALLS", "REMOVE_CONDITIONALS", "REMOVE_INCREMENTS", "EXPERIMENTAL_ARGUMENT_PROPAGATION",
            "EXPERIMENTAL_BIG_INTEGER", "EXPERIMENTAL_NAKED_RECEIVER", "EXPERIMENTAL_MEMBER_VARIABLE", "ABS",
            "AOR", "AOD", "CRCR", "OBBN", "ROR", "UOI");

    public abstract List<String> classesUnderTest();

    public abstract Map<String, Float> weights();

    public abstract CheckScript checkScript();

    public abstract List<MetaTest> metaTests();

    public boolean failureGivesZero() {
        return true;
    }

    public List<String> listOfMutants() {
        return DEFAULTS;
    }

}
