Your task is to write tests for the `EnrollParticipantInOfferingService` class. This class is part of a system that control trainings and who takes part of which training. As the name of the class says, this class is responsible for enrolling participants in an offering of an activity.

Before enrolling the participant, the class checks whether the request is valid. This is done through the `Validator`, another class. If something is wrong, the validator throws a `ValidationException`. If everything is ok, the service then gets the offering and participant from the database (through the `OfferingRepository` and `ParticipantRepository`). It then creates an `Enrollment` (which contains the offering, the participant, and the time the enrollment was generated), persists the offering, and then creates an e-mail that will be sent to the participant.

In order to solve this exercise perfectly, you should: 

* write a strong test suite that achieves high coverage. Although you are writing tests starting from the `EnrollParticipantInOfferingService` class, the test suite should validate the entire required behavior. 
* Mock what needs to be mocked. Don't mock what shouldn't be mocked, according to the guidelines discussed in the course.

Some coding tips:

* AssertJ offers a nice assertion for `ZonedDateTime`s that allow you to compare the date with some delta difference. For example, in the assertion `assertThat(date1).isCloseTo(date2, within(1, ChronoUnit.SECONDS));` if `date1` and `date2` are within 1 second from each other, the assertion passes.

* Mockito's `never()`, `any()`, `doNothing()`, and `doThrow()` may be handy.
