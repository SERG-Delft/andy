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
        // dummy
        this.score = 1;
        return this.score;
    }
}
