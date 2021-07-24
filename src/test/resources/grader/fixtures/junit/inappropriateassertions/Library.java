package tudelft.smells.innapropriateassertions;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class Cart {
    private final Set<String> items = new HashSet<>();


    public void add(String product) {
        items.add(product);
    }

    public int numberOfItems() {
        return items.size();
    }

    public Set<String> allItems() {
        return Collections.unmodifiableSet(items);
    }
}
