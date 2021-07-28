package nl.tudelft.cse1110.grader.execution.step;

import nl.tudelft.cse1110.grader.util.ClassUtils;
import nl.tudelft.cse1110.grader.config.Configuration;
import nl.tudelft.cse1110.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.grader.result.ResultBuilder;
import nl.tudelft.cse1110.grader.execution.AdditionalReportJUnitListener;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static nl.tudelft.cse1110.grader.util.FileUtils.filePathsAsString;
import static nl.tudelft.cse1110.grader.util.FileUtils.getAllJavaFiles;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public class RunJUnitTests implements ExecutionStep {
    @Override
    public void execute(Configuration cfg, ResultBuilder result) {
        try {

            SummaryGeneratingListener listener = new SummaryGeneratingListener();
            AdditionalReportJUnitListener additionalReportJUnitListener = new AdditionalReportJUnitListener(result);

            String testClass = ClassUtils.getTestClass(cfg.getNewClassNames());

            Launcher launcher = LauncherFactory.create();
            launcher.registerTestExecutionListeners(listener);
            launcher.registerTestExecutionListeners(additionalReportJUnitListener);

            LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                    .selectors(selectClass(testClass))
                    .configurationParameter("jqwik.reporting.usejunitplatform", "true")
                    .build();
            launcher.execute(request);

            TestExecutionSummary summary = listener.getSummary();

            if (!testsCheck(cfg) || summary.getTestsFoundCount() == 0) {
                result.noTestsFound();
            } else {
                result.logJUnitRun(summary);
            }
        } catch (Exception e) {
            result.genericFailure(this, e);
        }

    }


    /**
     * This method checks whether @.*Test can be found at least once in the student's solution.
     * If not, it means they have not written any tests.
     *
     * @param cfg the default configuration of the directories
     * @return false if no tests found, true if test(s) found
     * @throws IOException throws an IOException
     */
    public boolean testsCheck(Configuration cfg) throws IOException {
        List<String> listOfFiles = filePathsAsString(getAllJavaFiles(cfg.getWorkingDir()));
        int count = 0;

        for(String pathOfJavaClass : listOfFiles) {
            String content = new String(Files.readAllBytes(Paths.get(pathOfJavaClass)));

            String regex = "@.*Test";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(content);

            if (matcher.find()) {
                count++;
            }
        }

        if (count == 0) {
            return false;
        }
        return true;
    }

}
