package delft;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.util.*;
import java.util.stream.*;

import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;
import static delft.Field.*;
import static delft.Property.*;
import static delft.SportsHallPlanner.planHalls;

class SportsHallPlannerTests {

    @Test
    void duplicateHallsTest() {
        SportsHall hall = sportsHall(Set.of(NEAR_CITY_CENTRE, HAS_RESTAURANT), Map.of(BADMINTON, 2));
        assertThatThrownBy(() -> planHalls(
                List.of(request(emptySet(), BADMINTON, 1)),
                List.of(hall, hall))).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("planHallsArguments")
    void planHallsTest(String description,
                       List<Request> requests, List<SportsHall> halls, Map<SportsHall, Request> result) {
        assertThat(planHalls(requests, halls)).isEqualTo(result);
    }

    private static Stream<Arguments> planHallsArguments() {
        SportsHall sportsHallOne = sportsHall(Set.of(NEAR_CITY_CENTRE, CLOSE_PUBLIC_TRANSPORT),
                Map.of(TENNIS, 2, VOLLEYBALL, 1, BASKETBALL, 1, BADMINTON, 2));
        SportsHall sportsHallTwo = sportsHall(emptySet(), Map.of(BADMINTON, 8, BASKETBALL, 2));

        Request matchesFirst = request(Set.of(NEAR_CITY_CENTRE), VOLLEYBALL, 1);
        Request matchesSecond = request(emptySet(), BADMINTON, 4);
        Request matchesBoth = request(emptySet(), BASKETBALL, 1);
        Request matchesBothSecond = request(emptySet(), BADMINTON, 2);
        Request matchesNone = request(Set.of(HAS_RESTAURANT), BADMINTON, 3);

        return Stream.of(
                Arguments.of("No requests, no halls",
                        emptyList(), emptyList(), emptyMap()),
                Arguments.of("No requests, some halls",
                        emptyList(), List.of(sportsHallOne, sportsHallTwo), emptyMap()),
                Arguments.of("Some requests, no halls",
                        List.of(
                                request(Set.of(CLOSE_PUBLIC_TRANSPORT), BADMINTON, 3),
                                request(Set.of(HAS_RESTAURANT), TENNIS, 1)
                        ),
                        emptyList(),
                        null),
                Arguments.of("One request, one suitable hall",
                        List.of(matchesFirst),
                        List.of(sportsHallOne),
                        Map.of(sportsHallOne, matchesFirst)),
                Arguments.of("One request, multiple non-suitable hall",
                        List.of(matchesNone),
                        List.of(sportsHallOne, sportsHallTwo),
                        null),
                Arguments.of("One request, multiple suitable halls, gets assigned to first",
                        List.of(matchesBoth),
                        List.of(sportsHallOne, sportsHallTwo),
                        Map.of(sportsHallOne, matchesBoth)),
                Arguments.of("Multiple requests, no suitable halls for any",
                        List.of(matchesNone, request(Set.of(HAS_RESTAURANT), TENNIS, 5)),
                        List.of(sportsHallOne, sportsHallTwo),
                        null),
                Arguments.of("Multiple requests, no suitable hall at all for last one",
                        List.of(matchesSecond, matchesNone),
                        List.of(sportsHallOne, sportsHallTwo),
                        null),
                Arguments.of("Multiple requests, suitable halls for all, but not enough of them",
                        List.of(matchesFirst, matchesSecond, matchesBoth),
                        List.of(sportsHallOne, sportsHallTwo),
                        null),
                Arguments.of("Multiple requests, suitable hall for first is needed for second",
                        List.of(matchesBoth, matchesFirst),
                        List.of(sportsHallOne, sportsHallTwo),
                        Map.of(sportsHallOne, matchesFirst, sportsHallTwo, matchesBoth)),
                Arguments.of("Multiple requests, halls are suitable for all, first request to first hall",
                        List.of(matchesBoth, matchesBothSecond),
                        List.of(sportsHallOne, sportsHallTwo),
                        Map.of(sportsHallOne, matchesBoth, sportsHallTwo, matchesBothSecond)),
                Arguments.of("Multiple requests, more suitable sport halls than requests present",
                        List.of(matchesBoth, matchesBothSecond),
                        List.of(sportsHallOne, sportsHallTwo, sportsHall(emptySet(), Map.of(BASKETBALL, 1, BADMINTON, 2))),
                        Map.of(sportsHallOne, matchesBoth, sportsHallTwo, matchesBothSecond)),
                Arguments.of("Multiple same requests, with suitable halls",
                        List.of(matchesBoth, matchesBoth),
                        List.of(sportsHallOne, sportsHallTwo),
                        Map.of(sportsHallOne, matchesBoth, sportsHallTwo, matchesBoth))
        );
    }

    private static Request request(Set<Property> properties, Field requiredFieldType, int minNumberOfFields) {
        return new Request(properties, requiredFieldType, minNumberOfFields);
    }

    private static SportsHall sportsHall(Set<Property> properties, Map<Field, Integer> fields) {
        return new SportsHall(properties, fields);
    }
}
