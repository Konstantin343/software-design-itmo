package ru.akirakozov.sd.refactoring;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.akirakozov.sd.refactoring.base.HttpClientWithRetries;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainTest {
    HttpClientWithRetries httpClientWithRetries = new HttpClientWithRetries(3, 1000);
    
    @DataProvider(name = "args")
    public Object[][] args() {
        return new Object[][] {
                new Object[] {new String[] {}, 8081},
                new Object[] {new String[] {"8082"}, 8082},
                new Object[] {new String[] {"8082", "jdbc:sqlite:main-test.db"}, 8082},
        };
    }
    
    @Test(dataProvider = "args")
    public void testMainStart(String[] args, int port) throws IOException {
        String dbFilePath = args.length >= 2 ? args[1].replace("jdbc:sqlite:", "") : "test.db";
        ExecutorService serverExecutor = Executors.newSingleThreadExecutor();
        try {
            serverExecutor.submit(() -> {
                try {
                    Main.main(args);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            Assert.assertTrue(checkServerIsUp(port));
            Assert.assertTrue(new File(dbFilePath).exists());
        } finally {
            serverExecutor.shutdownNow();
            while (true) {
                if (!checkServerIsUp(port)) {
                    break;
                }
            }
            Files.deleteIfExists(Path.of(dbFilePath));
        }
    }
    
    private boolean checkServerIsUp(int port) {
        return !httpClientWithRetries.sendGetRequest("localhost", port, "", Map.of()).isEmpty();
    }
}
