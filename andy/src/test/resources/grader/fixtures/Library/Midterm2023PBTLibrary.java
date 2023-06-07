package delft;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.*;

class StringUtils {
    private StringUtils() {
        // Empty constructor
    }

    public static List<Integer> indices(List<String> strings) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < strings.size(); i++) {
            if (strings.get(i).startsWith("e")) {
                result.add(i);
            }
        }

        return result;
    }
}
