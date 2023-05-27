package nl.tudelft.cse1110.andy.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import nl.tudelft.cse1110.andy.Andy;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.utils.FilesUtils;
import nl.tudelft.cse1110.andy.writer.weblab.WebLabResultWriter;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;

import static nl.tudelft.cse1110.andy.utils.FilesUtils.*;

public class AndyOnAWSLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public AWSResult run(AWSInput input) {

        Path workDir = createTemporaryDirectory("andy-workdir");
        Path outputDir = createTemporaryDirectory("andy-outputdir");

        writeToFile(workDir, "Library.java", input.getLibrary());
        writeToFile(workDir, "Configuration.java", input.getConfiguration());
        writeToFile(workDir, "Solution.java", input.getSolution());

        WebLabResultWriter writer = new WebLabResultWriter();
        new Andy(Action.valueOf(input.getAction()), workDir.toString(), outputDir.toString(), Arrays.asList(myself()), writer).run();

        String textOutput = readFile(outputDir, "stdout.txt");
        String weblab = readFile(outputDir, "results.xml");
        String highlights = readFile(outputDir, "editor-feedback.json");

        FilesUtils.deleteDirectory(workDir);
        FilesUtils.deleteDirectory(outputDir);

        return new AWSResult(textOutput, weblab, highlights);
    }

    private String myself() {
        String jarPath = AndyOnAWSLambda.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        try {
            jarPath = new File(jarPath).getAbsolutePath();
            jarPath = java.net.URLDecoder.decode(jarPath, "UTF-8");
            System.out.println(jarPath);
            return jarPath;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {

        Gson gson = new Gson();
        AWSInput awsInput = gson.fromJson(apiGatewayProxyRequestEvent.getBody(), AWSInput.class);

        AWSResult result = run(awsInput);

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody(gson.toJson(result));
    }
}
