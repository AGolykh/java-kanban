package ru.yandex.practicum.agolykh.kanban.managers.task;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final HttpClient client;
    String host;
    String API_TOKEN;
    Gson gson = new Gson();
    public KVTaskClient(String host) {
        this.host = host;
        this.client = HttpClient.newHttpClient();
        this.API_TOKEN = getToken();
    }

    public String getAPI_TOKEN() {
        return API_TOKEN;
    }

    private String getToken() {
        HttpResponse<String> response = null;
        String endpoint = "/register";
        URI url = URI.create(this.host + endpoint);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по URL-адресу: '" + url + "', возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
        return response.body();
    }
}
