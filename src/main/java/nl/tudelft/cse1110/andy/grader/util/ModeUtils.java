package nl.tudelft.cse1110.andy.grader.util;

public class ModeUtils {

    public static boolean hints(String mode) {
        return mode.equals("HINTS");
    }

    public static boolean noHints(String mode) {
        return mode.equals("NO_HINTS");
    }

    public static boolean coverage(String mode) {
        return mode.equals("COVERAGE");
    }

    public static boolean tests(String mode) {
        return mode.equals("TESTS");
    }
}
