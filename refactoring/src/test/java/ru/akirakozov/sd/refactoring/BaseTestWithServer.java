package ru.akirakozov.sd.refactoring;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class BaseTestWithServer {
    private final ExecutorService serverExecutor = Executors.newSingleThreadExecutor();

    @BeforeClass
    public void startServer() {
        serverExecutor.submit(() -> {
            try {
                Main.main(new String[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @AfterClass(alwaysRun = true)
    public void stopServer() {
        serverExecutor.shutdown();
    }

    @AfterMethod(alwaysRun = true)
    public void cleanDatabase() {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM PRODUCT");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
