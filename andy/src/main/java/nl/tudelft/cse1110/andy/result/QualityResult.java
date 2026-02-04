package nl.tudelft.cse1110.andy.result;

import nl.tudelft.cse1110.andy.execution.metatest.MetaTestReport;

import java.util.*;

public class QualityResult {
    private int score; // between 0 and 1
    private int numUnitTests;
    private List<String> unitTests;

    private LinkedList<MetaTestReport> metaTestReports;
    private Map<String, Set<String>> testToMetaTests; // a useful mapping from the test cases to the meta-tests they cover

    private Map<String, Set<Integer>> coveragePerTest;
    private Map<String, Set<Integer>> mutationsKilledPerTest;

    public QualityResult(int numUnitTests) {
        // dummy:
        this.score = 0;
        this.numUnitTests = numUnitTests;
        metaTestReports  = new LinkedList<>();
        testToMetaTests  = new HashMap<>();
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

    public Map<String, Set<Integer>> getMutationsKilledPerTest() {
        return mutationsKilledPerTest;
    }

    public void setMutationsKilledPerTest(Map<String, Set<Integer>> mutationsKilledPerTest) {
    }

    @Override
    public String toString() {
        return "QualityResult{" +
                "score=" + score +
                '}';
    }

    public void considerMetaTest(MetaTestReport metaTestReport) {
        this.metaTestReports.addFirst(metaTestReport);

        for (TestFailureInfo failure : metaTestReport.getTestsTriggered()) {
            String test = failure.getTestCase();
            if (testToMetaTests.get(test) == null) {
                testToMetaTests.put(test, new HashSet<>());
            }
            testToMetaTests.get(test).add(metaTestReport.getName());
        }
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
        return testToMetaTests.values().stream().filter(nt -> nt.size() == 1).count();
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

    /**
     * Count the number of tests that increase one of:
     * 1) number of meta tests triggered
     * 2) lines covered
     * 3) mutations killed
     * @return the number of such tests
     */
    private long countMeaningfulTests() {

        Set<String> meaningfulTests = new HashSet<>();

        meaningfulTests.addAll(meaningfulCoverage(testToMetaTests)); // 1)

        meaningfulTests.addAll(meaningfulCoverage(coveragePerTest)); // 2)

        meaningfulTests.addAll(meaningfulCoverage(mutationsKilledPerTest)); // 3)

        return meaningfulTests.size();
    }

    private <T> Set<String> meaningfulCoverage(Map<String, Set<T>> map) {

        Set<String> meaningfulTests = new HashSet<>();

        for (String test : map.keySet()) {

            Set<T> meaningfulLines = new HashSet<>(map.get(test));

            for (String otherTest : map.keySet()) {
                if (test.equals(otherTest)) continue;
                Set<T> linesWithOtherTest = map.get(otherTest);
                meaningfulLines.removeAll(linesWithOtherTest);
            }

            if (!meaningfulLines.isEmpty() && !meaningfulTests.contains(test)) {
                meaningfulTests.add(test);
            }
        }

        return meaningfulTests;
    }
}
