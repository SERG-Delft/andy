package nl.tudelft.cse1110.codechecker.engine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class ScriptParser {

    public CheckScript parse(String configAsString) {
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            CheckScript cfg = mapper.readValue(configAsString, CheckScript.class);

            // TODO: assert that configuration is valid

            return cfg;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
