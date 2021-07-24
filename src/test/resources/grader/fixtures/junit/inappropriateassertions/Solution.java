package tudelft.smells.innapropriateassertions;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// In tests 1 and 2, the error message would not show the difference between expected and actual if you'd use assertTrue!
class CartTest {

    private final Cart cart = new Cart();

    @Test
    void numberOfItems() {
        cart.add("Playstation");
        cart.add("Big TV");
         assertTrue(cart.numberOfItems() == 2);
    }

    @Test
    void ignoreDuplicatedEntries() {
        cart.add("Playstation");
        cart.add("Big TV");
        cart.add("Playstation");

        assertTrue(cart.numberOfItems() == 2);
    }

    @Test
    void allItems() {
        cart.add("Playstation");
        cart.add("Big TV");

        var items = cart.allItems();

        assertThat(items).containsExactlyInAnyOrder("Playstation", "Big TV");
    }

}
