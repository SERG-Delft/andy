package nl.tudelft.cse1110.andy.grader.util;

public class ModeUtils {

    private static String getMode() {
        return System.getenv("MODE");
    }

    public static boolean tests() {
        return getMode().equals("TESTS");
    }

    public static boolean coverage() {
        return getMode().equals("COVERAGE");
    }

    public static boolean noHints() {
        return getMode().equals("NO_HINTS");
    }

    public static boolean hints() {
        return getMode().equals("HINTS");
    }

}
