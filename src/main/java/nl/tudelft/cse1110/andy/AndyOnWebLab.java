package nl.tudelft.cse1110.andy;

import nl.tudelft.cse1110.andy.writer.standard.RandomAsciiArtGenerator;
import nl.tudelft.cse1110.andy.writer.weblab.WebLabResultWriter;

public class AndyOnWebLab {

    public static void main(String[] args) {
        String action = args[0];
        String workDir = args[1];
        String outputDir = args[2];

        if (action == null) { System.out.println("No ACTION environment variable.");      System.exit(-1); }
        if (workDir == null) { System.out.println("No WORKING_DIR environment variable."); System.exit(-1); }
        if (outputDir == null) { System.out.println("No OUTPUT_DIR environment variable.");  System.exit(-1); }

        WebLabResultWriter writer = new WebLabResultWriter(new RandomAsciiArtGenerator());
        new Andy(action, workDir, outputDir, writer).run();
    }

}
