package ru.yandex.practicum.agolykh.kanban.managers.task;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.yandex.practicum.agolykh.kanban.tasks.Epic;
import ru.yandex.practicum.agolykh.kanban.tasks.SubTask;
import ru.yandex.practicum.agolykh.kanban.tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class HttpTaskManager extends FileBackedTaskManager {
    private final String host;
    private final HttpClient client;
    private final Gson gson;

    public HttpTaskManager(String host) {
        this.host = host;
        this.client = HttpClient.newHttpClient();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new Task.LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new Task.DurationAdapter())
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }

    @Override
    public Task getTask(int id) {
        String endpoint = "/tasks/task/?id=" + id;
        return gson.fromJson(sendRequest(endpoint, "GET", null).body(), Task.class);
    }

    @Override
    public Epic getEpic(int id) {
        String endpoint = "/tasks/epic/?id=" + id;
        return gson.fromJson(sendRequest(endpoint, "GET", null).body(), Epic.class);
    }

    @Override
    public SubTask getSubTask(int id) {
        String endpoint = "/tasks/subtask/?id=" + id;
        return gson.fromJson(sendRequest(endpoint, "GET", null).body(), SubTask.class);
    }

    @Override
    public ArrayList<Task> getTaskList() {
        String endpoint = "/tasks/task/";
        return gson.fromJson(sendRequest(endpoint, "GET",null).body(),
                new TypeToken<ArrayList<Task>>(){}.getType());
    }

    @Override
    public ArrayList<Epic> getEpicList() {
        String endpoint = "/tasks/epic/";
        return gson.fromJson(sendRequest(endpoint, "GET",null).body(),
                new TypeToken<ArrayList<Epic>>(){}.getType());
    }

    @Override
    public ArrayList<SubTask> getSubTaskList() {
        String endpoint = "/tasks/subtask/";
        return gson.fromJson(sendRequest(endpoint, "GET",null).body(),
                new TypeToken<ArrayList<SubTask>>(){}.getType());
    }

    @Override
    public ArrayList<SubTask> getSubTasksOfEpic(int id) {
        String endpoint = "/tasks/subtask/epic?id=" + id;
        return gson.fromJson(sendRequest(endpoint, "GET",null).body(),
                new TypeToken<ArrayList<SubTask>>(){}.getType());
    }

    @Override
    public void addTask(Task task) {
        String endpoint = "/tasks/task/";
        System.out.println(gson.toJson(task));
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(task));
        sendRequest(endpoint, "POST", body);
    }

    @Override
    public void addEpic(Epic epic) {
        String endpoint = "/tasks/epic/";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(epic));
        sendRequest(endpoint, "POST", body);
    }

    @Override
    public void addSubTask(SubTask subTask) {
        String endpoint = "/tasks/subtask/";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(subTask));
        sendRequest(endpoint, "POST", body);
    }

    @Override
    public void updateTask(Task newTask) {
        String endpoint = "/tasks/task/";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(newTask));
        sendRequest(endpoint, "POST", body);
    }

    @Override
    public void updateEpic(Epic newEpic) {
        String endpoint = "/tasks/epic/";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(newEpic));
        sendRequest(endpoint, "POST", body);
    }

    @Override
    public void updateSubTask(SubTask newSubTask) {
        String endpoint = "/tasks/subtask/";
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(newSubTask));
        sendRequest(endpoint, "POST", body);
    }

    @Override
    public void deleteTask(int id) {
        String endpoint = "/tasks/task/?id=" + id;
        sendRequest(endpoint, "DELETE", null);
    }

    @Override
    public void deleteEpic(int id) {
        String endpoint = "/tasks/epic/?id=" + id;
        sendRequest(endpoint, "DELETE", null);
    }

    @Override
    public void deleteSubTask(int id) {
        String endpoint = "/tasks/subtask/?id=" + id;
        sendRequest(endpoint, "DELETE", null);
    }

    @Override
    public void clearTaskList() {
        String endpoint = "/tasks/task";
        sendRequest(endpoint, "DELETE", null);
    }

    @Override
    public void clearEpicList() {
        String endpoint = "/tasks/epic";
        sendRequest(endpoint, "DELETE", null);
    }

    @Override
    public void clearSubTaskList() {
        String endpoint = "/tasks/subtask";
        sendRequest(endpoint, "DELETE", null);
    }

    @Override
    public Set<Task> getPrioritizedTasks() { // Сервер формирует множество
        String endpoint = "/tasks";
        return gson.fromJson(sendRequest(endpoint, "GET",null).body(),
                new TypeToken<HashSet<Task>>(){}.getType());
        /*Сервер формирует множество из классов TASK и SUBTASK
        При десериализации получается множество только из TASK,
        как можно сделать так, что бы получаемое множество было таким же, как и отправляемое?*/
    }

    @Override
    public ArrayList<Task> getHistory() {
        String endpoint = "/tasks/history";
        return gson.fromJson(sendRequest(endpoint, "GET",null).body(),
                new TypeToken<ArrayList<Task>>(){}.getType());
    }

    private HttpResponse<String> sendRequest(String endpoint,
                                             String method,
                                             HttpRequest.BodyPublisher body) {
        HttpResponse<String> response = null;
        URI url = URI.create(this.host + endpoint);
        HttpRequest request = null;
        if (method.equals("GET")) {
            request = HttpRequest.newBuilder().uri(url).GET().build();
        } else if (method.equals("POST") && (body != null)) {
            request = HttpRequest.newBuilder().uri(url).POST(body).build();
        } else if (method.equals("DELETE")) {
            request = HttpRequest.newBuilder().uri(url).DELETE().build();
        }
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по URL-адресу: '" + url + "', возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
        return response;
    }
}
