package nl.tudelft.cse1110.andy.result;

import java.util.Collections;
import java.util.List;

public class QualityResult {
    private int score; // between 0 and 1

    public QualityResult(int score) {
        // this.score = score;
        // dummy:
        this.score = 1;
    }

    public static QualityResult build(int score) {
        return new QualityResult(score);
    }

    public static QualityResult empty() {
        return new QualityResult(0);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "QualityResult{" +
                "score=" + score +
                '}';
    }
}
