package delft;

import java.time.LocalDate;
import java.util.List;

class ElementNotFoundException extends RuntimeException {
}

enum Destination {
	GREECE, MALTA, TAHITI, ZANZIBAR
}

class Info {

	private LocalDate startDate;

	private LocalDate endDate;

	private Destination destination;

	public Info(LocalDate startDate, LocalDate endDate, Destination destination) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.destination = destination;
	}
}

class Trip {

	private Long id;

	private String name;

	private Info info;

	private Integer price;

	private Integer capacity;

	public Trip(String name, Info info, Integer price, Integer capacity) {
		this.name = name;
		this.info = info;
		this.price = price;
		this.capacity = capacity;
	}

	public Integer getCapacity() {
		return capacity;
	}
}

interface TripRepository {

	Trip getTripById(Long id) throws ElementNotFoundException;

	void update(Trip trip, int capacity);
}

class Person {

	private String firstName;

	private String middleName;

	private String lastName;

	private LocalDate dateOfBirth;

	public Person(String firstName, String middleName, String lastName, LocalDate dateOfBirth) {
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
	}
}

class Reservation {

	private String code;

	private Trip trip;

	private List<Person> people;

	public Reservation(Trip trip, List<Person> people) {
		this.trip = trip;
		this.people = people;
	}

	public List<Person> getPeople() {
		return people;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Reservation that = (Reservation) o;
		if (code != null ? !code.equals(that.code) : that.code != null)
			return false;
		if (trip != null ? !trip.equals(that.trip) : that.trip != null)
			return false;
		return people != null ? people.equals(that.people) : that.people == null;
	}
}

interface ReservationRepository {

	void save(Reservation reservation);

	List<Reservation> getAllReservationsByTrip(Trip trip);
}

class SoftWhere {

	private final TripRepository tRepository;

	private final ReservationRepository rRepository;

	public SoftWhere(TripRepository tRepository, ReservationRepository rRepository) {
		this.tRepository = tRepository;
		this.rRepository = rRepository;
	}

	/**
	 * Checks the number of people who have reserved a trip.
	 *
	 * @param trip
	 *            the trip
	 * @return the number of people who have signed up for this trip
	 */
	private int numberOfReservations(Trip trip) {
		return rRepository.getAllReservationsByTrip(trip).stream().map(r -> r.getPeople().size()).reduce(0,
				Integer::sum);
	}

	/**
	 * Edits the trip capacity to any number that is higher or equal to the number
	 * of current reservations.
	 *
	 * @param id
	 *            the id of the trip
	 * @param capacity
	 *            the new capacity for that trip
	 * @return true if the update was successful
	 */
	public boolean editTripCapacity(Long id, int capacity) {
		try {
			Trip trip = tRepository.getTripById(id);
			if (numberOfReservations(trip) > capacity)
				return false;
			tRepository.update(trip, capacity);
			return true;
		} catch (ElementNotFoundException e) {
			return false;
		}
	}
}
