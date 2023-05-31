package delft;

import nl.tudelft.cse1110.andy.codechecker.checks.*;
import nl.tudelft.cse1110.andy.codechecker.engine.*;
import nl.tudelft.cse1110.andy.config.MetaTest;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.mode.Mode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Configuration extends RunConfiguration {

    @Override
    public Mode mode() {
        return Mode.PRACTICE;
    }

    @Override
    public Map<String, Float> weights() {
        return new HashMap<>() {{
            put("coverage", 0.05f);
            put("mutation", 0.05f);
            put("meta", 0.90f);
            put("codechecks", 0.0f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.ConnectFour1D");
    }

    @Override
    public CheckScript checkScript() {
        return new CheckScript(List.of(
            new SingleCheck(1, "should have at least one property",
                    new JQWikProperty(Comparison.GTE, 1)
            )
        ));
    }


    @Override
    public List<MetaTest> metaTests() {
        return List.of(
            // MetaTest.withStringReplacement(
            //     "method does not throw exception",
            //     """
            //     if (xWon && oWon) {
            //         throw new IllegalArgumentException(); 
            //     }
            //     """,
            //     ""
            // ),
            MetaTest.withStringReplacement(
                "method only throws exception when runs of X/O are close together",
                "if (xWon && oWon) {",
                """
                if (xWon && oWon && Math.abs(board.indexOf("XXXX") - board.indexOf("OOOO")) < 9) {
                """
            ),
            // MetaTest.withLineReplacement(
            //     "method always returns DRAW",
            //     10, 28,
            //     ""
            // ),
            // MetaTest.withStringReplacement(
            //     "method always returns X_WON (if there is a win)",
            //     "GameResult.O_WON",
            //     "GameResult.X_WON"
            // ),
            // MetaTest.withStringReplacement(
            //     "method always returns O_WON (if there is a win)",
            //     "GameResult.X_WON",
            //     "GameResult.O_WON"
            // ),
            // MetaTest.withStringReplacement(
            //     "method returns X_WON instead of DRAW",
            //     "GameResult.DRAW",
            //     "GameResult.X_WON"
            // ),
            MetaTest.withLineReplacement(
                "player with most letters on the board always wins",
                10, 29,
                """
                    int xCount = board.length() - board.replace("X", "").length();
                    int oCount = board.length() - board.replace("O", "").length();
                    if (xCount == oCount) {
                        return GameResult.DRAW;
                    }
                    else if (xCount > oCount) {
                        return GameResult.X_WON;
                    }
                    else {
                        return GameResult.O_WON;
                    }
                """
            ),
            MetaTest.withStringReplacement(
                2, // weight
                "method considers 3 in a row instead of 4",
                "playerCharacters == 4",
                "playerCharacters == 3"
            ),
            MetaTest.withLineReplacement(
                "method checks character count after loop",
                33, 44,
                """
                    int playerCharacters = 0;
                    for (int i = 0; i < board.length(); i++) {
                        if (board.charAt(i) == player) {
                            playerCharacters++;
                        } else {
                            playerCharacters = 0;
                        }
                    }
                    if (playerCharacters >= 4) {
                        return true;
                    }

                    return false;
                """
            ),
            MetaTest.withStringReplacement(
                "method ignores first character",
                "int i = 0;",
                "int i = 1;"
            ),
            MetaTest.withStringReplacement(
                "method ignores last character",
                "i < board.length();",
                "i < board.length() - 1;"
            ),
            MetaTest.insertAt(
                "method does not accept characters other than X or O",
                10,
                "if (board.chars().anyMatch(c -> (char) c != 'X' && (char) c != 'O')) { throw new IllegalArgumentException(); }\n\n"
            ),
            MetaTest.withStringReplacement(
                "method does not accept more than 4 in a row",
                "return true;",
                "return (i == board.length() - 1 || board.charAt(i + 1) != player);"
            ),
            MetaTest.withStringReplacement(
                "method does not reset character count",
                """
                } else {
                    playerCharacters = 0;
                """,
                ""
            ),
            MetaTest.withLineReplacement(
                "method expects exactly one run of X/O (so both win)",
                9, 45,
                """
                public static GameResult calculateResult(String board) {
                    int xRuns = numberOfRuns(board, 'X');
                    int oRuns = numberOfRuns(board, 'O');

                    if (xRuns == 1 && oRuns == 1) {
                        throw new IllegalArgumentException();
                    }

                    if (xRuns >= 1) {
                        return GameResult.X_WON;
                    }

                    if (oRuns >= 1) {
                        return GameResult.O_WON;
                    }

                    return GameResult.DRAW;
                }

                private static int numberOfRuns(String board, char player) {
                    int playerCharacters = 0;
                    int result = 0;
                    for (int i = 0; i < board.length(); i++) {
                        if (board.charAt(i) == player) {
                            playerCharacters++;
                        } else {
                            if (playerCharacters >= 4) {
                                result += 1;
                            }
                            playerCharacters = 0;
                        }
                    }
                    if (playerCharacters >= 4) {
                        result += 1;
                    }
                    return result;
                }
                """
            ),
            MetaTest.withLineReplacement(
                "method expects exactly one run of X/O (so either wins)",
                9, 45,
                """
                public static GameResult calculateResult(String board) {
                    int xRuns = numberOfRuns(board, 'X');
                    int oRuns = numberOfRuns(board, 'O');

                    if (xRuns >= 1 && oRuns >= 1) {
                        throw new IllegalArgumentException();
                    }

                    if (xRuns == 1) {
                        return GameResult.X_WON;
                    }

                    if (oRuns == 1) {
                        return GameResult.O_WON;
                    }

                    return GameResult.DRAW;
                }

                private static int numberOfRuns(String board, char player) {
                    int playerCharacters = 0;
                    int result = 0;
                    for (int i = 0; i < board.length(); i++) {
                        if (board.charAt(i) == player) {
                            playerCharacters++;
                        } else {
                            if (playerCharacters >= 4) {
                                result += 1;
                            }
                            playerCharacters = 0;
                        }
                    }
                    if (playerCharacters >= 4) {
                        result += 1;
                    }
                    return result;
                }
                """
            )
        );
    }

}
