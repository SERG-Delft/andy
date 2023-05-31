package delft;
class StackOverflow {

    private final UserRepository userRepository;
    private final Scoring scoring;

    public StackOverflow(UserRepository userRepository, Scoring scoring) {
        this.userRepository = userRepository;
        this.scoring = scoring;
    }

    public void downvote(Post post, User voter) {
        User author = post.getAuthor();
        if(voter.isJedi()) {
            author.removePoints(scoring.pointsForJedi());
        } else {
            author.removePoints(scoring.pointsForNormalUser());
        }
        userRepository.update(author);
    }
}

interface Scoring {
    int pointsForJedi();
    int pointsForNormalUser();
}

interface UserRepository {
    void update(User user);
}

class User {
    private String user;
    private int points;
    private boolean jedi;

    public User(String user, int points, boolean jedi) {
        this.user = user;
        this.points = points;
        this.jedi = jedi;
    }

    public User(String user, int points) {
        this(user, points, false);
    }

    public void addPoints(int pointsToAdd) {
        this.points += pointsToAdd;
    }

    public void removePoints(int pointsToAdd) {
        this.points -= pointsToAdd;
    }

    public int getPoints() {
        return points;
    }

    public boolean isJedi() {
        return jedi;
    }
}
class Post {
    private final User author;
    private final String message;

    public Post(User author, String message) {
        this.author = author;
        this.message = message;
    }
    public User getAuthor() {
        return this.author;
    }
    public String getMessage() {
        return message;
    }

}