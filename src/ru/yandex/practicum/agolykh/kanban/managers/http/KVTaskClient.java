package ru.yandex.practicum.agolykh.kanban.managers.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final HttpClient client;
    private final String API_TOKEN;
    private final String host;

    public KVTaskClient(String host) {
        this.host = host;
        this.client = HttpClient.newHttpClient();
        this.API_TOKEN = requestToken();
    }

    private String requestToken() {
        String endpoint = "/register";
        URI url = URI.create(this.host + endpoint);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по URL-адресу: '" + url + "', возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        } catch (NullPointerException e) {
            System.out.println("Тело ответа пустое.");
        }
        return null;
    }

    public void put(String key, String json) {
        URI url = URI.create(this.host + "/save/" + key +"?API_TOKEN=" + this.API_TOKEN);
        HttpRequest request = HttpRequest.newBuilder().uri(url).
                POST(HttpRequest.BodyPublishers.ofString(json)).build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по URL-адресу: '" + url + "', возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    public String load(String key) {
        URI url = URI.create(this.host + "/load/" + key +"?API_TOKEN=" + this.API_TOKEN);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по URL-адресу: '" + url + "', возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        } catch (NullPointerException e) {
            System.out.println("Тело ответа пустое.");
        }
        return null;
    }
}
