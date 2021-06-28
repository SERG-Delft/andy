package fixtures.integration.t01_m2021_upvote;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class Solution2VerifyAfterEachTest {

    private final Scoring scoring = mock(Scoring.class);

    private final UserRepository repo = mock(UserRepository.class);

    User author = new User("Mauricio", 100);

    @AfterEach
    void after() {
        verify(repo).update(author);
    }

    @Test
    void upvoteFeaturedPosts() {
        StackOverflow so = new StackOverflow(repo, scoring);
        Post featuredPost = new Post(author, "post 1", true);
        when(scoring.pointsForFeaturedPost()).thenReturn(50);
        so.upvote(featuredPost, mock(User.class));
        assertThat(author.getPoints()).isEqualTo(150);

    }

    @Test
    void upvoteRegularPosts() {
        StackOverflow so = new StackOverflow(repo, scoring);
        Post regularPost = new Post(author, "post 1", false);
        when(scoring.pointsForNormalPost()).thenReturn(20);
        so.upvote(regularPost, mock(User.class));
        assertThat(author.getPoints()).isEqualTo(120);
        verify(repo).update(author);
    }
}

