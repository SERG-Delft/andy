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

class StackOverflowTest {

    private final Scoring scoring = mock(Scoring.class);
    private final UserRepository repo = mock(UserRepository.class);

    @Test
    void jediDownvotes() {
        StackOverflow so = new StackOverflow(repo, scoring);

        User author = new User("Mauricio", 100);
        Post post = new Post(author, "post 1");

        User jedi = new User("Jedi downvoter", 1000, true);
        when(scoring.pointsForJedi()).thenReturn(70);

        so.downvote(post, jedi);

        assertThat(author.getPoints()).isEqualTo(30);
        verify(repo).update(author);
    }

    @Test
    void regularUserDownvotes() {
        StackOverflow so = new StackOverflow(repo, scoring);

        User author = new User("Mauricio", 100);
        Post post = new Post(author, "post 1");

        User normalDownvoter = new User("Normal user", 1000);
        when(scoring.pointsForNormalUser()).thenReturn(30);

        so.downvote(post, normalDownvoter);

        assertThat(author.getPoints()).isEqualTo(70);
        verify(repo).update(author);
    }

}