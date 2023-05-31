package delft;

import java.time.*;
import java.util.*;

class Activity {
    private final int id;
    private final String name;

    public Activity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}

class Enrollment {
    private final Offering offering;
    private final Participant participant;
    private ZonedDateTime createdAt;

    public Enrollment(Offering offering, Participant participant, ZonedDateTime createdAt) {
        this.offering = offering;
        this.participant = participant;
        this.createdAt = createdAt;
    }

    public Offering getOffering() {
        return this.offering;
    }

    public Participant getParticipant() {
        return this.participant;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }
}

class Offering {
    private int id;
    private final Activity activity;
    private final ZonedDateTime startTime;
    private final List<Enrollment> enrollments;

    public Offering(int id, Activity activity, ZonedDateTime startTime) {
        this.id = id;
        this.activity = activity;
        this.startTime = startTime;
        this.enrollments = new ArrayList<>();
    }

    public int getId() {
        return this.id;
    }

    public Activity getActivity() {
        return this.activity;
    }

    public ZonedDateTime startTime() {
        return this.startTime;
    }

    public List<Enrollment> getEnrollments() {
        return Collections.unmodifiableList(enrollments);
    }

    public Enrollment enroll(Participant participant) {
        Enrollment newEnrollment = new Enrollment(this, participant, ZonedDateTime.now());
        this.enrollments.add(newEnrollment);
        return newEnrollment;
    } 
}

class Participant {
    private final int id;
    private final String name;
    private final String email;

    public Participant(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }
}

interface Validator {
    ValidationResult validate(int offeringId, int participantId);
}

class ValidationException extends RuntimeException {}

interface ValidationResult {
    // this method may throw ValidationException
    void backToUserInCaseOfErrors();
}

interface OfferingRepository {
    void save(Offering offering);
    Offering getById(int offeringId);
}

interface ParticipantRepository {
    Participant getById(int participantId);

}

interface EnrollmentEmailBuilder {
    void createEnrollmentEmail(Activity activity, Offering offering, Enrollment enrollment);
}

class EnrollParticipantInOfferingService {
    private final Validator validator;
    private final OfferingRepository offerings;
    private final ParticipantRepository participants;
    private final EnrollmentEmailBuilder emailBuilder;

    public EnrollParticipantInOfferingService(Validator validator, OfferingRepository offerings, 
      ParticipantRepository participants, EnrollmentEmailBuilder emailBuilder) {
        this.validator = validator;
        this.offerings = offerings;
        this.participants = participants;
        this.emailBuilder = emailBuilder;
    }

    public void enroll(int offeringId, int participantId) {
        // some validation happens here
        // in case something fails, the method throws a ValidationException
        ValidationResult validation = validator.validate(offeringId, participantId);
        validation.backToUserInCaseOfErrors();

        // we get the Offering and Participant from the database
        // in here, we know that they exist, as the validation already takes care of checking it
        Offering offering = offerings.getById(offeringId);
        Participant participant = participants.getById(participantId);

        // we create and save the enrollment
        Enrollment newEnrollment = offering.enroll(participant);
        offerings.save(offering);

        // we send an enrollment email to the participant
        Activity activity = offering.getActivity();
        emailBuilder.createEnrollmentEmail(activity, offering, newEnrollment);
    }
}
