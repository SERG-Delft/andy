package delft;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Queue;

import static org.mockito.Mockito.mock;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.BeforeEach;

/**
 * Check if a specific class was mocked at least once, anywhere in
 * the code of the given class.
 *
 * Params:
 * - Name of the class expected to be mocked. Provide only the name of the class.
 *   e.g., Class (and not a.b.c.Class)
 *
 * Output: it returns true if the class was mocked at least once.
 */
public class ManyMocks {

    // Mock with annotation
    @Mock
    Queue<String> mockedQueue;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void t1() {
        // full call to the static method
        List<String> mockedList = Mockito.mock(List.class);
        // with the static import
        Set<String> mockedSet = mock(Set.class);
        // no mock
        HashMap<String, String> concreteHashMap = new HashMap<>();
    }

}
