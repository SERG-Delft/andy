package nl.tudelft.cse1110.andy.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import nl.tudelft.cse1110.andy.Andy;
import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.execution.Context.ContextBuilder;
import nl.tudelft.cse1110.andy.execution.Context.ContextDirector;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.result.Result;
import nl.tudelft.cse1110.andy.utils.FilesUtils;
import nl.tudelft.cse1110.andy.writer.ResultWriter;
import nl.tudelft.cse1110.andy.writer.standard.StandardResultWriter;

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

        ContextDirector director = new ContextDirector(new ContextBuilder());
        nl.tudelft.cse1110.andy.execution.Context.Context ctx = director.constructWithLibraries(
                Action.valueOf(input.getAction()),
                new DirectoryConfiguration(workDir.toString(), outputDir.toString()),
                Arrays.asList(myself())
        );

        Result result = new Andy().run(ctx);

        ResultWriter writer = new StandardResultWriter();
        writer.write(ctx, result);

        String textOutput = FilesUtils.readFile(outputDir, "stdout.txt");

        deleteDirectory(workDir);
        deleteDirectory(outputDir);

        return new AWSResult(textOutput, result);
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
