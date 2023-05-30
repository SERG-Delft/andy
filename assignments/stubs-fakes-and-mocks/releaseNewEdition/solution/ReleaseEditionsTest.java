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

class BookBusinessImplTest {

    private BookService bookService;
    private EmailService emailService;
    private ReleaseEditions business;

    @BeforeEach
    void setup() {
        bookService = Mockito.mock(BookService.class);
        emailService = Mockito.mock(EmailService.class);
        business = new ReleaseEditions(bookService, emailService);
    }

    @Test
    void testNoBooks() {
        String author = "Agatha Christie";
        when(bookService.retrieveBooks(author)).thenReturn(List.of());

        business.releaseNewEdition(author, "physics", 1);
    }

    @Test
    void testOneBook() {
        String author = "Agatha Christie";
        when(bookService.retrieveBooks(author)).thenReturn(List.of("And then there were none"));

        business.releaseNewEdition(author, "none", 2);
        Mockito.verify(bookService, times(1)).addBook(author, "And then there were none - edition 2");
    }

    @Test
    void testAnotherKeyword() {
        String author = "Agatha Christie";
        when(bookService.retrieveBooks(author)).thenReturn(List.of("And then there were none"));

        business.releaseNewEdition(author, "people", 4);
        Mockito.verify(bookService, never()).addBook(author, "And then there were none - edition 4");
    }

    @Test
    void testMoreBooks() {
        String author = "Fredrik Backman";
        when(bookService.retrieveBooks(author)).thenReturn(List.of("A man called Ove", "Grandma"));

        business.releaseNewEdition(author, "Ove", 3);
        verify(bookService, times(1)).addBook(author, "A man called Ove - edition 3");
        verify(bookService, never()).addBook(author, "Grandma - edition 3");
    }

    @Test
    void testMoreBooksWithTheKeyword() {
        String author = "Fredrik Backman";
        when(bookService.retrieveBooks(author)).thenReturn(List.of("A man called Ove", "Grandma and Ove"));

        business.releaseNewEdition(author, "Ove", 3);
        verify(bookService, times(1)).addBook(author, "A man called Ove - edition 3");
        verify(bookService, times(1)).addBook(author, "Grandma and Ove - edition 3");
    }

}

