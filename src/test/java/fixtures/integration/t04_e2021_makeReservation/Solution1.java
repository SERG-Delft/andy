package fixtures.integration.t04_e2021_makeReservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class SoftWhereTests {

    private SoftWhere softWhere;
    private TripRepository tripRepository;
    private ReservationRepository reservationRepository;
    private List<Person> people;
    private Info info = new Info(LocalDate.of(2021,7,9),
            LocalDate.of(2021,7,15),
            Destination.ZANZIBAR);

    // Refresh variables used in tests
    @BeforeEach
    void setUp() {
        Person p1 = new Person("Senne", "Marc", "Van den Broeck",
                LocalDate.of(2002,5,8));

        Person p2 = new Person("Dries", "Mark", "Van den Broeck",
                LocalDate.of(1999,7,10));

        Person p3 = new Person("Peter", "Maria", "Van den Broeck",
                LocalDate.of(1969,6,9));

        people = Arrays.asList(p1, p2, p3);

        reservationRepository = mock(ReservationRepository.class);
        tripRepository = mock(TripRepository.class);

        softWhere = new SoftWhere(tripRepository, reservationRepository);
    }

    /**
     * Capacity is too low for reserving
     */
    @Test
    void makeReservationCapacityTooLow() {
        Trip trip = new Trip("Hike", info, 500, 3);

        List<Person> reserved = Arrays.asList(new Person("Peter", "Maria", "Van den Broeck",
                LocalDate.of(1969,6,9)), new Person("Peter", "Maria", "Van den Broeck",
                LocalDate.of(1969,6,9)));

        when(tripRepository.getTripById(5L)).thenReturn(trip);
        when(reservationRepository.getAllReservationsByTrip(trip)).thenReturn(Arrays.asList(new Reservation(trip, reserved)));

        assertFalse(softWhere.makeReservation(5L, people));
        verify(reservationRepository, never()).save(new Reservation(trip, people));

    }

    /**
     * Trip doesn't exist
     */
    @Test
    void makeReservationTripIdNotFound() {

        Trip trip = new Trip("Hike", info, 500, 4);

        when(tripRepository.getTripById(5L)).thenThrow(new ElementNotFoundException());

        assertFalse(softWhere.makeReservation(5L, people));
        verify(reservationRepository, never()).save(new Reservation(trip, people));

    }

    /**
     * Capacity is sufficient -> reservation added
     */
    @Test
    void makeReservationCapacitySufficient() {

        Trip trip = new Trip("Hike", info, 500, 5);
        List<Person> reserved = Arrays.asList(new Person("Peter", "Maria", "Van den Broeck",
                LocalDate.of(1969,6,9)), new Person("Peter", "Maria", "Van den Broeck",
                LocalDate.of(1969,6,9)));
        when(tripRepository.getTripById(5L)).thenReturn(trip);
        when(reservationRepository.getAllReservationsByTrip(trip)).thenReturn(Arrays.asList(new Reservation(trip, reserved)));
        assertTrue(softWhere.makeReservation(5L, people));

        verify(reservationRepository).save(new Reservation(trip, people));
    }

}

