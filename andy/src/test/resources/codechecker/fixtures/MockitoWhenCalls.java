package delft;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Queue;

import static org.mockito.Mockito.*;

public class MockitoWhenCalls {

    @Test
    void t1() {
        List<String> mockedList = mock(List.class);

        Mockito.doThrow(IllegalAccessError.class).when(mockedList).toString();
        when(mockedList.add("1")).thenReturn(true);
        when(mockedList.remove("1")).thenReturn(true);
        Mockito.when(mockedList.equals("1")).thenReturn(true);

        verify(mockedList).add("1");
        verify(mockedList).remove("1");
        verify(mockedList).toString();
    }

    @Test
    void t2() {
        List<String> mockedList = mock(List.class);

        when(mockedList.contains("a")).thenReturn(true);
        Mockito.when(mockedList.contains("b")).thenReturn(true);
        when(mockedList.contains("c")).thenReturn(true);

        when(mockedList.equals("2")).thenReturn(true);
        verify(mockedList).contains("c");
    }

    @Test
    void t3() {
        List<String> mockedList = mock(List.class);

        doReturn(true).when(mockedList.add("4"));
        doReturn(true).when(mockedList.remove("4"));
        Mockito.doReturn(true).when(mockedList.equals("3"));
        doNothing().when(mockedList.toString());

        verify(mockedList).add("4");
        verify(mockedList).remove("4");
    }

    @Test
    void t4() {
        List<String> mockedList = mock(List.class);

        doReturn(true).when(mockedList.contains("a"));
        Mockito.doReturn(true).when(mockedList.contains("b"));
        doReturn(true).when(mockedList.contains("c"));
        doNothing().when(mockedList).toString();

        doReturn(true).when(mockedList.equals("4"));
        verify(mockedList).equals("4");
    }
}
