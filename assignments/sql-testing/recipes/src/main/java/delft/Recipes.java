package delft;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;


class Recipe {
	private final long id;
	private final String name;
	private final int prepTime;
	private final boolean vegan;
	private final boolean dessert;
	private final int price;

	public Recipe(long id, int prepTime, int price, boolean vegan, boolean dessert, String name) {
		this.id = id;
		this.name = name;
		this.prepTime = prepTime;
		this.price = price;
		this.vegan = vegan;
		this.dessert = dessert;
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
			vegan == recipe.vegan && dessert == recipe.dessert &&
			name.equals(recipe.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, prepTime, vegan, dessert, price);
	}

	@Override
	public String toString() {
		return "Recipe{" +
			"name='" + name + '\'' +
			", preparation time=" + prepTime + '\'' +
			", price='" + price + '\'' +
			", is vegan='" + vegan + '\'' +
			", is dessert='" + dessert + '\'' +
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

	public boolean getdessert() {
		return dessert;
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

	public int getQuickestVeganDessert() {
		try {
			PreparedStatement ps = connection.prepareStatement("select prepTime from recipe where vegan and dessert group by prepTime order by prepTime limit 1");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				return rs.getInt("prepTime");
			}
			return 0;

		} catch (Exception e){
			throw new RuntimeException(e);
		}
	}

	public void save(Recipe recipe) {
		try {
			PreparedStatement ps = connection.prepareStatement(
				"insert into recipe (id, name, prepTime, vegan, dessert, price) values (?,?,?,?,?,?)");
			ps.setLong(1, recipe.getId());
			ps.setString(2, recipe.getName());
			ps.setInt(3, recipe.getPrepTime());
			ps.setBoolean(4, recipe.getVegan());
			ps.setBoolean(5, recipe.getdessert());
			ps.setInt(6, recipe.getPrice());
			ps.execute();
			connection.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

class RecipeDaoTemplate {
    protected static final String DB_CONNECTION = "jdbc:h2:mem:test" + random() + ";DB_CLOSE_DELAY=-1";
    protected static final String DB_USER = "";
    protected static final String DB_PASSWORD = "";

    protected RecipeDao recipeDao;
    protected Connection connection;

    protected static void createTable(String createQuery, Connection connection) throws SQLException {
        PreparedStatement createPreparedStatement = connection.prepareStatement(createQuery);
        createPreparedStatement.execute();
        connection.commit();
    }

    @BeforeEach
    protected void openConnectionAndCleanup() throws SQLException {
        connection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
        createTable(
                "create table if not exists recipe (id long primary key, name varchar(255), prepTime int, vegan bool, dessert bool, price int)",
                connection);
        recipeDao = new RecipeDao(connection);

        connection.prepareStatement("truncate table recipe").execute();
    }

    @AfterEach
    protected void close() throws SQLException {
        connection.close();
    }

	private static int random() {
		return new Random().nextInt();
	}
}
