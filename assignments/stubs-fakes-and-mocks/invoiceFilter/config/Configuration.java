package delft;

import nl.tudelft.cse1110.andy.codechecker.checks.*;
import nl.tudelft.cse1110.andy.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.andy.codechecker.engine.SingleCheck;
import nl.tudelft.cse1110.andy.codechecker.engine.OrCheck;
import nl.tudelft.cse1110.andy.config.MetaTest;
import nl.tudelft.cse1110.andy.config.RunConfiguration;

import java.util.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration extends RunConfiguration {

    @Override
    public Map<String, Float> weights() {
        return new HashMap<>() {{
            put("coverage", 0.25f);
            put("mutation", 0.25f);
            put("meta", 0.25f);
            put("codechecks", 0.25f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.InvoiceFilter");
    }

    @Override
    public CheckScript checkScript() {
        return new CheckScript(List.of(
                new SingleCheck("IssuedInvoices should be mocked",
                        new MockClass("IssuedInvoices")),
                new SingleCheck("InvoiceFilter should not be mocked", true,
                        new MockClass("InvoiceFilter")),
                new SingleCheck("Spies should not be used", true,
                        new MockitoSpy()),
                new SingleCheck(2, "all should not be verified", true,
                        new MockitoVerify("all", MockitoVerify.MethodType.TEST,
                                Comparison.GTE, 1)),
                new SingleCheck("tests should have assertions",
                        new TestMethodsHaveAssertions())
        ));
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.withStringReplacement("change boundary",
                        "return issuedInvoices.all().stream().filter(invoice -> invoice.getValue() < 100).collect(toList());",
                        "return issuedInvoices.all().stream().filter(invoice -> invoice.getValue() <= 100).collect(toList());" ),
                        MetaTest.insertAt("at least two invoices used", 17,
                        """
                        if (issuedInvoices.all().size() >= 2) {
                            throw new RuntimeException("killed the mutant");
                        }
                        """)
        );
    }

}
