package unit.writer.weblab;

import nl.tudelft.cse1110.andy.result.CompilationErrorInfo;
import nl.tudelft.cse1110.andy.result.CoverageLineByLine;
import nl.tudelft.cse1110.andy.result.CoverageResult;
import nl.tudelft.cse1110.andy.result.Result;
import nl.tudelft.cse1110.andy.writer.ResultWriter;
import nl.tudelft.cse1110.andy.writer.weblab.WebLabResultWriter;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import testutils.ResultTestDataBuilder;
import unit.writer.standard.StandardResultWriterTest;

import java.io.File;
import java.util.List;

import static nl.tudelft.cse1110.andy.utils.FilesUtils.concatenateDirectories;
import static nl.tudelft.cse1110.andy.utils.FilesUtils.readFile;
import static org.assertj.core.api.Assertions.allOf;
import static org.assertj.core.api.Assertions.assertThat;
import static unit.writer.standard.StandardResultTestAssertions.finalGradeInXml;
import static unit.writer.weblab.WebLabEditorFeedbackJsonTestAssertions.*;

public class WebLabResultWriterTest extends StandardResultWriterTest {

    @Override
    protected ResultWriter buildWriter() {
        return new WebLabResultWriter(versionInformation, asciiArtGenerator, codeSnippetGenerator);
    }

    private String editorFeedbackJson() {
        return readFile(new File(concatenateDirectories(reportDir.toString(), "editor-feedback.json")));
    }

    @Test
    void reportCompilationErrorWithJson() {
        Result result = new ResultTestDataBuilder().withCompilationFail(
                new CompilationErrorInfo("Library.java", 10, "some compilation error"),
                new CompilationErrorInfo("Library.java", 11, "some other compilation error")
        ).build();

        writer.write(ctx, result);

        String editorFeedbackJson = editorFeedbackJson();

        assertThat(editorFeedbackJson)
                .has(editorFeedbackCompilationError(10, "some compilation error"))
                .has(editorFeedbackCompilationError(11, "some other compilation error"));
    }

    @Test
    void testLineCoverageInEditorFeedback() {
        Result result = new ResultTestDataBuilder()
                .withCoverageResult(CoverageResult.build(
                        5, 7, 5, 8, 1, 2,
                        new CoverageLineByLine(
                                List.of(1, 2, 3, 6, 7),
                                List.of(4),
                                List.of(5)
                        )
                ))
                .build();

        writer.write(ctx, result);

        String editorFeedbackJson = editorFeedbackJson();

        assertThat(editorFeedbackJson)
                .has(editorFeedbackFullyCovered(1, 3))
                .has(editorFeedbackFullyCovered(6, 7))
                .has(editorFeedbackPartiallyCovered(4, 4))
                .has(editorFeedbackNotCovered(5, 5));
    }

    @Test
    void testEditorFeedbackCoverageBoundaries() {
        Result result = new ResultTestDataBuilder()
                .withCoverageResult(CoverageResult.build(
                        8, 13, 0, 0, 0, 0,
                        new CoverageLineByLine(
                                List.of(4, 7, 13, 9, 3, 1, 10, 5),
                                List.of(),
                                List.of(2, 6, 8, 11, 12)
                        )
                ))
                .build();

        writer.write(ctx, result);

        String editorFeedbackJson = editorFeedbackJson();

        assertThat(editorFeedbackJson)
                .has(editorFeedbackFullyCovered(1, 1))
                .has(editorFeedbackFullyCovered(3, 5))
                .has(editorFeedbackFullyCovered(7, 7))
                .has(editorFeedbackFullyCovered(9, 10))
                .has(editorFeedbackFullyCovered(13, 13))
                .has(editorFeedbackNotCovered(2, 2))
                .has(editorFeedbackNotCovered(6, 6))
                .has(editorFeedbackNotCovered(8, 8))
                .has(editorFeedbackNotCovered(11, 12));
    }

    @Override
    protected Condition<? super String> finalGradeOnScreen(int grade) {

        // grade is correct
        Condition<? super String> correctFinalGrade = super.finalGradeOnScreen(grade);

        // result xml contains the correct score
        Condition<String> xmlIsCorrect = finalGradeInXml(reportDir.toString(), grade);

        return allOf(correctFinalGrade, xmlIsCorrect);
    }

    protected Condition<? super String> finalGradeNotOnScreen(int grade) {
        // result xml contains the correct score
        Condition<String> xmlIsCorrect = finalGradeInXml(reportDir.toString(), grade);

        return xmlIsCorrect;
    }
}
