package delft;

class ConnectFour1D {

    private ConnectFour1D() {
        // Empty constructor
    }

    public static GameResult calculateResult(String board) {
        // Check if X won
        boolean xWon = checkWin(board, 'X');

        // Check if O won
        boolean oWon = checkWin(board, 'O');

        // Check if both have 4 in a row (this should not happen)
        if (xWon && oWon) {
            throw new IllegalArgumentException();
        }

        if (xWon) {
            return GameResult.X_WON;
        }

        if (oWon) {
            return GameResult.O_WON;
        }

        return GameResult.DRAW;
    }

    private static boolean checkWin(String board, char player) {
        int playerCharacters = 0;
        for (int i = 0; i < board.length(); i++) {
            if (board.charAt(i) == player) {
                playerCharacters++;
                if (playerCharacters == 4) {
                    return true;
                }
            } else {
                playerCharacters = 0;
            }
        }
        return false;
    }

}

enum GameResult {
    X_WON,
    O_WON,
    DRAW
}
