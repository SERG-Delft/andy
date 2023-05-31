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

class GradeEmailServiceTest {

    private StudentRepository studentRepository;
    private EmailService emailService;
    private ErrorBuilder errorBuilder;

    private GradeEmailService service;

    private ArgumentCaptor<List<GradeEmail>> emailCaptor;

    @BeforeEach
    void setup() {
        studentRepository = mock(StudentRepository.class);
        emailService = mock(EmailService.class);

        errorBuilder = new ErrorBuilder();

        service = new GradeEmailService(studentRepository, emailService, errorBuilder);

        emailCaptor = ArgumentCaptor.forClass(List.class);
    }

    @Test
    void testSingleEmailBeingSent() {
        when(studentRepository.getEmailsByStudentNumbers(Set.of(1)))
            .thenReturn(Map.of(1, new EmailAddress("one", "test", "nl")));
        doNothing().when(emailService).sendEmails(anyList());

        assertThat(service.sendGradesToStudents(
            Map.of(1, 9.5), "SQT"
        )).isEmpty();

        verify(emailService).sendEmails(emailCaptor.capture());
        List<GradeEmail> emails = emailCaptor.getValue();

        assertThat(emails).isNotEmpty();
        assertThat(emails.get(0).getRecipient().toString()).isEqualTo("one@test.nl");
        assertThat(emails.get(0).getCourse()).isEqualTo("SQT");
        assertThat(emails.get(0).getGrade()).isEqualTo(9.5);
    }

    @Test
    void testNoEmailsSent() {
        when(studentRepository.getEmailsByStudentNumbers(Mockito.any(Set.class)))
            .thenReturn(new HashMap<Integer, EmailAddress>());

        assertThat(service.sendGradesToStudents(
            new HashMap<Integer, Double>(), "SQT"
        )).isEmpty();

        verify(emailService).sendEmails(emailCaptor.capture());
        List<GradeEmail> emails = emailCaptor.getValue();

        assertThat(emails).isEmpty();
    }

    @Test
    void testErrorsContainsAddressNotPresent() {
        when(studentRepository.getEmailsByStudentNumbers(Set.of(1)))
            .thenReturn(Map.of());
        doNothing().when(emailService).sendEmails(anyList());
        
        List<String> errors = service.sendGradesToStudents(
            Map.of(1, 9.5), "SQT"
        );

        assertThat(errors.size()).isEqualTo(1);
        assertThat(errors.get(0)).contains("1");

        verify(emailService).sendEmails(emailCaptor.capture());
        List<GradeEmail> emails = emailCaptor.getValue();

        assertThat(emails).isEmpty();
    }

    @Test
    void testErrorsContainsFailedAddresses() {
        EmailAddress address = new EmailAddress("one", "test", "nl");

        when(studentRepository.getEmailsByStudentNumbers(Set.of(1)))
            .thenReturn(Map.of(1, address));
        doThrow(new CouldNotSendAllEmailsException(List.of(address)))
            .when(emailService).sendEmails(anyList());

        List<String> errors = service.sendGradesToStudents(
            Map.of(1, 9.5), "SQT"
        );

        assertThat(errors.size()).isEqualTo(1);
        assertThat(errors.get(0)).contains(address.toString());

        verify(emailService).sendEmails(emailCaptor.capture());
        List<GradeEmail> emails = emailCaptor.getValue();

        assertThat(emails).isNotEmpty();
        assertThat(emails.get(0).getRecipient().toString()).isEqualTo("one@test.nl");
        assertThat(emails.get(0).getCourse()).isEqualTo("SQT");
        assertThat(emails.get(0).getGrade()).isEqualTo(9.5);
    }

    @Test
    void testManyEmailsSuccessfullySent() {
        EmailAddress one = new EmailAddress("one", "test", "nl");
        EmailAddress two = new EmailAddress("two", "test", "nl");

        when(studentRepository.getEmailsByStudentNumbers(any(Set.class)))
            .thenReturn(Map.of(
                1, one, 2, two
            ));
        doNothing().when(emailService).sendEmails(anyList());

        assertThat(service.sendGradesToStudents(
            Map.of(1, 9.5, 2, 5.7), "SQT"
        )).isEmpty();

        verify(emailService).sendEmails(emailCaptor.capture());
        List<GradeEmail> emails = emailCaptor.getValue();

        assertThat(emails.size()).isEqualTo(2);

        assertThat(emails)
            .usingFieldByFieldElementComparator()
            .contains(new GradeEmail(one, "SQT", 9.5));
        assertThat(emails)
            .usingFieldByFieldElementComparator()
            .contains(new GradeEmail(two, "SQT", 5.7));
    }

}
