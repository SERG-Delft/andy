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

        when(mockedList.add("1")).thenReturn(true);
        when(mockedList.remove("1")).thenReturn(true);
        Mockito.when(mockedList.equals("1")).thenReturn(true);

        verify(mockedList).add("1");
        verify(mockedList).remove("1");
    }

    @Test
    void t2() {
        List<String> mockedList = mock(List.class);

        when(mockedList.contains("a")).thenReturn(true);
        Mockito.when(mockedList.contains("b")).thenReturn(true);
        when(mockedList.contains("c")).thenReturn(true);

        when(mockedList.equals("1")).thenReturn(true);
    }

    @Test
    void t3(){
        List<String> mockedQueue = mock(Queue.class);

        doReturn(true).when(mockedList.offer("4"));
        doReturn(true).when(mockedList.poll("4"));
        Mockito.doReturn(true).when(mockedList.equals("4"));
        doNothing().when(mockedQueue.toString());

        verify(mockedList).offer("4");
        verify(mockedList).poll("4");
    }

    @Test
    void t4(){
        List<String> mockedQueue = mock(Queue.class);

        doReturn(true).when(mockedQueue.contains("a"));
        Mockito.doReturn(true).when(mockedQueue.contains("b"));
        doReturn(true).when(mockedQueue.contains("c"));
        doNothing().when(mockedQueue.toString());

        doReturn(true).when(mockedQueue.equals("1"));
    }

}
