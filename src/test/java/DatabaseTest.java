import db.Product;
import db.ProductDAO;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

public class DatabaseTest {
    private final String USERNAME_KEY = "datasource.username";
    private final String PASSWORD_KEY = "datasource.password";

    ProductDAO dao = new ProductDAO(getUsername(), getPassword());

    private String getUsername() {
        try (InputStream input = getClass().getResourceAsStream("database.properties")) {
            final Properties properties = new Properties();
            properties.load(input);

            System.out.println(properties.getProperty(USERNAME_KEY));

            return properties.getProperty(USERNAME_KEY);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getPassword() {
        try (InputStream input = getClass().getResourceAsStream("database.properties")) {
            final Properties properties = new Properties();
            properties.load(input);

            return properties.getProperty(PASSWORD_KEY);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Before
    public void clear() {
        dao.clear();
    }

    @Test
    public void testList() {
        final int size = 5;
        final ArrayList<Product> listToLoad = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            listToLoad.add(new Product(i + 1, "tovar " + i, i * 10));
            dao.add(listToLoad.get(i));
        }

        final ArrayList<Product> dataBaseList = dao.list();

        assert dataBaseList.size() == size;

        for (int i = 0; i < size; i++) {
            assert dataBaseList.get(i).equals(listToLoad.get(i));
        }
    }

    @Test
    public void testAdding() {
        final Product toAdd = new Product(1, "PRODUCT_TITLE", 500);

        dao.add(toAdd);

        assert dao.getByTitle(toAdd.getTitle()).equals(toAdd);
    }

    @Test
    public void testPriceChanging() {
        final Product toAdd = new Product(1, "PRODUCT_TITLE", 500);
        final int newPrice = 1000;

        dao.add(toAdd);

        dao.changePrice(toAdd.getTitle(), newPrice);

        assert dao.getByTitle(toAdd.getTitle()).getCost() == newPrice;
    }

    @Test
    public void testFilterByPrice() {
        final int size = 15;
        final ArrayList<Product> listToLoad = new ArrayList<>();
        final int h = 10;
        for (int i = 0; i < size; i++) {
            listToLoad.add(new Product(i + 1, "tovar " + i, i * h));
            dao.add(listToLoad.get(i));
        }

        final int from = 30;
        final int to = 90;
        final ArrayList<Product> filtered = dao.filterByPrice(from, to);
        final int indexStart = from / h;
        final int indexEnd = to / h + 1;

        for (int i = indexStart; i < indexEnd; i++) {
            assert listToLoad.get(i).equals(filtered.get(i - indexStart));
        }
    }
}
