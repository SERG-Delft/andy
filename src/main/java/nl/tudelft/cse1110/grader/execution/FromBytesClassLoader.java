package nl.tudelft.cse1110.grader.execution;

import java.util.HashMap;
import java.util.Map;

public class FromBytesClassLoader extends ClassLoader {
    private final Map<String, byte[]> definitions = new HashMap<>();

    public void addDefinition(final String name, final byte[] bytes) {
        definitions.put(name, bytes);
    }

    @Override
    protected Class<?> loadClass(final String name, final boolean resolve)
            throws ClassNotFoundException {
        final byte[] bytes = definitions.get(name);
        if (bytes != null) {
            return defineClass(name, bytes, 0, bytes.length);
        }
        return super.loadClass(name, resolve);
    }
}
