package nl.tudelft.cse1110.andy.codechecker.integration;

import nl.tudelft.cse1110.andy.codechecker.CodeCheckerTestUtils;
import nl.tudelft.cse1110.andy.codechecker.checks.*;
import nl.tudelft.cse1110.andy.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.andy.codechecker.engine.SingleCheck;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static nl.tudelft.cse1110.andy.codechecker.checks.MockitoVerify.MethodType.TEST;
import static org.assertj.core.api.Assertions.assertThat;

public class T04_E2021_MakeReservation {

    private final CheckScript checkScript = new CheckScript(Arrays.asList(
            new SingleCheck("TripRepository should be mocked", new MockClass("TripRepository")),
            new SingleCheck("ReservationRepository should be mocked", new MockClass("ReservationRepository")),
            new SingleCheck("Trip should not be mocked", true, new MockClass("Trip")),
            new SingleCheck("SoftWhere should not be mocked", true, new MockClass("SoftWhere")),
            new SingleCheck("Spies should not be used", true, new MockitoSpy()),
            new SingleCheck("getTripById should be set up", new MockitoWhen("getTripById", Comparison.GTE, 1)),
            new SingleCheck("getAllReservationsByTrip should be set up", new MockitoWhen("getAllReservationsByTrip", Comparison.GTE, 1)),
            new SingleCheck("update should not be set up", true, new MockitoWhen("update", Comparison.GTE, 1)),
            new SingleCheck("save should be verified", new MockitoVerify("save", TEST, Comparison.GTE, 1)),
            new SingleCheck("save should be verified (with never) once", new MockitoVerify("save", TEST, Comparison.EQ, 1, true)),
            new SingleCheck("getAllReservationsByTrip should not be verified", true, new MockitoVerify("getAllReservationsByTrip", TEST, Comparison.GTE, 1)),
            new SingleCheck("getTripById should not be verified", true, new MockitoVerify("getTripById", TEST, Comparison.GTE, 1)),
            new SingleCheck("assertDoesNotThrow is not called (no need for it)", true, new MethodCalledInTestMethod("assertDoesNotThrow")),
            new SingleCheck("getCapacity is not called in the tests", true, new MethodCalledInTestMethod("getCapacity")),
            new SingleCheck("tests should have assertions", new TestMethodsHaveAssertions())
    ));

    @Test
    void solution1_pass() {
        checkScript.runChecks(new CodeCheckerTestUtils().getTestResource("integration/t04_e2021_makeReservation/Solution1.java"));

        assertThat(checkScript.generateReport()).isEqualTo(
                "15\n" +
                "14\n" +
                "TripRepository should be mocked: PASS (weight: 1)\n" +
                "ReservationRepository should be mocked: PASS (weight: 1)\n" +
                "Trip should not be mocked: PASS (weight: 1)\n" +
                "SoftWhere should not be mocked: PASS (weight: 1)\n" +
                "Spies should not be used: PASS (weight: 1)\n" +
                "getTripById should be set up: PASS (weight: 1)\n" +
                "getAllReservationsByTrip should be set up: PASS (weight: 1)\n" +
                "update should not be set up: PASS (weight: 1)\n" +
                "save should be verified: PASS (weight: 1)\n" +
                "save should be verified (with never) once: FAIL (weight: 1)\n" +
                "getAllReservationsByTrip should not be verified: PASS (weight: 1)\n" +
                "getTripById should not be verified: PASS (weight: 1)\n" +
                "assertDoesNotThrow is not called (no need for it): PASS (weight: 1)\n" +
                "getCapacity is not called in the tests: PASS (weight: 1)\n" +
                "tests should have assertions: PASS (weight: 1)\n"
        );
    }
}
