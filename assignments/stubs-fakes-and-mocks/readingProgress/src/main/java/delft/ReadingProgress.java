package delft;

interface EmailService {

	void sendKeepUpGoodWorkEmail(int bookId);

	void sendAlmostThereEmail(int bookId);

	void sendCongratsYouHaveMadeItEmail(int bookId);
}

interface UserService {

	boolean isMarketingAuthorised(int userId);
}

interface BookService {

	void updateLastVisualization(int bookId, double percentageRead);
}

class ReadingProgress {

	private final UserService userService;

	private final BookService bookService;

	private final EmailService emailService;

	public ReadingProgress(UserService userService, BookService bookService, EmailService emailService) {
		this.userService = userService;
		this.bookService = bookService;
		this.emailService = emailService;
	}

	public void readingProgress(int userId, int bookId, double percentageRead) {
		bookService.updateLastVisualization(bookId, percentageRead);
		boolean mkt = userService.isMarketingAuthorised(userId);
		if (mkt) {
			if (percentageRead < 50.0) {
				emailService.sendKeepUpGoodWorkEmail(bookId);
			} else if (percentageRead < 99.0) {
				emailService.sendAlmostThereEmail(bookId);
			} else {
				emailService.sendCongratsYouHaveMadeItEmail(bookId);
			}
		}
	}
}
