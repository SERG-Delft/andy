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
	return List.of("delft.RestaurantDao");
  }

  @Override
  public List<MetaTest> metaTests() {
	return List.of(
		MetaTest.withStringReplacement("Change first boundary",
			"\"select name from restaurant where rating > 6.5 and rating <= 8 group by name order by name\"",
			"\"select name from restaurant where rating >= 6.5 and rating <= 8 group by name order by name\""
		),
		MetaTest.withStringReplacement("Change second boundary",
			"\"select name from restaurant where rating > 6.5 and rating <= 8 group by name order by name\"",
			"\"select name from restaurant where rating > 6.5 and rating < 8 group by name order by name\""
		),
		MetaTest.withStringReplacement("Change boundaries",
			"\"select name from restaurant where rating > 6.5 and rating <= 8 group by name order by name\"",
			"\"select name from restaurant where rating >= 6.5 and rating < 8 group by name order by name\""
		),
		MetaTest.withStringReplacement("Change boundaries",
			"\"select name from restaurant where rating > 6.5 and rating <= 8 group by name order by name\"",
			"\"select name from restaurant where rating == 6.5 and rating < 8 group by name order by name\""
		),
		MetaTest.withStringReplacement("Change boundaries",
			"\"select name from restaurant where rating > 6.5 and rating <= 8 group by name order by name\"",
			"\"select name from restaurant where rating > 6.5 group by name order by name\""
		),
		MetaTest.withStringReplacement("Change boundaries",
			"\"select name from restaurant where rating > 6.5 and rating <= 8 group by name order by name\"",
			"\"select name from restaurant where rating <= 8 group by name order by name\""
		),
		MetaTest.withStringReplacement("Change boundaries",
			"\"select name from restaurant where rating > 6.5 and rating <= 8 group by name order by name\"",
			"\"select name from restaurant where rating > 6.5 or rating <= 8 group by name order by name\""
		),
		MetaTest.withStringReplacement("Order should be ASC",
			"\"select name from restaurant where rating > 6.5 and rating <= 8 group by name order by name\"",
			"\"select name from restaurant where rating > 6.5 and rating <= 8 group by name order by name desc\""
		)
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