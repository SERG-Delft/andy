package fixtures.integration.t01_m2021_upvote;

class StackOverflow {

    private final UserRepository userRepository;

    private final Scoring scoring;

    public StackOverflow(UserRepository userRepository, Scoring scoring) {
        this.userRepository = userRepository;
        this.scoring = scoring;
    }

    public void upvote(Post post, User voter) {
        User author = post.getAuthor();
        if (post.isFeatured()) {
            author.addPoints(scoring.pointsForFeaturedPost());
        } else {
            author.addPoints(scoring.pointsForNormalPost());
        }
        userRepository.update(author);
    }
}

interface Scoring {

    int pointsForFeaturedPost();

    int pointsForNormalPost();
}

interface UserRepository {

    void update(User user);
}

class User {

    private String user;

    private int points;

    public User(String user, int points) {
        this.user = user;
        this.points = points;
    }

    public void addPoints(int pointsToAdd) {
        this.points += pointsToAdd;
    }

    public int getPoints() {
        return points;
    }
}

class Post {

    private final User author;

    private final String message;

    private final boolean featured;

    public Post(User author, String message, boolean featured) {
        this.author = author;
        this.message = message;
        this.featured = featured;
    }

    public User getAuthor() {
        return this.author;
    }

    public String getMessage() {
        return message;
    }

    public boolean isFeatured() {
        return featured;
    }
}
