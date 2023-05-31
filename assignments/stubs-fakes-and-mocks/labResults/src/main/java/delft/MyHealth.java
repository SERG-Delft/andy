package delft;

interface Log {

    /**
	 * Adds a warning to the log.
	 *
	 * @param message the message to log
	 */
    void warn(String message);

}

interface AuthService {

	/**
	 * Checks whether a user is allowed to retrieve a specific
     * lab result.
	 *
	 * @param userId the id of the user
	 * @param resultId the id of the lab result
	 * @return true if this user can view this lab result
	 */
	boolean canViewLabResult(long userId, long resultId);
}

interface LabResultRepository {

    /**
	 * Retrieves a lab result from the database using the id.
	 *
	 * @param resultId the id of the lab result
	 * @return the lab result
	 */
    String getLabResultById(long resultId);
}

class MyHealth {

	private final AuthService authService;
    private final LabResultRepository repository;
    private final Log log;

	public MyHealth(
        AuthService authService,
        LabResultRepository repository,
        Log log
    ) {
		this.authService = authService;
        this.repository = repository;
        this.log = log;
	}

	/**
	 * Retrieves a specific lab result if the user requesting
     * it is authorised to view it.
	 * 
	 * @param userId the id of the user requesting the lab result
	 * @param resultId the id of the requested lab result
	 * @return the lab result or null if the user is not authorised
	 */
	public String getLabResult(long userId, long resultId) {
		if (!authService.canViewLabResult(userId, resultId)) {
			log.warn(userId + " tried to illegally access lab result " + resultId);
			return null;
		}
		String result = repository.getLabResultById(resultId);
		if (result == null) {
			log.warn("Lab result " + resultId + " was requested but doesn't exist.");
		}
        return result;
	}
}
