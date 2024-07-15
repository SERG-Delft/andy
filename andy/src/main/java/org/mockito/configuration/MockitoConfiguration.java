package org.mockito.configuration;

// The presence of this class configures Mockito to disable the Objenesis cache.
// Caching causes problems when we dynamically replace classes in meta tests.

@SuppressWarnings("unused")
public class MockitoConfiguration extends DefaultMockitoConfiguration {
    @Override
    public boolean enableClassCache() {
        return false;
    }
}
