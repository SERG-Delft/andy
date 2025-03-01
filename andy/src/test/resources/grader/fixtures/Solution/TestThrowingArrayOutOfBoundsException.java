package delft;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

class ArrayOutOfBoundsTest {

    @Test
    void arrayOutOfBoundsTest() {
        int[] arr = new int[] {1, 2, 2, 3, 4, 4};
        int invalid = arr[10];
        assertEquals(2, arr);
    }
}
