package nl.tudelft.cse1110.andy.writer.weblab;

import com.google.gson.Gson;
import nl.tudelft.cse1110.andy.execution.Context.Context;
import nl.tudelft.cse1110.andy.result.CompilationErrorInfo;
import nl.tudelft.cse1110.andy.result.Result;
import nl.tudelft.cse1110.andy.utils.FilesUtils;
import nl.tudelft.cse1110.andy.writer.standard.CodeSnippetGenerator;
import nl.tudelft.cse1110.andy.writer.standard.RandomAsciiArtGenerator;
import nl.tudelft.cse1110.andy.writer.standard.StandardResultWriter;
import nl.tudelft.cse1110.andy.writer.standard.VersionInformation;
import nl.tudelft.cse1110.andy.writer.weblab.EditorFeedbackRange.EditorFeedbackFile;
import nl.tudelft.cse1110.andy.writer.weblab.EditorFeedbackRangeUnderline.EditorFeedbackSeverity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

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
    }

    private void writeResultsXmlFile(Context ctx, Result result) {
        String xml = buildResultsXml(result);

        File resultsXml = new File(FilesUtils.concatenateDirectories(ctx.getDirectoryConfiguration().getOutputDir(), "results.xml"));
        FilesUtils.writeToFile(resultsXml, xml);
    }

    private String buildResultsXml(Result result) {
        int passedCount = result.getFinalGrade();
        int failedCount = 100 - passedCount;

        // Create the document structure
        Document doc;
        try {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }

        Element testSuitesElement = doc.createElement("testsuites");
        doc.appendChild(testSuitesElement);

        appendTestSuiteElement(passedCount, failedCount, doc, testSuitesElement);
        appendMetaScoreElements(result, doc, testSuitesElement);

        return buildXmlStringFromDocument(doc);
    }

    private static void appendTestSuiteElement(int passedCount, int failedCount, Document doc, Element testSuitesElement) {
        Element testSuiteElement = doc.createElement("testsuite");
        testSuitesElement.appendChild(testSuiteElement);

        Element passedElement = doc.createElement("testcase");
        passedElement.setAttribute("name", "Passed");
        passedElement.setAttribute("weight", String.valueOf(passedCount));

        Element failedElement = doc.createElement("testcase");
        failedElement.setAttribute("name", "Failed");
        failedElement.setAttribute("weight", String.valueOf(failedCount));
        failedElement.appendChild(doc.createElement("failure"));

        testSuiteElement.appendChild(passedElement);
        testSuiteElement.appendChild(failedElement);
    }

    private static void appendMetaScoreElements(Result result, Document doc, Element testSuitesElement) {
        Element metaElement = doc.createElement("meta");

        appendMetaScore(doc, metaElement, "Branch coverage", result.getCoverage().getCoveredBranches());
        appendMetaScore(doc, metaElement, "Mutation coverage", result.getMutationTesting().getKilledMutants());
        appendMetaScore(doc, metaElement, "Code checks", result.getCodeChecks().getNumberOfPassedChecks());
        appendMetaScore(doc, metaElement, "Meta tests", result.getMetaTests().getPassedMetaTests());

        result.getCodeChecks().getCheckResults().forEach(check -> appendMetaScore(doc, metaElement, check.getDescription(), check.passed() ? 1 : 0));
        result.getPenaltyCodeChecks().getCheckResults().forEach(check -> appendMetaScore(doc, metaElement, check.getDescription(), check.passed() ? 1 : 0));
        result.getMetaTests().getMetaTestResults().forEach(metaTest -> appendMetaScore(doc, metaElement, metaTest.getName(), metaTest.succeeded() ? 1 : 0));

        testSuitesElement.appendChild(metaElement);
    }

    private static void appendMetaScore(Document doc, Element metaElement, String id, int value) {
        Element scoreElement = doc.createElement("score");
        scoreElement.setAttribute("id", id);
        scoreElement.setTextContent(String.valueOf(value));
        metaElement.appendChild(scoreElement);
    }

    private static String buildXmlStringFromDocument(Document doc) {
        // Remove standalone="no" from the XML declaration
        doc.setXmlStandalone(true);

        // Create string
        try {
            StringWriter sw = new StringWriter();
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            return sw.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void writeEditorFeedbackJson(Context ctx, Result result) {

        List<EditorFeedbackRange> editorFeedbackRanges = buildEditorFeedback(result);
        String json = new Gson().toJson(editorFeedbackRanges);

        File editorFeedbackFile = new File(FilesUtils.concatenateDirectories(ctx.getDirectoryConfiguration().getOutputDir(),
                "editor-feedback.json"));
        FilesUtils.writeToFile(editorFeedbackFile, json);
    }

    private List<EditorFeedbackRange> buildEditorFeedback(Result result) {
        List<EditorFeedbackRange> editorFeedbackRanges = new ArrayList<>();

        // coverage
        editorFeedbackRanges.addAll(aggregateLinesIntoRanges(
                result.getCoverage().getFullyCoveredLines(),
                "100% coverage",
                EditorFeedbackFile.LIBRARY,
                EditorFeedbackRangeBackground.EditorFeedbackClassName.BACKGROUND_BLUE));

        editorFeedbackRanges.addAll(aggregateLinesIntoRanges(
                result.getCoverage().getPartiallyCoveredLines(),
                "Partial coverage",
                EditorFeedbackFile.LIBRARY,
                EditorFeedbackRangeBackground.EditorFeedbackClassName.BACKGROUND_YELLOW));

        editorFeedbackRanges.addAll(aggregateLinesIntoRanges(
                result.getCoverage().getNotCoveredLines(),
                "No coverage",
                EditorFeedbackFile.LIBRARY,
                EditorFeedbackRangeBackground.EditorFeedbackClassName.BACKGROUND_RED));

        // compilation error
        for (CompilationErrorInfo error : result.getCompilation().getErrors()) {
            editorFeedbackRanges.add(new EditorFeedbackRangeUnderline(
                    EditorFeedbackFile.SOLUTION,
                    error.getLineNumber(),
                    error.getLineNumber(),
                    error.getMessage(),
                    EditorFeedbackSeverity.ERROR));
        }

        return editorFeedbackRanges;
    }

    private List<EditorFeedbackRange> aggregateLinesIntoRanges(
            List<Integer> lines, String message, EditorFeedbackFile file,
            EditorFeedbackRangeBackground.EditorFeedbackClassName className) {
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
                ranges.add(new EditorFeedbackRangeBackground(file, currRangeStart, currLine, message, className));
                if (i != sortedLines.size() - 1) currRangeStart = sortedLines.get(i + 1);
            }
        }

        return ranges;
    }

}
