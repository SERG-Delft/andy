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
            put("coverage", 0.1f);
            put("mutation", 0.1f);
            put("meta", 0.4f);
            put("codechecks", 0.4f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.SoftWhere");
    }

    @Override
    public CheckScript checkScript() {
        return new CheckScript(List.of(
                new SingleCheck("TripRepository should be mocked",
                        new MockClass("TripRepository")),
                new SingleCheck("ReservationRepository should be mocked",
                        new MockClass("ReservationRepository")),
                new SingleCheck( "Trip should not be mocked", true,
                        new MockClass("Trip")),
                new SingleCheck( "SoftWhere should not be mocked", true,
                        new MockClass("SoftWhere")),
                new SingleCheck("Spies should not be used", true,
                        new MockitoSpy()),
                new SingleCheck("getTripById should be set up",
                        new MockitoWhen("getTripById",
                                Comparison.GTE, 1)),
                new SingleCheck("getAllReservationsByTrip should be set up",
                        new MockitoWhen("getAllReservationsByTrip",
                                Comparison.GTE, 1)),
                new SingleCheck("save should not be set up", true,
                        new MockitoWhen("save",
                                Comparison.GTE, 1)),
                new SingleCheck("update should be verified",
                        new MockitoVerify("update", MockitoVerify.MethodType.TEST,
                                Comparison.GTE, 1)),
                new SingleCheck(2,"getAllReservationsByTrip should not be verified", true, new MockitoVerify("getAllReservationsByTrip", 
                                MockitoVerify.MethodType.TEST, Comparison.GTE, 1)),
                new SingleCheck(2, "getTripById should not be verified", true, 
                        new MockitoVerify("getTripById", MockitoVerify.MethodType.TEST, 
                                Comparison.GTE, 1)),
                new SingleCheck("assertDoesNotThrow is not called (no need for it)", true, 
                        new MethodCalledInTestMethod("assertDoesNotThrow")),
                new SingleCheck("getCapacity is not called in the tests", true, 
                        new MethodCalledInTestMethod("getCapacity")),
                new SingleCheck("tests should have assertions", 
                        new TestMethodsHaveAssertions()),
                new OrCheck(2, "update should be verified (with never)", Arrays.asList(
                        new SingleCheck(new MockitoVerify("update", MockitoVerify.MethodType.TEST, 
                                Comparison.GTE, 1, true)),
                        new SingleCheck(new MethodCalledInTestMethod("verifyNoMoreInteractions"))
                ))
        ));
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.withStringReplacement("does not check update", 
                        "tRepository.update(trip, capacity);", 
                        ""),
                MetaTest.withLineReplacement("does not check invalid tripId", 150, 158,
                        """
                        try {
                            Trip trip = tRepository.getTripById(id);
                            if (numberOfReservations(trip) > capacity) return false;
                            tRepository.update(trip, capacity);
                            return true;
                        } catch (ElementNotFoundException e) {
                            throw new RuntimeException("killed the mutant");
                        }
                        """),
                MetaTest.withLineReplacement("does not check capacity", 150, 158,
                        """
                        try {
                            Trip trip = tRepository.getTripById(id);
                            tRepository.update(trip, capacity);
                            return true;
                        } catch (ElementNotFoundException e) {
                            return false;
                        }
                        """),
                MetaTest.withStringReplacement("boundary check",
                        "(numberOfReservations(trip) > capacity)", 
                        "(numberOfReservations(trip) >= capacity)")
        );
    }

}
