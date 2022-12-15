package nl.tudelft.cse1110.andy.writer.weblab;

import com.google.gson.Gson;
import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.result.CompilationErrorInfo;
import nl.tudelft.cse1110.andy.result.Result;
import nl.tudelft.cse1110.andy.writer.standard.CodeSnippetGenerator;
import nl.tudelft.cse1110.andy.writer.standard.RandomAsciiArtGenerator;
import nl.tudelft.cse1110.andy.writer.standard.StandardResultWriter;
import nl.tudelft.cse1110.andy.writer.standard.VersionInformation;
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
        writeHighlightsJson(ctx, result);
        writeAnalyticsFile(ctx, result);
    }

    private void writeResultsXmlFile(Context ctx, Result result) {
        String xml = buildResultsXml(result);

        File resultsXml = new File(concatenateDirectories(ctx.getDirectoryConfiguration().getOutputDir(), "results.xml"));
        writeToFile(resultsXml, xml);
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
        Element testSuiteElement = doc.createElement("testsuite");
        testSuitesElement.appendChild(testSuiteElement);

        // Create passed and failed elements
        Element passedElement = doc.createElement("testcase");
        passedElement.setAttribute("name", "Passed");
        passedElement.setAttribute("weight", String.valueOf(passedCount));

        Element failedElement = doc.createElement("testcase");
        failedElement.setAttribute("name", "Failed");
        failedElement.setAttribute("weight", String.valueOf(failedCount));
        failedElement.appendChild(doc.createElement("failed"));

        testSuiteElement.appendChild(passedElement);
        testSuiteElement.appendChild(failedElement);

        return buildXmlStringFromDocument(doc);
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


    private void writeHighlightsJson(Context ctx, Result result){

        List<Highlight> highlights = buildHighlights(result);
        String json = new Gson().toJson(highlights);

        File highlightsJson = new File(concatenateDirectories(ctx.getDirectoryConfiguration().getOutputDir(), "highlights.json"));
        writeToFile(highlightsJson, json);
    }

    private List<Highlight> buildHighlights(Result result) {
        List<Highlight> highlights = new ArrayList<>();

        // coverage
        for(int line : result.getCoverage().getFullyCoveredLines()) {
            highlights.add(new Highlight(line, "100% coverage",
                    Highlight.HighlightLocation.LIBRARY, Highlight.HighlightPurpose.FULL_COVERAGE));
        }

        for(int line : result.getCoverage().getPartiallyCoveredLines()) {
            highlights.add(new Highlight(line, "Partial coverage",
                    Highlight.HighlightLocation.LIBRARY, Highlight.HighlightPurpose.PARTIAL_COVERAGE));
        }

        for(int line : result.getCoverage().getNotCoveredLines()) {
            highlights.add(new Highlight(line, "No coverage",
                    Highlight.HighlightLocation.LIBRARY, Highlight.HighlightPurpose.NO_COVERAGE));
        }

        // compilation error
        for (CompilationErrorInfo error : result.getCompilation().getErrors()) {
            highlights.add(new Highlight(error.getLineNumber(), error.getMessage(), Highlight.HighlightLocation.SOLUTION, Highlight.HighlightPurpose.COMPILATION_ERROR));
        }

        return highlights;
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
