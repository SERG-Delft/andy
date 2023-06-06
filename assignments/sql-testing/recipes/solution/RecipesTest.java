package delft;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;
import java.sql.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;


class RecipesTest extends RecipeDaoTemplate {
    @ParameterizedTest(name = "{0}")
    @MethodSource("getQuickestGenerator")
    public void getQuickest(String description, List<Recipe> recipes, int quickestDessertTime) {
        for (Recipe recipe:recipes) {
            recipeDao.save(recipe);
        }

        assertEquals(recipeDao.getQuickestVeganDessert(), quickestDessertTime);
    }

    public static Stream<Arguments> getQuickestGenerator() {
        String dummyName = "name";
        int dummyPrice = 5;
        Arguments tc1 = Arguments.of("Gets the quickest dessert that is also vegan. Note that the actual quickest dessert is not vegan.", List.of(
                new Recipe(1, 10, dummyPrice, false, true, dummyName),
                new Recipe(2, 80, dummyPrice, true, true, dummyName),
                new Recipe(3, 40, dummyPrice, true, true, dummyName),
                new Recipe(4, 70, dummyPrice, true, false, dummyName)),
                40);

        Arguments tc2 = Arguments.of("This test ensures that only desserts are chosen.", List.of(
                new Recipe(1, 10, dummyPrice, true, false, dummyName),
                new Recipe(2, 80, dummyPrice, true, true, dummyName),
                new Recipe(3, 40, dummyPrice, true, true, dummyName),
                new Recipe(4, 70, dummyPrice, true, false, dummyName)),
                40);
        return Stream.of(tc1, tc2);
    }
}
