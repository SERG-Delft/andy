package delft;

import nl.tudelft.cse1110.andy.codechecker.checks.*;
import nl.tudelft.cse1110.andy.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.andy.codechecker.engine.SingleCheck;
import nl.tudelft.cse1110.andy.config.MetaTest;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.mode.Mode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration extends RunConfiguration {

    @Override
    public Mode mode() {
        return Mode.PRACTICE;
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.SeatFinder");
    }

    @Override
    public Map<String, Float> weights() {
        return new HashMap<>() {{
            put("coverage", 0.2f);
            put("mutation", 0.3f);
            put("meta", 0.5f);
            put("codechecks", 0.0f);
        }};
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.insertAt("prices null not tested", 32, "if (prices == null) return -1;"),
                MetaTest.insertAt("taken null not tested", 32, "if (taken == null) return -1;"),
                MetaTest.insertAt("price and length having different sizes not tested", 35, "if (prices.length != taken.length) return 0;"),
                MetaTest.insertAt("number of seats being zero not tested", 38, "if (numberOfSeats == 0) return 0;"),
                MetaTest.insertAt("number of seats being negative not tested", 38, "if (numberOfSeats < 0) return 0;"),
                MetaTest.withLineReplacement("does not test the sorting property", 42, 46, "int[] seats = IntStream.range(0, prices.length).toArray();"),
                MetaTest.withLineReplacement("does not test more tickets requested", 57, 57, "continue;"),
                MetaTest.withStringReplacement("does not test taken seats", "!taken[seat]", "true"),
                MetaTest.withStringReplacement("discount not applied at 100 is not tested", "totalPrice > 100.00", "totalPrice == 100.00"),
                MetaTest.withLineReplacement("discount not tested at all", 62, 62, "")
        );
    }
}
