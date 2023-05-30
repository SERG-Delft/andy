package delft;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The service which performs operations on the Person table in the database.
 */
interface PersonService {

	String findUsernameById(Long id);
}

/** The service which performs operations on the TODO table in the database. */
interface TodoService {

	List<String> retrieveTodos(String username);

	boolean addTodo(String username, String newTodo);

	void completeTodo(String todo);
}

class TodoApplication {

	private final TodoService todoService;

	private final PersonService personService;

	public TodoApplication(TodoService todoService, PersonService personService) {
		this.todoService = todoService;
		this.personService = personService;
	}

	/**
	 * Completes all ToDos from a specific user.
	 *
	 * @param userId
	 *            the id of the user
	 */
	public void completeAllTodos(Long userId) {
		String user = personService.findUsernameById(userId);
		List<String> allTodos = todoService.retrieveTodos(user);
		for (String todo : allTodos) {
			todoService.completeTodo(todo);
		}
	}
}
