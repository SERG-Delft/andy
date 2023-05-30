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
	void editTripCapacityThrows() {
		Long tripId = 5452L;
		when(tRepository.getTripById(tripId)).thenThrow(new ElementNotFoundException());
		assertThat(softwhere.editTripCapacity(tripId, 54)).isFalse();
		verify(tRepository, never()).update(any(Trip.class), anyInt());
	}

	@Test
	void editTripCapacityFailsWithLowCap() {
		Long tripId = 483125L;
		Trip trip = new Trip("", new Info(LocalDate.now().plusDays(5), LocalDate.now().plusDays(13), Destination.MALTA),
				999, 4);
		when(tRepository.getTripById(tripId)).thenReturn(trip);
		when(rRepository.getAllReservationsByTrip(trip)).thenReturn(
				List.of(new Reservation(trip, List.of(new Person("Tony", "", "Stark", LocalDate.ofYearDay(1998, 354)))),
						new Reservation(trip, List.of(new Person("Bucky", "", "Barnes", LocalDate.ofYearDay(1936, 5)))),
						new Reservation(trip, List.of(new Person("Steve", "", "Rogers", LocalDate.ofYearDay(1936, 109)),
								new Person("Peggy", "", "Carter", LocalDate.ofYearDay(1938, 265))))));
		assertThat(softwhere.editTripCapacity(tripId, 3)).isFalse();
		verify(tRepository, never()).update(any(), anyInt());
	}

	@ParameterizedTest
	@CsvSource({"4", "5"})
	void editTripCapacitySucceeds(int newCapacity) {
		Long tripId = 2545L;
		Trip trip = new Trip("",
				new Info(LocalDate.now().plusDays(5), LocalDate.now().plusDays(13), Destination.GREECE), 7999, 4);
		when(tRepository.getTripById(tripId)).thenReturn(trip);
		when(rRepository.getAllReservationsByTrip(trip)).thenReturn(
				List.of(new Reservation(trip, List.of(new Person("Tony", "", "Stark", LocalDate.ofYearDay(1998, 354)))),
						new Reservation(trip, List.of(new Person("Bucky", "", "Barnes", LocalDate.ofYearDay(1936, 5)))),
						new Reservation(trip, List.of(new Person("Steve", "", "Rogers", LocalDate.ofYearDay(1936, 109)),
								new Person("Peggy", "", "Carter", LocalDate.ofYearDay(1938, 265))))));
		assertThat(softwhere.editTripCapacity(tripId, newCapacity)).isTrue();
		verify(tRepository).update(trip, newCapacity);
	}
}


