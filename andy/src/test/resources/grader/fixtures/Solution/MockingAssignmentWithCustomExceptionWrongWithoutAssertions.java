package delft;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.time.*;
import java.util.stream.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;

class MyServiceTest {

    @Test
    void test() throws MyException {
        var anotherService = Mockito.mock(AnotherService.class);
        var myService = new MyService(anotherService);

        when(anotherService.anotherMethod()).thenThrow(MyException.class);

        boolean result = myService.myMethod(1);
    }

}
