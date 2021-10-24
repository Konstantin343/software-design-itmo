package ru.akirakozov.sd.refactoring.server;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.akirakozov.sd.refactoring.base.BaseTestWithServer;
import ru.akirakozov.sd.refactoring.base.HttpClientWithRetries;

import java.util.Map;

public class ProductsServerTest extends BaseTestWithServer {
    private final HttpClientWithRetries httpClient = new HttpClientWithRetries(3, 1000);

    @Test
    public void addOneProduct() {
        Assert.assertEquals(addProduct("phone", 12345),
                "OK\n");
    }
    
    @Test
    public void getProductsEmpty() {
        Assert.assertEquals(getProducts(),
                "<html><body>\n" +
                        "</body></html>\n");
    }

    @Test
    public void addAndGetProducts() {
        addProduct("TV", 54321);
        addProduct("Phone", 12345);
        Assert.assertEquals(getProducts(),
                "<html><body>\n" +
                        "TV\t54321</br>\n" +
                        "Phone\t12345</br>\n" +
                        "</body></html>\n");
    }

    @DataProvider(name = "commands")
    public Object[][] commands() {
        return new Object[][]{
                new Object[]{"sum", "<html><body>\n" +
                        "Summary price: \n" +
                        "100\n" +
                        "</body></html>\n"},
                new Object[]{"count", "<html><body>\n" +
                        "Number of products: \n" +
                        "2\n" +
                        "</body></html>\n"},
                new Object[]{"max", "<html><body>\n" +
                        "<h1>Product with max price: </h1>\n" +
                        "TV\t60</br>\n" +
                        "</body></html>\n"},
                new Object[]{"min", "<html><body>\n" +
                        "<h1>Product with min price: </h1>\n" +
                        "Phone\t40</br>\n" +
                        "</body></html>\n"},
                new Object[]{"unknown-command", "Unknown command: unknown-command\n"},
        };
    }

    @Test(dataProvider = "commands")
    public void queryTest(String command, String result) {
        addProduct("Phone", 40);
        addProduct("TV", 60);
        Assert.assertEquals(query(command), result);
    }

    private String addProduct(String name, long price) {
        return sendRequest("add-product", Map.of(
                "name", name,
                "price", price
        ));
    }

    private String getProducts() {
        return sendRequest("get-products", Map.of());
    }

    private String query(String command) {
        return sendRequest("query", Map.of(
                "command", command
        ));
    }

    private String sendRequest(String method, Map<String, Object> params) {
        return httpClient.sendGetRequest(HOST, PORT, method, params);
    }
}
