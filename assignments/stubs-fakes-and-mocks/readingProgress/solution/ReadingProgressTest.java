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
import org.mockito.*;

class ReadingProgressTest {

    private final EmailService emailService = Mockito.mock(EmailService.class);
    private final UserService userService = Mockito.mock(UserService.class);
    private final BookService bookService = Mockito.mock(BookService.class);
    private final ReadingProgress rp = new ReadingProgress(userService, bookService, emailService);

    // Test when the user has a percentage read of less than 50.
    @Test
    void testLessThan50PercetageRead() {
        int userID = 899;
        int bookID = 3812;
        double percentageRead = 49.0;

        when(userService.isMarketingAuthorised(userID)).thenReturn(true);
        rp.readingProgress(userID, bookID, percentageRead);

        verify(emailService, times(1)).sendKeepUpGoodWorkEmail(bookID);
        verify(bookService, times(1)).updateLastVisualization(bookID, percentageRead);
        verify(emailService, never()).sendAlmostThereEmail(bookID);
        verify(emailService, never()).sendCongratsYouHaveMadeItEmail(bookID);
    }

    // Test when the user has a percentage read of 50.
    @Test
    void test50PercentageRead() {
        int userID = 899;
        int bookID = 3812;
        double percentageRead = 50.0;

        when(userService.isMarketingAuthorised(userID)).thenReturn(true);
        rp.readingProgress(userID, bookID, percentageRead);

        verify(emailService, never()).sendKeepUpGoodWorkEmail(bookID);
        verify(bookService, times(1)).updateLastVisualization(bookID, percentageRead);
        verify(emailService, times(1)).sendAlmostThereEmail(bookID);
        verify(emailService, never()).sendCongratsYouHaveMadeItEmail(bookID);
    }

    // Test when the user has a percentage read of 99.
    @Test
    void test99PercentageRead() {
        int userID = 899;
        int bookID = 3812;
        double percentageRead = 99.0;

        when(userService.isMarketingAuthorised(userID)).thenReturn(true);
        rp.readingProgress(userID, bookID, percentageRead);

        verify(emailService, never()).sendKeepUpGoodWorkEmail(bookID);
        verify(emailService, never()).sendAlmostThereEmail(bookID);
        verify(emailService, times(1)).sendCongratsYouHaveMadeItEmail(bookID);
    }
    
    // Test when the user has a percentage read of greater than 50 and less than 99.
    @Test
    void testLessThan99PercentageRead() {
        int userID = 234;
        int bookID = 5632;
        double percentageRead = 87.0;

        when(userService.isMarketingAuthorised(userID)).thenReturn(true);
        rp.readingProgress(userID, bookID, percentageRead);

        verify(emailService, times(1)).sendAlmostThereEmail(bookID);
        verify(bookService, times(1)).updateLastVisualization(bookID, percentageRead);
        verify(emailService, never()).sendKeepUpGoodWorkEmail(bookID);
        verify(emailService, never()).sendCongratsYouHaveMadeItEmail(bookID);
    }

    // Test for when the user has read the whole book and the percentage is 100.
    @Test
    void test100PercentageRead() {
        int userID = 1232;
        int bookID = 2434;
        double percentageRead = 100.0;

        when(userService.isMarketingAuthorised(userID)).thenReturn(true);
        rp.readingProgress(userID, bookID, percentageRead);

        verify(emailService, times(1)).sendCongratsYouHaveMadeItEmail(bookID);
        verify(bookService, times(1)).updateLastVisualization(bookID, percentageRead);
        verify(emailService, never()).sendAlmostThereEmail(bookID);
        verify(emailService, never()).sendKeepUpGoodWorkEmail(bookID);
    }

    // Test for when the user is not authorized.
    @Test
    void testNotAuthorized() {
        int userID = 987;
        int bookID = 2211;
        double percentageRead = 78.0;

        when(userService.isMarketingAuthorised(userID)).thenReturn(false);
        rp.readingProgress(userID, bookID, percentageRead);

        verify(emailService, never()).sendCongratsYouHaveMadeItEmail(bookID);
        verify(emailService, never()).sendKeepUpGoodWorkEmail(bookID);
        verify(bookService, times(1)).updateLastVisualization(bookID, percentageRead);
    }

}

