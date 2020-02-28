package db;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.*;
import java.util.ArrayList;

public class ProductDAO {
    private final String TABLE_NAME = "products";
    private final String BASE_URL = "jdbc:mysql://localhost/LAB4?serverTimezone=Europe/Moscow";

    MysqlDataSource source;

    public ProductDAO(String username, String password) {
        source = new MysqlDataSource();
        source.setURL(BASE_URL);
        source.setUser(username);
        source.setPassword(password);

        try (Connection connection = source.getConnection()) {
            if (!tableExists(connection)) {
                createTable(connection);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database", e);
        }

    }

    public void add(Product product) {
        try (Connection connection = source.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO " + TABLE_NAME + " " +
                     "(prodid, title, cost) VALUES" + " " +
                     "(?, ?, ?)")) {
            statement.setString(1, product.getProdid());
            statement.setString(2, product.getTitle());
            statement.setInt(3, product.getCost());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Failed adding product", e);
        }
    }

    public Product getByTitle(String title) {
        try (Connection connection = source.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE title = ?");
            statement.setString(1, title);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return extractProduct(resultSet);
                } else {
                    throw new SQLException("No such element.");
                }
            } catch (SQLException e) {
                throw new RuntimeException("No such element.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed retrieving products.");
        }
    }

    public void deleteByName(String title) {
        try (Connection connection = source.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM " + TABLE_NAME + " WHERE title = ?");
            statement.setString(1, title);
            if (statement.executeUpdate() == 0) {
                throw new RuntimeException("No such product.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed adding product", e);
        }
    }

    public ArrayList<Product> filterByPrice(int from, int to) {
        if ((from < 0) || (to < 0) || (to < from)) {
            throw new IllegalArgumentException("Invalid price.");
        }

        try (Connection connection = source.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + TABLE_NAME +
                    " WHERE cost BETWEEN ? AND ?");
            statement.setInt(1, from);
            statement.setInt(2, to);

            ResultSet res = statement.executeQuery();

            ArrayList<Product> list = new ArrayList<>();

            while (res.next()) {
                int id = res.getInt("id");
                String prodid = res.getString("prodid");
                String title = res.getString("title");
                int cost = res.getInt("cost");
                list.add(new Product(id, prodid, title, cost));
            }

            return list;

        } catch (SQLException e) {
            throw new RuntimeException("Failed filtering", e);
        }
    }

    public void changePrice(String title, int newPrice) {
        if (newPrice < 0) {
            throw new RuntimeException("Price can not be negative");
        }

        try (Connection connection = source.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE " + TABLE_NAME +
                    " SET cost = ? WHERE title = ?");
            statement.setInt(1, newPrice);
            statement.setString(2, title);
            if (statement.executeUpdate() == 0) {
                throw new RuntimeException("No such element.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed changing price", e);
        }
    }

    public ArrayList<Product> list() {
        try (Connection connection = source.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            ArrayList<Product> list = new ArrayList<>();
            while (res.next()) {
                int id = res.getInt("id");
                String prodid = res.getString("prodid");
                String title = res.getString("title");
                int cost = res.getInt("cost");
                list.add(new Product(id, prodid, title, cost));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to clear table " + TABLE_NAME, e);
        }
    }

    public void clear() {
        try (Connection connection = source.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute("TRUNCATE TABLE " + TABLE_NAME);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to clear table " + TABLE_NAME, e);
        }
    }

    private boolean tableExists(Connection connection) throws SQLException {
        DatabaseMetaData meta = source.getConnection().getMetaData();
        ResultSet res = meta.getTables(null, null, TABLE_NAME, new String[]{"TABLE"});
        while (res.next()) {
            if (res.getString("TABLE_NAME").equals(TABLE_NAME)) {
                return true;
            }
        }
        return false;
    }

    private void createTable(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE " + TABLE_NAME + "(id INT NOT NULL AUTO_INCREMENT, " +
                "prodid VARCHAR(36), " +
                "title VARCHAR(40), " +
                "cost INT NOT NULL, " +
                "PRIMARY KEY(id), " +
                "UNIQUE(prodid), " +
                "UNIQUE(title))");
    }

    private Product extractProduct(ResultSet resultSet) {
        try {
            return new Product(resultSet.getInt("id"),
                    resultSet.getString("prodid"),
                    resultSet.getString("title"),
                    resultSet.getInt("cost"));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve data from set.");
        }
    }
}
