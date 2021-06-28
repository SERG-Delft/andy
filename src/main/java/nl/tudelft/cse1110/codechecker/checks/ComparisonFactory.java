package nl.tudelft.cse1110.codechecker.checks;

public class ComparisonFactory {

    private static Comparison LT = ((actual, expected) -> actual<expected);
    private static Comparison LTE = ((actual, expected) -> actual<=expected);
    private static Comparison GT = ((actual, expected) -> actual>expected);
    private static Comparison GTE = ((actual, expected) -> actual>=expected);
    private static Comparison EQ = ((actual, expected) -> actual==expected);

    public static Comparison build(String name) {
        switch (name) {
            case "LT":
                return LT;
            case "LTE":
                return LTE;
            case "GT":
                return GT;
            case "GTE":
                return GTE;
            case "EQ":
                return EQ;
            default:
                throw new RuntimeException("Comparison not found");
        }
    }
}
