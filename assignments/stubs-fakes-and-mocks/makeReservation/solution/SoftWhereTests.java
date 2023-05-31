package delft;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class SoftWhereTests {

	private TripRepository tRepository;

	private ReservationRepository rRepository;

	private SoftWhere softwhere;

	@BeforeEach
	void setup() {
		tRepository = mock(TripRepository.class);
		rRepository = mock(ReservationRepository.class);
		softwhere = new SoftWhere(tRepository, rRepository);
	}

	@Test
	void makeReservationThrows() {
		Long tripId = 365847L;
		when(tRepository.getTripById(tripId)).thenThrow(new ElementNotFoundException());
		assertThat(softwhere.makeReservation(tripId, List.of())).isFalse();
		verify(rRepository, never()).save(any());
	}

	@Test
	void makeReservationFailsWithoutCapacity() {
		Long tripId = 483125L;
		Trip trip = new Trip("",
				new Info(LocalDate.now().plusDays(5), LocalDate.now().plusDays(13), Destination.ZANZIBAR), 199999, 3);
		when(tRepository.getTripById(tripId)).thenReturn(trip);
		when(rRepository.getAllReservationsByTrip(trip)).thenReturn(List.of(
				new Reservation(trip, List.of(new Person("Tony", "", "Stark", LocalDate.ofYearDay(1998, 354)))),
				new Reservation(trip, List.of(new Person("Bucky", "", "Barnes", LocalDate.ofYearDay(1936, 5))))));
		assertThat(softwhere.makeReservation(tripId,
				List.of(new Person("Steve", "", "Rogers", LocalDate.ofYearDay(1936, 109)),
						new Person("Peggy", "", "Carter", LocalDate.ofYearDay(1938, 265))))).isFalse();
		verify(rRepository, never()).save(any());
	}

	@ParameterizedTest
	@CsvSource({"4", "5"})
	void makeReservationSucceeds(int fullCapacity) {
		Long tripId = 42358L;
		Trip trip = new Trip("",
				new Info(LocalDate.now().plusDays(42), LocalDate.now().plusDays(69), Destination.GREECE), 59999,
				fullCapacity);
		when(tRepository.getTripById(tripId)).thenReturn(trip);
		when(rRepository.getAllReservationsByTrip(trip)).thenReturn(List.of(
				new Reservation(trip, List.of(new Person("Tony", "", "Stark", LocalDate.ofYearDay(1998, 354)))),
				new Reservation(trip, List.of(new Person("Bucky", "", "Barnes", LocalDate.ofYearDay(1936, 5))))));
		List<Person> people = List.of(new Person("Steve", "", "Rogers", LocalDate.ofYearDay(1936, 109)),
				new Person("Peggy", "", "Carter", LocalDate.ofYearDay(1938, 265)));
		assertThat(softwhere.makeReservation(tripId, people)).isTrue();
		verify(rRepository).save(new Reservation(trip, people));
	}

	@Test
	void killHardToSeeMutant() {
		/**
		 * This test is here out of curiosity. This kills a mutant that happens when the
		 * subtraction in the `capacityLeft` is replaced by a division! If your test
		 * suite does not contain this test, it is not a problem. We do not count it in
		 * our mutation score.
		 */
		Long tripId = 42358L;
		Trip trip = new Trip("",
				new Info(LocalDate.now().plusDays(42), LocalDate.now().plusDays(69), Destination.GREECE), 59999, 10);
		when(tRepository.getTripById(tripId)).thenReturn(trip);
		when(rRepository.getAllReservationsByTrip(trip)).thenReturn(List.of(
				new Reservation(trip, List.of(new Person("Tony", "", "Stark", LocalDate.ofYearDay(1998, 354)))),
				new Reservation(trip, List.of(new Person("Bucky", "", "Barnes", LocalDate.ofYearDay(1936, 5))))));
		List<Person> people = List.of(new Person("Steve", "", "Rogers", LocalDate.ofYearDay(1936, 109)),
				new Person("Peggy", "", "Carter", LocalDate.ofYearDay(1938, 265)),
				new Person("Peggy2", "", "Carter", LocalDate.ofYearDay(1938, 265)));
		assertThat(softwhere.makeReservation(tripId, people)).isTrue();
		verify(rRepository).save(new Reservation(trip, people));
	}
}
