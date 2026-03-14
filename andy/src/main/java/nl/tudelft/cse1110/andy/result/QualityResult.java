package nl.tudelft.cse1110.andy.result;

import nl.tudelft.cse1110.andy.execution.metatest.MetaTestReport;

import java.util.*;
import java.util.stream.Collectors;

public class QualityResult {
    private int score; // between 0 and 1
    private int numUnitTests;
    private Map<String, String> unitTests; // uniqueId (testId below) -> displayName

    private LinkedList<MetaTestReport> metaTestReports;
    private Map<String, Set<String>> testToMetaTests; // a useful mapping from the test cases to the meta-tests they cover

    private Map<String, Set<Integer>> coveragePerTest; // testId -> linesCovered
    private Map<String, Set<Integer>> mutationsKilledPerTest; // testId -> mutationId

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

    public Map<String, String> getUnitTests() {
        return unitTests;
    }

    public void setUnitTests(Map<String, String> unitTests) {
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
        this.mutationsKilledPerTest = mutationsKilledPerTest;
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

            String testName = metaTestReport.getName();

            if (testName.matches(".* \\(\\d+\\)")) {
                String method = testName.replaceAll(" \\(\\d+\\)$", "").trim();
                String invocation = testName.replaceAll(".* \\((\\d+)\\)$", "#$1").trim();
                testName = method + " " + invocation;
            }
            testToMetaTests.get(test).add(testName);
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

    @SuppressWarnings("checkstyle:DeclarationOrder")
    private Map<String, Set<String>> nonisolatedTests = new HashMap<>();

    /**
     * Count the number of tests that do not trigger meta-tests already covered by other tests
     * @return the number of such tests
     */
    public long countIsolatedTests() {

        for (MetaTestReport metaTestReport : metaTestReports) {
            if (metaTestReport.getTestsTriggered().size() == 1) continue;

            // All tests that trigger this meta-test collide with each other
            List<String> collidingTests = metaTestReport.getTestsTriggered().stream()
                    .map(TestFailureInfo::getTestCase)
                    .toList();

            for (String test : collidingTests) {
                nonisolatedTests.computeIfAbsent(test, t -> {
                    return new HashSet<>();
                        })
                        .addAll(collidingTests.stream()
                                .filter(other -> !other.equals(test))
                                .collect(Collectors.toSet()));
            }
        }

        int count = unitTests.size();

        for (Set<String> collisions : nonisolatedTests.values()) {
            if (!collisions.isEmpty()) count--;
        }

        return count;
    }

    @SuppressWarnings("checkstyle:DeclarationOrder")
    Map<String, List<Integer>> contributingTests = new HashMap<>();

    /**
     * Count the number of tests that increase one of:
     * 1) number of meta tests triggered
     * 2) lines covered
     * 3) mutations killed
     * @return the number of such tests
     */
    public long countContributingTests() {

        // 1)
        Set<String> contributingMetaTests = contribution(testToMetaTests);
        for (String test : contributingMetaTests) {
            contributingTests.computeIfAbsent(test, t -> new ArrayList<>());
            contributingTests.get(test).add(1);
        }

        // 2)
        Set<String> contributingCoverage = contribution(coveragePerTest);
        for (String test : contributingCoverage) {
            contributingTests.computeIfAbsent(test, t -> new ArrayList<>());
            contributingTests.get(test).add(2);
        }

        // 3)
        Set<String> contributingMutation = contribution(mutationsKilledPerTest);
        for (String test : contributingMutation) {
            contributingTests.computeIfAbsent(test, t -> new ArrayList<>());
            contributingTests.get(test).add(3);
        }

        return contributingTests.size();
    }

    private <T> Set<String> contribution(Map<String, Set<T>> map) {
        Set<String> contributingTests = new HashSet<>();
        for (String test : map.keySet()) {
            if (!map.get(test).isEmpty()) {
                contributingTests.add(test);
            }
        }
        return contributingTests;
    }

    /**
     * Used to get an overview of cohesive tests in the output
     * @return a list of cohesive and non-cohesive tests
     */
    public String listCohesiveTests() {
        StringBuilder sb = new StringBuilder("Tests that only cover a single meta-test: \n");

        for (String testName : testToMetaTests.keySet()) {
            if (testToMetaTests.get(testName) == null ||
                    testToMetaTests.get(testName).size() != 1) {
                sb.append("  > " + testName + " ✕\n");
            } else {
                sb.append("  > " + testName + " ✓\n");
            }
        }

        return sb.toString();
    }

    /**
     * Used to get an overview of isolated tests in the output
     * @return a list of isolated and non-isolated tests
     */
    public String listIsolatedTests() {

        StringBuilder sb = new StringBuilder("Tests that do not trigger meta-tests already covered by other tests: \n");

        for (String testName : unitTests.values()) {
            if (nonisolatedTests.containsKey(testName)) {
                sb.append("  > " + testName + " ✕ - ");
                Set<String> collisions =  nonisolatedTests.get(testName);
                for (String collision : collisions) {
                    sb.append(collision + "; ");
                }
                sb.append("\n");
            } else {
                sb.append("  > " + testName + " ✓\n");
            }
        }

        return sb.toString();
    }

    /**
     * Used to get an overview of contributing tests in the output
     * @return a list of contributing and non-contributing tests
     */
    public String listContributingTests() {

        StringBuilder sb = new StringBuilder("Tests that increase a metric: \n");

        for (String testName : unitTests.values()) {
            if (contributingTests.containsKey(testName)) {
                sb.append("  > " + testName + " ✓ - ");
                List<Integer> contributions =  contributingTests.get(testName);
                for (int contribution : contributions) {
                    switch (contribution) {
                        case 1:
                            sb.append("meta-tests; ");
                            break;
                        case 2:
                            sb.append("coverage; ");
                            break;
                        case 3:
                            sb.append("mutation; ");
                            break;
                        default:
                    }
                }
                sb.append("\n");
            } else {
                sb.append("  > " + testName + " ✕\n");
            }
        }

        return sb.toString();
    }
}
