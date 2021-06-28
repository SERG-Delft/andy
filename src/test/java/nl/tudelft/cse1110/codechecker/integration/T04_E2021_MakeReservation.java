package nl.tudelft.cse1110.codechecker.integration;

import nl.tudelft.cse1110.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.codechecker.engine.TestUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class T04_E2021_MakeReservation extends IntegrationTestBase {

    private final CheckScript checkScript = script("integration/t04_e2021_makeReservation/e2021-makeReservation.yml");

    @Test
    void solution1_pass() {
        checkScript.runChecks(new TestUtils().getTestResource("integration/t04_e2021_makeReservation/Solution1.java"));

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
