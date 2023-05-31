package delft;

import java.util.*;

interface BookService {

	List<String> retrieveBooks(String author);

	void deleteBook(String author, String bookTitle);

	void addBook(String author, String newBookTitle);

	void updateLastVisualization(String author, String bookTitle);

	void moveBook(String author, String bookTitle, String destAuthor);
}

interface EmailService {

	void sendNotification(String bookTitle);
}

class ReleaseEditions {

	private final BookService bookService;

	private final EmailService emailService;

	public ReleaseEditions(BookService bookService, EmailService emailService) {
		this.bookService = bookService;
		this.emailService = emailService;
	}

	/**
	 * Creates a copy of the book, now with the new edition in the title.
	 *
	 * @param author
	 *            name of the author
	 * @param keyword
	 *            keyword to search
	 * @param edition
	 *            number of the new edition
	 */
	public void releaseNewEdition(String author, String keyword, int edition) {
		List<String> allBooks = bookService.retrieveBooks(author);
		for (String bookTitle : allBooks) {
			if (bookTitle.contains(keyword)) {
				bookService.addBook(author, bookTitle + " - edition " + edition);
			}
		}
	}
}



