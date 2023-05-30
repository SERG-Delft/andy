package delft;

import java.util.*;
import java.util.stream.Collectors;

class EmailAddress {

    private String username;
    private String company;
    private String domain;

    public EmailAddress(String username, String company, String domain) {
        this.username = username;
        this.company = company;
        this.domain = domain;
    }

    @Override
    public String toString() {
        return username + "@" + company + "." + domain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmailAddress that = (EmailAddress) o;

        if (!Objects.equals(username, that.username)) return false;
        if (!Objects.equals(company, that.company)) return false;
        return Objects.equals(domain, that.domain);
    }

}

class GradeEmail {

    private EmailAddress recipient;
    private String course;
    private double grade;

    public GradeEmail(
        EmailAddress recipient,
        String course,
        double grade
    ) {
        this.recipient = recipient;
        this.course = course;
        this.grade = grade;
    }

    public EmailAddress getRecipient() {
        return this.recipient;
    }

    public String getCourse() {
        return this.course;
    }

    public double getGrade() {
        return this.grade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GradeEmail that = (GradeEmail) o;

        if (Double.compare(that.grade, grade) != 0) return false;
        if (!Objects.equals(recipient, that.recipient)) return false;
        return Objects.equals(course, that.course);
    }

}

class CouldNotSendAllEmailsException extends RuntimeException {

    private List<EmailAddress> addresses;
    
    public CouldNotSendAllEmailsException(List<EmailAddress> addresses) {
        super();
        this.addresses = addresses;
    }
    
    public List<EmailAddress> getAddresses() {
        return this.addresses;
    }

}

interface EmailService {

    /**
    * Sends a list of emails.
    *
    * @param emails the list of emails to send
    * @throws a CouldNotSendAllEmailsException holding the list
    *         of email addresses it could not send a mail to
    */
    void sendEmails(List<GradeEmail> emails) throws CouldNotSendAllEmailsException;

}

interface StudentRepository {

    /**
    * Retrieves students' emails by their student number.
    * Will only contain the numbers that it successfully found email for.
    * 
    * @param students the set of student numbers to retrieve for
    * @return a map linking student numbers to email addresses
    */
    Map<Integer, EmailAddress> getEmailsByStudentNumbers(Set<Integer> students);

}

class ErrorBuilder {

    /**
    * Builds an error string from a message and an object.
    */
    public <T> String buildError(String message, T errorObject) {
        return message + ": " + errorObject.toString();
    }

    /**
    * Builds a error strings from a message and objects.
    */
    public <T> List<String> buildErrors(String message, List<T> errorObjects) {
        return errorObjects.stream()
            .map(e -> buildError(message, e))
            .collect(Collectors.toList());
    }

}

class GradeEmailService {

    private StudentRepository studentRepository;
    private EmailService emailService;
    private ErrorBuilder errorBuilder;

    public GradeEmailService(
        StudentRepository studentRepository,
        EmailService emailService,
        ErrorBuilder errorBuilder
    ) {
        this.studentRepository = studentRepository;
        this.emailService = emailService;
        this.errorBuilder = errorBuilder;
    }

    /**
    * This method sends an email to students about their grades for a course.
    * 
    * @param grades a map of student numbers pointing to grades
    * @param course the name of the course
    * @return the list of errors
    */
    public List<String> sendGradesToStudents(Map<Integer, Double> grades, String course) {
        Set<Integer> studentNumbers = grades.keySet();

        Map<Integer, EmailAddress> addresses = 
            studentRepository.getEmailsByStudentNumbers(studentNumbers);

        List<GradeEmail> emails = new ArrayList<>();

        List<String> errors = new ArrayList<>();
        for (int student : studentNumbers) {
            EmailAddress address = addresses.get(student);
            if (address != null) {
                emails.add(new GradeEmail(address, course, grades.get(student)));
            } else {
                errors.add(errorBuilder.buildError(
                    "Did not find email address for student with number",
                    student
                ));
            }
        }

        try {
            emailService.sendEmails(emails);
        } catch (CouldNotSendAllEmailsException e) {
            errors.addAll(errorBuilder.buildErrors(
                "Could not send email to",
                e.getAddresses()
            ));
        }

        return errors;
    }

}
