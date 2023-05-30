package delft;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

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

    @ParameterizedTest
    @CsvSource({"true", "false"})
    void addTodoTest(boolean returnValue) {
        String todo = "Find all the infinity stones.";
        Mockito.when(personServiceMock.findUsernameById(anyLong())).thenReturn(username);
        Mockito.when(todoServiceMock.addTodo(anyString(), anyString())).thenReturn(returnValue);
        assertThat(todoApp.addTodo(userId, todo)).isEqualTo(returnValue);
        Mockito.verify(todoServiceMock).addTodo(username, todo);
    }


}
