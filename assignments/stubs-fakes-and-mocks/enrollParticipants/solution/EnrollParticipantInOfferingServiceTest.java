package delft;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.within;
import java.time.temporal.ChronoUnit;

import java.util.*;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;
import java.time.*;

class EnrollParticipantInOfferingServiceTest {
        private final Validator validator = mock(Validator.class);
        private final ValidationResult validationResult = mock(ValidationResult.class);
        private final OfferingRepository offerings = mock(OfferingRepository.class);
        private final ParticipantRepository participants = mock(ParticipantRepository.class);
        private final EnrollmentEmailBuilder emailBuilder = mock(EnrollmentEmailBuilder.class);

        private final EnrollParticipantInOfferingService service = new 
            EnrollParticipantInOfferingService(validator, offerings, participants, emailBuilder);

    @Test
    void enrollParticipants() {
        int activityId = 1;
        int offeringId = 2;
        int participantId = 3;

        // instantiate the entities
        // as we saw in the course, there's rarely a need to mock entities
        Activity activity = new Activity(activityId, "CSE1110");
        Offering offering = new Offering(offeringId, activity, ZonedDateTime.now().plusDays(5));
        Participant participant = new Participant(1, "Mauricio Aniche", "mauricio@example.com");

        when(validator.validate(offeringId, participantId)).thenReturn(validationResult);
        doNothing().when(validationResult).backToUserInCaseOfErrors();

        when(offerings.getById(offeringId)).thenReturn(offering);
        when(participants.getById(participantId)).thenReturn(participant);

        // call the method under test
        service.enroll(offeringId, participantId);

        // 1st, we check that the offering was saved
        verify(offerings).save(offering);

        // 2nd, we check that the email is created
        // we need the created enrollment, which we can easily get from the offering object
        // we take the opportunity to assert that the correct Enrollment was created
        assertThat(offering.getEnrollments()).hasSize(1);
        Enrollment newEnrollment = offering.getEnrollments().get(0);

        assertThat(newEnrollment.getOffering()).isEqualTo(offering);
        assertThat(newEnrollment.getParticipant()).isEqualTo(participant);
        assertThat(newEnrollment.getCreatedAt()).isCloseTo(ZonedDateTime.now(), within(1, ChronoUnit.SECONDS));

        verify(emailBuilder).createEnrollmentEmail(activity, offering, newEnrollment);
    }

    @Test
    void validationFails() {
        when(validator.validate(any(Integer.class), any(Integer.class))).thenReturn(validationResult);
        doThrow(new ValidationException()).when(validationResult).backToUserInCaseOfErrors();

        // call the method under test
        assertThrows(ValidationException.class, () -> {
            service.enroll(1, 1); // ids don't matter
        });
        

        // we can make the test stronger by asserting that some methods were not called
        // we don't want to save the offering or generating an email in case validation fails
        verify(offerings, never()).save(any(Offering.class));
        verify(emailBuilder, never()).createEnrollmentEmail(any(Activity.class), any(Offering.class), any(Enrollment.class));
    }

}
