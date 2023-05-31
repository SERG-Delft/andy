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
	return List.of("delft.EmployeeDao", "delft.RestaurantDao");
  }

  @Override
  public List<MetaTest> metaTests() {
      return List.of(
        MetaTest.withStringReplacement("Does not check if restaurant exists in database",
        "\"select e.name from employee as e join restaurant as r on e.restaurant_name = r.name where r.name like 'P%' and e.age < 35\"",
        "\"select e.name from employee as e join where e.restaurant_name like 'P%' and e.age < 35\""),
        MetaTest.withStringReplacement("Does not check correct boundary",
        "\"select e.name from employee as e join restaurant as r on e.restaurant_name = r.name where r.name like 'P%' and e.age < 35\"",
        "\"select e.name from employee as e join restaurant as r on e.restaurant_name = r.name where r.name like 'P%' and e.age <= 35\""),
        MetaTest.withStringReplacement("Does not check correct boundary",
        "\"select e.name from employee as e join restaurant as r on e.restaurant_name = r.name where r.name like 'P%' and e.age < 35\"",
        "\"select e.name from employee as e join restaurant as r on e.restaurant_name = r.name where r.name like 'P%' and e.age > 35\""),
        MetaTest.withStringReplacement("Swapped and with or",
        "\"select e.name from employee as e join restaurant as r on e.restaurant_name = r.name where r.name like 'P%' and e.age < 35\"",
        "\"select e.name from employee as e join restaurant as r on e.restaurant_name = r.name where r.name like 'P%' or e.age < 35\""),
        MetaTest.withStringReplacement("Does not check correct restaurant name",
        "\"select e.name from employee as e join restaurant as r on e.restaurant_name = r.name where r.name like 'P%' and e.age < 35\"",
        "\"select e.name from employee as e join restaurant as r on e.restaurant_name = r.name where r.name like 'S%' and e.age < 35\""),
        MetaTest.withStringReplacement("Only selects all employees",
        "\"select e.name from employee as e join restaurant as r on e.restaurant_name = r.name where r.name like 'P%' and e.age < 35\"",
        "\"select e.name from employee as e\""),
        MetaTest.withStringReplacement("Changed join",
        "\"select e.name from employee as e join restaurant as r on e.restaurant_name = r.name where r.name like 'P%' and e.age < 35\"",
        "\"select e.name from employee as e join restaurant as r on e.restaurant_name != r.name where r.name like 'P%' and e.age < 35\""),
        MetaTest.withStringReplacement("Does not check boundaries at all",
        "\"select e.name from employee as e join restaurant as r on e.restaurant_name = r.name where r.name like 'P%' and e.age < 35\"",
        "\"select e.name from employee as e join restaurant as r on e.restaurant_name = r.name where r.name like 'P%'\"")
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