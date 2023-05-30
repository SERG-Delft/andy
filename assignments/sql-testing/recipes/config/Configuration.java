package delft;

import nl.tudelft.cse1110.andy.config.MetaTest;
import nl.tudelft.cse1110.andy.config.RunConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration extends RunConfiguration {

  @Override
  public Map<String, Float> weights() {
	return new HashMap<>() {{
	  put("coverage", 0.0f);
	  put("mutation", 0.0f);
	  put("meta", 1.0f);
	  put("codechecks", 0.0f);
	}};
  }

  @Override
  public List<String> classesUnderTest() {
	return List.of("delft.RecipeDao");
  }

  @Override
  public List<MetaTest> metaTests() {
      return List.of(
              MetaTest.withStringReplacement("Does not check if recipe is vegan",
                      "\"select prepTime from recipe where vegan and dessert group by prepTime order by prepTime limit 1\"",
                      "\"select prepTime from recipe where dessert group by prepTime order by prepTime limit 1\""),
              MetaTest.withStringReplacement("Does not check if recipe is a dessert",
                      "\"select prepTime from recipe where vegan and dessert group by prepTime order by prepTime limit 1\"",
                      "\"select prepTime from recipe where vegan group by prepTime order by prepTime limit 1\""),
              MetaTest.withStringReplacement("Replaced and with or",
                      "\"select prepTime from recipe where vegan and dessert group by prepTime order by prepTime limit 1\"",
                      "\"select prepTime from recipe where vegan or dessert group by prepTime order by prepTime limit 1\""),
              MetaTest.withStringReplacement("Negated first boolean",
                      "\"select prepTime from recipe where vegan and dessert group by prepTime order by prepTime limit 1\"",
                      "\"select prepTime from recipe where not vegan and dessert group by prepTime order by prepTime limit 1\""),
              MetaTest.withStringReplacement("Negated second boolean",
                      "\"select prepTime from recipe where vegan and dessert group by prepTime order by prepTime limit 1\"",
                      "\"select prepTime from recipe where vegan and not dessert group by prepTime order by prepTime limit 1\""),
              MetaTest.withStringReplacement("Negated both of the booleans",
                      "\"select prepTime from recipe where vegan and dessert group by prepTime order by prepTime limit 1\"",
                      "\"select prepTime from recipe where not vegan and not dessert group by prepTime order by prepTime limit 1\""),
              MetaTest.withStringReplacement("Negated the entire where",
                      "\"select prepTime from recipe where vegan and dessert group by prepTime order by prepTime limit 1\"",
                      "\"select prepTime from recipe where not vegan or not dessert group by prepTime order by prepTime limit 1\""),
              MetaTest.withStringReplacement("Got the most time-consuming recipe",
                      "\"select prepTime from recipe where vegan and dessert group by prepTime order by prepTime limit 1\"",
                      "\"select prepTime from recipe where not vegan or not dessert group by prepTime order by prepTime desc limit 1\"")
      );
  }

    @Override
    public boolean skipJacoco() {
        return true;
    }

    @Override
    public boolean skipPitest() {
        return true;
    }

}