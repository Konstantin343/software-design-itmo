package ru.akirakozov.sd.refactoring.database;

import ru.akirakozov.sd.refactoring.products.Product;
import ru.akirakozov.sd.refactoring.products.ProductsManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductsDatabaseManager implements ProductsManager {
    private final Connection connection;

    public ProductsDatabaseManager(String connectionString) throws SQLException {
        connection = DriverManager.getConnection(connectionString);
    }

    @Override
    public List<Product> getProducts() {
        return executeQuery("SELECT * FROM PRODUCT", resultSet -> {
            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                products.add(parseProduct(resultSet));
            }
            return products;
        });
    }

    @Override
    public void createProducts() {
        executeUpdate("CREATE TABLE IF NOT EXISTS PRODUCT" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " PRICE          INT     NOT NULL)");
    }

    @Override
    public void addProduct(Product product) {
        executeUpdate("INSERT INTO PRODUCT " +
                "(NAME, PRICE) VALUES (\"" + product.getName() + "\"," + product.getPrice() + ")");
    }

    @Override
    public Product maxPrice() {
        return executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1", resultSet -> {
            resultSet.next();
            return parseProduct(resultSet);
        });
    }

    @Override
    public Product minPrice() {
        return executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1", resultSet -> {
            resultSet.next();
            return parseProduct(resultSet);
        });
    }

    @Override
    public Long sumPrice() {
        return executeQuery("SELECT SUM(price) FROM PRODUCT", resultSet -> {
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
            return null;
        });
    }

    @Override
    public Integer countProducts() {
        return executeQuery("SELECT COUNT(*) FROM PRODUCT", resultSet -> {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return null;
        });
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }

    private void executeUpdate(String query) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T executeQuery(String query, SqlFunction<ResultSet, T> action) {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            return action.apply(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    private Product parseProduct(ResultSet resultSet) throws SQLException {
        String name = resultSet.getString("name");
        int price = resultSet.getInt("price");
        return new Product(name, price); 
    }

    @FunctionalInterface
    private interface SqlFunction<T, R> {
        R apply(T t) throws SQLException;
    }
}
