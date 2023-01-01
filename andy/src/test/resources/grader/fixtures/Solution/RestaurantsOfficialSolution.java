package delft;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;
import java.sql.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class SolutionTest extends RestaurantDaoTemplate {
    @ParameterizedTest(name = "{0}")
    @MethodSource("getAverageGenerator")
    public void getAverageRestaurantTest(String description, List<Restaurant> restaurants,
                                         List<String> expectedOutput) {
        for (Restaurant r : restaurants) {
            restaurantDao.save(r);
        }
        List<String> output = restaurantDao.getAverageRestaurant();
        String[] expected = new String[expectedOutput.size()];
        expectedOutput.toArray(expected);
        assertThat(output).containsExactly(expected);
    }

    public static Stream<Arguments> getAverageGenerator() {
        String cuisineType = "type";
        String location = "location";
        Arguments tc1 = Arguments.of("2 ratings out of bounds, one within second boundary", List.of(
                new Restaurant(1, "Pizzas!", cuisineType, 10.0, location),
                new Restaurant(2, "Burgers", cuisineType, 5.9, location),
                new Restaurant(3, "Seafood", cuisineType, 8.0, location)), List.of("Seafood"));
        Arguments tc2 = Arguments.of("2 ratings out of bounds, one within first boundary", List.of(
                new Restaurant(1, "Pizzas!", cuisineType, 10.0, location),
                new Restaurant(2, "Burgers", cuisineType, 6.5, location),
                new Restaurant(3, "Seafood", cuisineType, 6.6, location)), List.of("Seafood"));
        Arguments tc3 = Arguments.of("1 ratings out of bounds, two within boundaries", List.of(
                new Restaurant(1, "Pizzas!", cuisineType, 7.9, location),
                new Restaurant(2, "Burgers", cuisineType, 6.5, location),
                new Restaurant(3, "Seafood", cuisineType, 6.6, location)), List.of("Pizzas!", "Seafood"));

        return Stream.of(tc1, tc2, tc3);
    }
}
