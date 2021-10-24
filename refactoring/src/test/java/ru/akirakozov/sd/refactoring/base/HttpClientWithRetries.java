package ru.akirakozov.sd.refactoring.base;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpClientWithRetries {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final int timeout;
    private final int retries;

    public HttpClientWithRetries(int retries, int timeout) {
        this.retries = retries;
        this.timeout = timeout;
    }

    public String sendGetRequest(String host, int port, String method, Map<String, Object> params) {
        String paramsString = params.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));
        String uriContent = "http://" + host + ":" + port + "/" + method + (params.isEmpty() ? "" : "?" + paramsString);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uriContent))
                .build();
        for (int i = 0; i < retries; i++) {
            try {
                return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
            } catch (ConnectException ignored) {
                try {
                    Thread.sleep(timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                return "";
            }
        }
        return "";
    }
}
