package delft;

import java.util.List;

enum SignalType {
    ALPHA,
    BETA,
    GAMMA
}

class SpaceSignals {

    public static final List<SignalType> pattern = List.of(
            SignalType.ALPHA, 
            SignalType.GAMMA, 
            SignalType.BETA, 
            SignalType.GAMMA, 
            SignalType.BETA
    );

    private SpaceSignals() {
        // Empty constructor
    }

    /**
     * This method detects relevant signals from space.
     * A signal is relevant if the sequence "ALPHA, GAMMA, BETA, GAMMA, BETA" is found at least 3 times.
     * <p>
     * If the signal is relevant, the number of occurrences of the pattern is returned.
     * If the signal is not relevant, -1 is returned.
     *
     * @param signals the list of signals
     * @return the number of times the sequence "ALPHA, GAMMA, BETA, GAMMA, BETA" is found,
     * if it is found at least 3 times, otherwise -1.
     */
    public static int numberOfOccurrences(List<SignalType> signals) {
        int sequenceCount = 0;

        for (int i = 0; i <= signals.size() - pattern.size(); i++) {
            boolean foundPattern = true;

            for (int j = 0; j < pattern.size(); j++) {
                if (signals.get(i + j) != pattern.get(j)) {
                    foundPattern = false;
                    break;
                }
            }

            if (foundPattern) {
                sequenceCount++;
            }
        }

        return sequenceCount >= 3 ? sequenceCount : -1;
    }
}
