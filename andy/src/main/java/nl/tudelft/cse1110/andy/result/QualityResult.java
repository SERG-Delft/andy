package nl.tudelft.cse1110.andy.result;

import nl.tudelft.cse1110.andy.execution.metatest.MetaTestReport;
import org.junit.platform.launcher.TestIdentifier;

import java.util.*;

public class QualityResult {
    private int score; // between 0 and 1
    private int numUnitTests;
    private List<String> unitTests;
    private LinkedList<MetaTestReport> metaTestReports;
    private Map<String, Set<Integer>> coveragePerTest;

    public QualityResult(int numUnitTests) {
        // dummy:
        this.score = 0;
        this.numUnitTests = numUnitTests;
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

    public int getNumUnitTests() {
        return numUnitTests;
    }

    public void setNumUnitTests(int numUnitTests) {
        this.numUnitTests = numUnitTests;
    }

    public List<String> getUnitTests() {
        return unitTests;
    }

    public void setUnitTests(List<String> unitTests) {
        this.unitTests = unitTests;
    }

    public Map<String, Set<Integer>> getCoveragePerTest() {
        return coveragePerTest;
    }

    public void setCoveragePerTest(Map<String, Set<Integer>> coveragePerTest) {
        this.coveragePerTest = coveragePerTest;
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
        // dummy:
        this.score = 1;
        return this.score;
    }

    public long countTests() {
        return numUnitTests;
    }

    /**
     * Get how many tests cover a single meta-test
     * @return the number of such tests
     */
    public long countCohesiveTests() {

        Map<String, Integer> numTriggers = new HashMap<>(); // list with tests that were triggered once
        unitTests.forEach(ut -> numTriggers.put(ut, 0));

        for (MetaTestReport metaTestReport : metaTestReports) {
            for (TestFailureInfo testFailureInfo : metaTestReport.getTestsTriggered()) {
                String testName = testFailureInfo.getTestCase();
                numTriggers.put(testName, numTriggers.get(testName) + 1);
            }
        }

        return numTriggers.values().stream().filter(nt -> nt == 1).count();
    }

    /**
     * Count the number of tests that do not trigger meta-tests already covered by other tests
     * @return the number of such tests
     */
    public long countIsolatedTests() {

        long count = countTests();

        Set<String> unisolatedTests = new HashSet<>();

        for (MetaTestReport metaTestReport : metaTestReports) {
            if (metaTestReport.getTestsTriggered().size() == 1) continue;
            for (TestFailureInfo testFailureInfo : metaTestReport.getTestsTriggered()) {
                String testName = testFailureInfo.getTestCase();
                if (!unisolatedTests.contains(testName)) {
                    unisolatedTests.add(testName);
                    count--;
                }
            }
        }

        return count;
    }
}
