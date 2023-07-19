package delft;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import org.junit.jupiter.api.*;

class Employee {

	private final long id;
	private final String name;
	private final String job;
	private final int wage;
	private final int age;
	private final String restaurantName;

	public Employee(long id, String name, String job, int wage, int age, String restaurantName) {
		this.id = id;
		this.name = name;
		this.job = job;
		this.wage = wage;
		this.age = age;
		this.restaurantName = restaurantName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Employee)) {
			return false;
		}
		Employee employee = (Employee) o;
		return getId() == employee.getId() && getWage() == employee.getWage() &&
			getAge() == employee.getAge() &&
			getRestaurantName().equals(employee.getRestaurantName()) &&
			getName().equals(employee.getName()) &&
			getJob().equals(employee.getJob());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getName(), getJob(), getWage(), getAge(), getRestaurantName());
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getJob() {
		return job;
	}

	public int getWage() {
		return wage;
	}

	public int getAge() {
		return age;
	}

	public String getRestaurantName() {
		return restaurantName;
	}

}

class EmployeeDao {

	private final Connection connection;

	public EmployeeDao(Connection connection) {
		this.connection = connection;
	}

	public List<Employee> all() {
		try {
			PreparedStatement ps = connection.prepareStatement("select * from employee");

			ResultSet rs = ps.executeQuery();

			List<Employee> allEmployees = new ArrayList<>();
			while (rs.next()) {
				allEmployees.add(new Employee(rs.getLong("id"), rs.getString("name"),
					rs.getString("job"), rs.getInt("wage"), rs.getInt("age"),
					rs.getString("restaurant_name")));
			}

			return allEmployees;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void save(Employee employee) {
		try {
			PreparedStatement ps = connection.prepareStatement(
				"insert into employee (id, name, job, wage, age, restaurant_name) values (?,?,?,?,?,?)");
			ps.setLong(1, employee.getId());
			ps.setString(2, employee.getName());
			ps.setString(3, employee.getJob());
			ps.setInt(4, employee.getWage());
			ps.setInt(5, employee.getAge());
			ps.setString(6, employee.getRestaurantName());
			ps.execute();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<String> getEmployeesFromRestaurant() {
		try {
			PreparedStatement ps = connection.prepareStatement(
				"select e.name from employee as e join restaurant as r on e.restaurant_name = r.name where r.name like 'P%' and e.age < 35");
			ResultSet rs = ps.executeQuery();
			List<String> allEmployees = new ArrayList<>();
			while (rs.next()) {
				allEmployees.add(rs.getString("name"));
			}
			return allEmployees;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

class Restaurant {
	private final long id;
	private final String name;
	private final String cuisine;
	private final double rating;
	private final String location;

	public Restaurant(long id, String name, String cuisine, double rating, String location) {
		this.id = id;
		this.name = name;
		this.cuisine = cuisine;
		this.rating = rating;
		this.location = location;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Restaurant restaurant = (Restaurant) o;
		return id == restaurant.getId() && rating == restaurant.rating &&
			name.equals(restaurant.name) &&
			cuisine.equals(restaurant.cuisine) &&
			location.equals(restaurant.location);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, cuisine, rating, location);
	}

	@Override
	public String toString() {
		return "Restaurant{" +
			"name='" + name + '\'' +
			", cuisine=" + cuisine + '\'' +
			", rating='" + rating + '\'' +
			", location='" + location + '\'' +
			'}';
	}

	public double getRating() {
		return rating;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCuisine() {
		return cuisine;
	}

	public String getLocation() {
		return location;
	}
}

class RestaurantDao {

	private final Connection connection;

	public RestaurantDao(Connection connection) {
		this.connection = connection;
	}

	public void save(Restaurant restaurant) {
		try {
			PreparedStatement ps = connection.prepareStatement(
				"insert into restaurant (id, name, cuisine, rating, location) values (?,?,?,?,?)");
			ps.setLong(1, restaurant.getId());
			ps.setString(2, restaurant.getName());
			ps.setString(3, restaurant.getCuisine());
			ps.setDouble(4, restaurant.getRating());
			ps.setString(5, restaurant.getLocation());
			ps.execute();
			connection.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

class EmployeeDaoTemplate {
	protected static final String DB_CONNECTION = "jdbc:h2:mem:test" + random() + ";DB_CLOSE_DELAY=-1";
	protected static final String DB_USER = "";
	protected static final String DB_PASSWORD = "";

	protected EmployeeDao employeeDao;
	protected RestaurantDao restaurantDao;
	
	protected Connection connection;

	private static void createTable(String createQuery, Connection connection) throws SQLException {
		PreparedStatement createPreparedStatement = connection.prepareStatement(createQuery);
		createPreparedStatement.execute();
		connection.commit();
	}

	private static int random() {
		return new Random().nextInt();
	}

	@BeforeEach
	void openConnectionAndCleanup() throws SQLException {
		connection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
		createTable(
			"create table if not exists restaurant (id int primary key, name varchar(255), " +
				"cuisine varchar(255), rating float, location varchar(255))", connection);
		createTable(
			"create table if not exists employee (id int primary key, name varchar(255), " +
				"job varchar(255), wage int, age int, restaurant_name varchar(255))", connection);
		employeeDao = new EmployeeDao(connection);
		restaurantDao = new RestaurantDao(connection);

		connection.prepareStatement("truncate table restaurant").execute();
		connection.prepareStatement("truncate table employee").execute();
	}

	@AfterEach
	void close() throws SQLException {
		connection.close();
	}

}
