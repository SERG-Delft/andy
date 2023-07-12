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

	public void save(Employee employee) throws SQLException {
		try {
			PreparedStatement ps = connection.prepareStatement(
				"insert into employee (id, name, job, wage, age, restaurant_name) values (?,?,?,?,?,?)");
			ps.setLong(1, employee.getId());
			ps.setString(2, employee.getName());
			ps.setInt(3, employee.getWage());
			ps.setInt(4, employee.getAge());
			ps.setString(5, employee.getRestaurantName());
			ps.execute();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

class Recipe {
	private final String name;
	private final int prepTime;
	private final boolean vegan;
	private final boolean desert;
	private final int price;
	private final long id;

	public Recipe(long id, int prepTime, int price, boolean vegan, boolean desert, String name) {
		this.id = id;
		this.name = name;
		this.prepTime = prepTime;
		this.price = price;
		this.vegan = vegan;
		this.desert = desert;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Recipe recipe = (Recipe) o;
		return id == recipe.getId() && prepTime == recipe.prepTime && price == recipe.price &&
			vegan == recipe.vegan && desert == recipe.desert &&
			name.equals(recipe.name);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public String toString() {
		return "Recipe{" +
			"name='" + name + '\'' +
			", preparation time=" + prepTime + '\'' +
			", price='" + price + '\'' +
			", is vegan='" + vegan + '\'' +
			", is desert='" + desert + '\'' +
			'}';
	}

	public long getId() {
		return id;
	}

	public int getPrepTime() {
		return prepTime;
	}

	public int getPrice() {
		return price;
	}

	public String getName() {
		return name;
	}

	public boolean getVegan() {
		return vegan;
	}

	public boolean getDesert() {
		return desert;
	}
}

class RecipeDao {

	private final Connection connection;

	public RecipeDao(Connection connection) {
		this.connection = connection;
	}

	public List<Recipe> all() {
		try {
			PreparedStatement ps = connection.prepareStatement("select * from recipe");
			ResultSet rs = ps.executeQuery();
			List<Recipe> allRecipes = new ArrayList<>();
			while (rs.next()) {
				allRecipes.add(new Recipe(rs.getLong("id"), rs.getInt("prepTime"),
					rs.getInt("price"),
					rs.getBoolean("vegan"), rs.getBoolean("dessert"),
					rs.getString("name")));
			}
			return allRecipes;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void save(Recipe recipe) {
		try {
			PreparedStatement ps = connection.prepareStatement(
				"insert into recipe (id, name, prepTime, vegan, desert, price) values (?,?,?,?,?,?)");
			ps.setLong(1, recipe.getId());
			ps.setString(2, recipe.getName());
			ps.setInt(3, recipe.getPrepTime());
			ps.setBoolean(4, recipe.getVegan());
			ps.setBoolean(5, recipe.getDesert());
			ps.setInt(6, recipe.getPrice());
			ps.execute();
			connection.commit();
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
		return super.hashCode();
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

	public List<Restaurant> all() {
		try {
			PreparedStatement ps = connection.prepareStatement("select * from restaurant");
			ResultSet rs = ps.executeQuery();
			List<Restaurant> allRestaurants = new ArrayList<>();
			while (rs.next()) {
				allRestaurants.add(
					new Restaurant(rs.getLong("id"), rs.getString("name"), rs.getString("cuisine"),
						rs.getDouble("rating"), rs.getString("location")));
			}
			return allRestaurants;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<String> getAverageRestaurant() {
		try {
			PreparedStatement ps = connection.prepareStatement(
				"select name from restaurant where rating > 6.5 and rating <= 8 group by name order by name");
			ResultSet rs = ps.executeQuery();
			List<String> allRestaurants = new ArrayList<>();
			while (rs.next()) {
				allRestaurants.add(rs.getString("name"));
			}
			return allRestaurants;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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

class RestaurantDaoTemplate {

	protected static final String DB_CONNECTION = "jdbc:h2:mem:test" + random() + ";DB_CLOSE_DELAY=-1";
	protected static final String DB_USER = "";
	protected static final String DB_PASSWORD = "";

	protected EmployeeDao employeeDao;
	protected RestaurantDao restaurantDao;
	protected RecipeDao recipeDao;

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
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println(e);
		}
		connection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);

		createTable(
			"create table if not exists restaurant (id int primary key, name varchar(255), " +
				"cuisine varchar(255), rating float, location varchar(255))", connection);
		createTable(
			"create table if not exists employee (id int primary key, name varchar(255), " +
				"job varchar(255), wage int, age int, restaurant_name varchar(255))", connection);
		createTable(
			"create table if not exists recipe (id int primary key, name varchar(255), " +
				"prep_time int, vegan bool, desert bool, price int)", connection);
		employeeDao = new EmployeeDao(connection);
		restaurantDao = new RestaurantDao(connection);
		recipeDao = new RecipeDao(connection);

		connection.prepareStatement("truncate table restaurant").execute();
		connection.prepareStatement("truncate table employee").execute();
		connection.prepareStatement("truncate table recipe").execute();
	}

	@AfterEach
	void close() throws SQLException {
		connection.close();
	}

}
