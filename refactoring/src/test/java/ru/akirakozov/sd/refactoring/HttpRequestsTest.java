package ru.akirakozov.sd.refactoring;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequestsTest extends BaseTestWithServer {
    private final static String HOST = "localhost";
    private final static int PORT = 8081;

    private final HttpClient httpClient = HttpClient.newHttpClient();

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
        String paramsString = params.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));
        String uriContent = "http://" + HOST + ":" + PORT + "/" + method + (params.isEmpty() ? "" : "?" + paramsString);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uriContent))
                .build();
        while (true) {
            try {
                return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
            } catch (ConnectException ignored) {
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                return "";
            }
        }
    }
}
