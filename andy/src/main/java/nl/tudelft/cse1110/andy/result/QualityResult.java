package nl.tudelft.cse1110.andy.result;

import nl.tudelft.cse1110.andy.execution.metatest.MetaTestReport;

import java.util.LinkedList;

public class QualityResult {
    private int score; // between 0 and 1
    private LinkedList<MetaTestReport> metaTestReports;

    public QualityResult(int score) {
        // this.score = score;
        // dummy:
        this.score = 1;
        metaTestReports  = new LinkedList<>();
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

    public LinkedList<MetaTestReport> getMetaTestReports() {
        return metaTestReports;
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

    public void considerMetaTest(MetaTestReport metaTestReport) {
        this.metaTestReports.addFirst(metaTestReport);
    }

    public int computeScore() {

        int limitedNumberMetaTestsScore = this.computeLimitedNumberMetaTestsScore();

        this.score = limitedNumberMetaTestsScore;

        return limitedNumberMetaTestsScore;
    }

    /**
     * Measures how well tests are isolated.
     * Lower scores indicate that individual tests trigger many meta-tests.
     */
    private int computeLimitedNumberMetaTestsScore() {
        int tolerance = 2;
        int penaltyPerExtraTest = 1;

        double score = 100;
        for (MetaTestReport r : metaTestReports) {
            int triggered = r.getTestsRan() - r.getTestsSucceeded();
            int excess = Math.max(0, triggered - tolerance);
            score -= excess * penaltyPerExtraTest; // alternatives: quadratic or logarithmic penalties
        }

        return (int) Math.max(0, score);
    }
}
