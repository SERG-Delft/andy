package delft;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;

class TodoApplicationTest {

    private TodoApplication todoApp;

    private PersonService personServiceMock;

    private TodoService todoServiceMock;

    private final Long userId = 42L;

    private final String username = "tony-stark";

    @BeforeEach
    void setup() {
        personServiceMock = Mockito.mock(PersonService.class);
        todoServiceMock = Mockito.mock(TodoService.class);
        todoApp = new TodoApplication(todoServiceMock, personServiceMock);
    }

    @Test
    void completeAllTodosTest() {
        List<String> todos = List.of("Find all the infinity stones.", "Snap your fingers.", "Save the world.");
        Mockito.when(personServiceMock.findUsernameById(anyLong())).thenReturn(username);
        Mockito.when(todoServiceMock.retrieveTodos(username)).thenReturn(todos);
        Mockito.doNothing().when(todoServiceMock).completeTodo(anyString());
        todoApp.completeAllTodos(userId);
        Mockito.verify(todoServiceMock).completeTodo(todos.get(0));
        Mockito.verify(todoServiceMock).completeTodo(todos.get(1));
        Mockito.verify(todoServiceMock).completeTodo(todos.get(2));
        Mockito.verify(todoServiceMock, times(3)).completeTodo(anyString());
    }

    @Test
    void completeAllTodosWithNoNotes() {
        Mockito.when(personServiceMock.findUsernameById(anyLong())).thenReturn(username);
        Mockito.when(todoServiceMock.retrieveTodos(username)).thenReturn(List.of());
        todoApp.completeAllTodos(userId);
        Mockito.verify(todoServiceMock, times(0)).completeTodo(anyString());
    }
}
