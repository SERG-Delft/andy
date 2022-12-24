package nl.tudelft.cse1110.andy.writer.weblab;

import com.google.gson.Gson;
import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.result.CompilationErrorInfo;
import nl.tudelft.cse1110.andy.result.Result;
import nl.tudelft.cse1110.andy.writer.standard.CodeSnippetGenerator;
import nl.tudelft.cse1110.andy.writer.standard.RandomAsciiArtGenerator;
import nl.tudelft.cse1110.andy.writer.standard.StandardResultWriter;
import nl.tudelft.cse1110.andy.writer.standard.VersionInformation;
import nl.tudelft.cse1110.andy.writer.weblab.EditorFeedbackRange.EditorFeedbackLocation;
import nl.tudelft.cse1110.andy.writer.weblab.EditorFeedbackRange.EditorFeedbackSeverity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static nl.tudelft.cse1110.andy.utils.FilesUtils.concatenateDirectories;
import static nl.tudelft.cse1110.andy.utils.FilesUtils.writeToFile;

public class WebLabResultWriter extends StandardResultWriter {

    public WebLabResultWriter(VersionInformation versionInformation, RandomAsciiArtGenerator asciiArtGenerator,
                              CodeSnippetGenerator codeSnippetGenerator) {
        super(versionInformation, asciiArtGenerator, codeSnippetGenerator);
    }

    public WebLabResultWriter() {
        super();
    }

    @Override
    public void write(Context ctx, Result result) {

        super.write(ctx, result);

        writeResultsXmlFile(ctx, result);
        writeEditorFeedbackJson(ctx, result);
        writeAnalyticsFile(ctx, result);
    }

    private void writeResultsXmlFile(Context ctx, Result result) {
        String xml = buildResultsXml(result);

        File resultsXml = new File(concatenateDirectories(ctx.getDirectoryConfiguration().getOutputDir(), "results.xml"));
        writeToFile(resultsXml, xml);
    }

    private String buildResultsXml(Result result) {
        StringBuilder xml = new StringBuilder();

        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<testsuites>\n\t<testsuite>\n");
        int score = result.getFinalGrade();
        String failed = "\t\t<testcase><failure></failure></testcase>\n";
        String passed = "\t\t<testcase/>\n";

        xml.append(failed.repeat(100 - score));
        xml.append(passed.repeat(score));

        xml.append("\t</testsuite>\n</testsuites>\n");
        return xml.toString();
    }


    private void writeEditorFeedbackJson(Context ctx, Result result) {

        List<EditorFeedbackRange> editorFeedbackRanges = buildEditorFeedback(result);
        String json = new Gson().toJson(editorFeedbackRanges);

        File editorFeedbackFile = new File(concatenateDirectories(ctx.getDirectoryConfiguration().getOutputDir(),
                "editor-feedback.json"));
        writeToFile(editorFeedbackFile, json);
    }

    private List<EditorFeedbackRange> buildEditorFeedback(Result result) {
        List<EditorFeedbackRange> editorFeedbackRanges = new ArrayList<>();

        // coverage
        editorFeedbackRanges.addAll(aggregateLinesIntoRanges(
                result.getCoverage().getFullyCoveredLines(),
                "100% coverage",
                EditorFeedbackLocation.LIBRARY,
                EditorFeedbackSeverity.INFO));

        editorFeedbackRanges.addAll(aggregateLinesIntoRanges(
                result.getCoverage().getPartiallyCoveredLines(),
                "Partial coverage",
                EditorFeedbackLocation.LIBRARY,
                EditorFeedbackSeverity.HINT));

        editorFeedbackRanges.addAll(aggregateLinesIntoRanges(
                result.getCoverage().getNotCoveredLines(),
                "No coverage",
                EditorFeedbackLocation.LIBRARY,
                EditorFeedbackSeverity.WARNING));

        // compilation error
        for (CompilationErrorInfo error : result.getCompilation().getErrors()) {
            editorFeedbackRanges.add(new EditorFeedbackRange(
                    EditorFeedbackLocation.SOLUTION,
                    error.getLineNumber(),
                    error.getLineNumber(),
                    EditorFeedbackSeverity.ERROR,
                    error.getMessage()));
        }

        return editorFeedbackRanges;
    }

    private List<EditorFeedbackRange> aggregateLinesIntoRanges(
            List<Integer> lines, String message, EditorFeedbackLocation location, EditorFeedbackSeverity severity) {
        // Convert list of line numbers, e.g. [2,4,8,3,10,7]
        // into a list of ranges, e.g. [(2,4),(7,8),(10,10)]

        List<EditorFeedbackRange> ranges = new ArrayList<>();
        if (lines.isEmpty()) return ranges;

        List<Integer> sortedLines = new ArrayList<>(lines);
        sortedLines.sort(Integer::compareTo);

        int currRangeStart = sortedLines.get(0);
        for (int i = 0; i < sortedLines.size(); i++) {
            int currLine = sortedLines.get(i);
            if (i == sortedLines.size() - 1 || currLine + 1 != sortedLines.get(i + 1)) {
                ranges.add(new EditorFeedbackRange(location, currRangeStart, currLine, severity, message));
                if (i != sortedLines.size() - 1) currRangeStart = sortedLines.get(i + 1);
            }
        }

        return ranges;
    }


    private void writeAnalyticsFile(Context ctx, Result result) {
        if(ctx.getModeActionSelector()==null || !ctx.getModeActionSelector().shouldGenerateAnalytics())
            return;

        Submission submission = new Submission(
                ctx.getAction(),
                ctx.getSubmissionMetaData(),
                result
        );

        String json = new Gson().toJson(submission);

        File file = new File(concatenateDirectories(ctx.getDirectoryConfiguration().getOutputDir(), "post.json"));
        writeToFile(file, json);
    }
}
