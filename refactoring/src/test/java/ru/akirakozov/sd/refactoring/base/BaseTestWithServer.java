package ru.akirakozov.sd.refactoring.base;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import ru.akirakozov.sd.refactoring.server.ProductsServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class BaseTestWithServer {
    protected final static String HOST = "localhost";
    protected final static int PORT = 8081;
    protected final static String DB_CONNECTION = "jdbc:sqlite:test.db";
    
    private final ExecutorService serverExecutor = Executors.newSingleThreadExecutor();
    private final ProductsServer productsServer = new ProductsServer(PORT, DB_CONNECTION);

    @BeforeClass
    public void startServer() {
        serverExecutor.submit(() -> {
            try {
                productsServer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @AfterClass(alwaysRun = true)
    public void stopServer() throws Exception {
        productsServer.stop();
        serverExecutor.shutdown();
    }

    @AfterMethod(alwaysRun = true)
    public void cleanDatabase() {
        try (Connection connection = DriverManager.getConnection(DB_CONNECTION)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM PRODUCT");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
