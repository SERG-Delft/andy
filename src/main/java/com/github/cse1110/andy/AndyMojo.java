package com.github.cse1110.andy;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import nl.tudelft.cse1110.andy.Andy;

@Mojo(name = "andy")
public class AndyMojo extends AbstractMojo {

    @Parameter(property = "exercise", required = true)
    private String exercise;

    @Override
    public void execute() {
        try {
            Andy.main(new String[] { "-e", exercise });
        } catch (Exception ignored) {}
    }

}
