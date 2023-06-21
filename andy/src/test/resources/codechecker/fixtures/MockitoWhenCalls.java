package delft;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

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
        List<String> mockedList = mock(List.class);
        Mockito.doThrow(IllegalArgumentException.class).when(mockedList).toString();
        doNothing().when(mockedList.size());
        doReturn("2").when(mockedList).get(any(Integer.class));

        verify(mockedList, times(2)).toString();
        verify(mockedList, times(1)).contains();
    }

    @Test
    void t4(){
        List<String> mockedList = mock(List.class);
        Mockito.doNothing().when(mockedList.size());
        doReturn("2").when(mockedList.get(any(Integer.class)));
        doReturn("3").when(mockedList.remove(any(Integer.class)));

        verify(mockedList, times(1)).remove(1);
        verify(mockedList, times(1)).remove(2);
        verify(mockedList, times(1)).remove(3);
        verify(mockedList, times(1)).get(4);
        verify(mockedList, times(1)).get(5);
    }

}
