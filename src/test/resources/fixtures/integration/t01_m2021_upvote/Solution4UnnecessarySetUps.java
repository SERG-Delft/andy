package fixtures.integration.t01_m2021_upvote;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class Solution4UnnecessarySetUps {

    //Dependencies
    private UserRepository userRepository;
    private Scoring scoring;

    //Class Object
    private StackOverflow stackOverflow;


    /** Setup the test environment for each test
     *
     */
    @BeforeEach
    void setup() {
        //Initialize and mock the two given dependencies
        userRepository = mock(UserRepository.class);
        scoring = mock(Scoring.class);

        //Initialize the given class Object with the mocked dependencies
        stackOverflow = new StackOverflow(userRepository, scoring);
    }


    /** Test Cases
     * There are two branches in the given method, separated by a simple if condition.
     * Therefore we need two test cases to achieve 100% branch+condition coverage
     * In these two test cases, the dependencies are mocked and tested
     *     that they behave correctly
     */
    @Test
    void featuredPostTest() {
        //Test Case 1 -> the Post given as a parameter is featured.

        //Initialize the test
        User user = new User("Codrin", 42);
        Post post = new Post(user, "message", true);

        //how should the dependencies methods behave when called
        when(scoring.pointsForFeaturedPost()).thenReturn(10);
        when(scoring.pointsForNormalPost()).thenReturn(1);

        //call the method under test
        stackOverflow.upvote(post, user);

        //Assert that the number of points for the user is changed correctly after the method call
        assertEquals(52, user.getPoints());

        //verify that the correct method in scoring dependency is called
        verify(scoring, times(1)).pointsForFeaturedPost();
        verify(scoring, never()).pointsForNormalPost();

        //Verify that the update method in the userRepository is called exactly once for the given user
        verify(userRepository, times(1)).update(user);
    }

    @Test
    void notFeaturedPostTest() {
        //Test Case 2 -> the Post given as a parameter is not featured.

        //Initialize the test
        User user = new User("Codrin", 42);
        Post post = new Post(user, "message", false);

        //how should the dependencies methods behave when called
        when(scoring.pointsForFeaturedPost()).thenReturn(10);
        when(scoring.pointsForNormalPost()).thenReturn(1);

        //call the method under test
        stackOverflow.upvote(post, user);

        //Assert that the number of points for the user is changed correctly after the method call
        assertEquals(43, user.getPoints());

        //verify that the correct method in scoring dependency is called
        verify(scoring, never()).pointsForFeaturedPost();
        verify(scoring, times(1)).pointsForNormalPost();

        //Verify that the update method in the userRepository is called exactly once for the given user
        verify(userRepository, times(1)).update(user);
    }
}
