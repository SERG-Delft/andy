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
	 * Adds a todo for a user based on their id.
	 *
	 * @param userId
	 *            the id of the
	 * @param newTodo
	 *            the new todo
	 * @return true, if the todo was successfully added
	 */
	public boolean addTodo(Long userId, String newTodo) {
		String user = personService.findUsernameById(userId);
		return todoService.addTodo(user, newTodo);
	}
}
