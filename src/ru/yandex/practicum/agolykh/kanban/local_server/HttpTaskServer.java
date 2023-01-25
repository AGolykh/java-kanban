package ru.yandex.practicum.agolykh.kanban.local_server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.agolykh.kanban.adapters.DurationAdapter;
import ru.yandex.practicum.agolykh.kanban.adapters.LocalDateTimeAdapter;
import ru.yandex.practicum.agolykh.kanban.managers.Managers;
import ru.yandex.practicum.agolykh.kanban.managers.TaskManager;
import ru.yandex.practicum.agolykh.kanban.tasks.Epic;
import ru.yandex.practicum.agolykh.kanban.tasks.SubTask;
import ru.yandex.practicum.agolykh.kanban.tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class HttpTaskServer {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public static TaskManager manager;
    private final HttpServer taskServer;
    private final int port;
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
//            .registerTypeAdapter(Epic.class, new EpicAdapter())
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    public HttpTaskServer(int port) throws IOException {
        this.port = port;
        manager = Managers.getDefault("http://localhost:8078");
        taskServer = HttpServer.create(new InetSocketAddress("localhost", this.port), 0);
        taskServer.createContext("/tasks", new TasksHandler());
    }

    public TaskManager getManager() {
        return manager;
    }

    public void start() {
        System.out.println("Запускаем сервер задач на порту " + this.port);
        taskServer.start();
    }

    public void stop() {
        System.out.println("Остановка сервера задач на порту " + this.port);
        taskServer.stop(0);
    }

    private static class TasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestMethod = exchange.getRequestMethod();
            String[] pathElements = exchange.getRequestURI().getPath().split("/");
            switch (requestMethod) {
                case "GET" -> {
                    if (pathElements.length == 2) {
                        getPrioritized(exchange);
                        return;
                    }
                    switch (pathElements[2]) {
                        case "task" -> getAllTasks(exchange);
                        case "epic" -> getAllEpics(exchange);
                        case "subtask" -> getAllSubTasks(exchange);
                        case "history" -> getHistory(exchange);
                        default -> writeResponse(exchange, "Неверный запрос к серверу", 404);
                    }
                }
                case "POST" -> {
                    switch (pathElements[2]) {
                        case "task" -> addTask(exchange);
                        case "epic" -> addEpic(exchange);
                        case "subtask" -> addSubTask(exchange);
                        default -> writeResponse(exchange, "Неверный запрос к серверу", 404);
                    }
                }
                case "DELETE" -> {
                    switch (pathElements[2]) {
                        case "task" -> deleteAllTasks(exchange);
                        case "epic" -> deleteAllEpics(exchange);
                        case "subtask" -> deleteAllSubTasks(exchange);
                        default -> writeResponse(exchange, "Неверный запрос к серверу", 404);
                    }
                }
                default -> writeResponse(exchange, "Неверный запрос к серверу", 404);
            }
        }

        private void getAllTasks(HttpExchange exchange) throws IOException {
            if (exchange.getRequestURI().getQuery() != null) {
                getTask(exchange);
                return;
            }
            try {
                writeResponse(exchange, gson.toJson(manager.getTaskList()), 200);
            } catch (NullPointerException e) {
                writeResponse(exchange, e.getMessage(), 400);
            }
        }

        private void getAllEpics(HttpExchange exchange) throws IOException {
            if (exchange.getRequestURI().getQuery() != null) {
                getEpic(exchange);
                return;
            }
            try {
                writeResponse(exchange, gson.toJson(manager.getEpicList()), 200);
            } catch (NullPointerException e) {
                writeResponse(exchange, e.getMessage(), 400);
            }
        }


        private void getAllSubTasks(HttpExchange exchange) throws IOException {
            if (exchange.getRequestURI().getQuery() != null) {
                String[] pathElements = exchange.getRequestURI().getPath().split("/");
                if ("epic".equals(pathElements[3])) {
                    getSubTaskOfEpic(exchange);
                } else {
                    getSubTask(exchange);
                }
                return;
            }
            try {
                writeResponse(exchange, gson.toJson(manager.getSubTaskList()), 200);
            } catch (NullPointerException e) {
                writeResponse(exchange, e.getMessage(), 400);
            }
        }

        private void getTask(HttpExchange exchange) throws IOException {
            Optional<Integer> id = getId(exchange);
            if (id.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор задачи.", 400);
                return;
            }
            try {
                writeResponse(exchange, gson.toJson(manager.getTask(id.get())), 200);
            } catch (NullPointerException e) {
                writeResponse(exchange, e.getMessage(), 400);
            }
        }

        private void getEpic(HttpExchange exchange) throws IOException {
            Optional<Integer> id = getId(exchange);
            if (id.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор задачи.", 400);
                return;
            }
            try {
                writeResponse(exchange, gson.toJson(manager.getEpic(id.get())), 200);
            } catch (NullPointerException e) {
                writeResponse(exchange, e.getMessage(), 400);
            }
        }

        private void getSubTask(HttpExchange exchange) throws IOException {
            Optional<Integer> id = getId(exchange);
            if (id.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор задачи.", 400);
                return;
            }
            try {
                writeResponse(exchange, gson.toJson(manager.getSubTask(id.get())), 200);
            } catch (NullPointerException e) {
                writeResponse(exchange, e.getMessage(), 400);
            }
        }

        private void deleteTask(HttpExchange exchange) throws IOException {
            Optional<Integer> id = getId(exchange);
            if (id.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор задачи.", 400);
                return;
            }
            try {
                manager.deleteTask(id.get());
                writeResponse(exchange, "Задача " + id.get() + " удалена.", 200);
            } catch (NullPointerException e) {
                writeResponse(exchange, e.getMessage(), 400);
            }
        }

        private void addTask(HttpExchange exchange) throws IOException {
            Task task = gson.fromJson(new String(exchange
                    .getRequestBody()
                    .readAllBytes(), DEFAULT_CHARSET), Task.class);
            if (task.getId() != null) {
                for (Task taskFromManager : manager.getTaskList()) {
                    if (task.getId().equals(taskFromManager.getId())) {
                        updateTask(exchange, task);
                        return;
                    }
                }
            }
            manager.addTask(task);
            if (manager.getTaskList().contains(task)) {
                writeResponse(exchange, "Задача добавлена.", 201);
            }
        }

        private void addEpic(HttpExchange exchange) throws IOException {
            Epic epic = gson.fromJson(new String(exchange
                    .getRequestBody()
                    .readAllBytes(), DEFAULT_CHARSET), Epic.class);
            if (epic.getId() != null) {
                for (Epic epicFromList : manager.getEpicList()) {
                    if (epic.getId().equals(epicFromList.getId())) {
                        updateEpic(exchange, epic);
                        return;
                    }
                }
            }
            manager.addEpic(epic);
            if (manager.getEpicList().contains(epic)) {
                writeResponse(exchange, "Родительская задача добавлена.", 201);
            }
        }

        private void addSubTask(HttpExchange exchange) throws IOException {
            SubTask subTask = gson.fromJson(new String(exchange
                    .getRequestBody()
                    .readAllBytes(), DEFAULT_CHARSET), SubTask.class);
            if (subTask.getId() != null) {
                for (SubTask subTaskFromList : manager.getSubTaskList()) {
                    if (subTask.getId().equals(subTaskFromList.getId())) {
                        updateSubTask(exchange, subTask);
                        return;
                    }
                }
            }
            manager.addSubTask(subTask);
            if (manager.getSubTaskList().contains(subTask)) {
                writeResponse(exchange, "Подзадача добавлена.", 201);
            }
        }

        private void updateTask(HttpExchange exchange, Task newTask) throws IOException {
            manager.updateTask(newTask);
            writeResponse(exchange, "Задача обновлена.", 201);
        }

        private void updateEpic(HttpExchange exchange, Epic newEpic) throws IOException {
            manager.updateEpic(newEpic);
            writeResponse(exchange, "Родительская задача обновлена.", 201);
        }

        private void updateSubTask(HttpExchange exchange, SubTask newSubTask) throws IOException {
            manager.updateSubTask(newSubTask);
            writeResponse(exchange, "Подзадача обновлена.", 201);
        }

        private void deleteEpic(HttpExchange exchange) throws IOException {
            Optional<Integer> id = getId(exchange);
            if (id.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор задачи.", 400);
                return;
            }
            try {
                manager.deleteEpic(id.get());
                writeResponse(exchange, "Родительская задача " + id.get() + " удалена.", 200);
            } catch (NullPointerException e) {
                writeResponse(exchange, e.getMessage(), 400);
            }
        }

        private void deleteSubTask(HttpExchange exchange) throws IOException {
            Optional<Integer> id = getId(exchange);
            if (id.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор задачи.", 400);
                return;
            }
            try {
                manager.deleteSubTask(id.get());
                writeResponse(exchange, "Подзадача " + id.get() + " удалена.", 200);
            } catch (NullPointerException e) {
                writeResponse(exchange, e.getMessage(), 400);
            }
        }

        private void deleteAllTasks(HttpExchange exchange) throws IOException {
            if (exchange.getRequestURI().getQuery() != null) {
                deleteTask(exchange);
                return;
            }
            manager.clearTaskList();
            writeResponse(exchange, "Список задач очищен.", 200);
        }

        private void deleteAllEpics(HttpExchange exchange) throws IOException {
            if (exchange.getRequestURI().getQuery() != null) {
                deleteEpic(exchange);
                return;
            }
            manager.clearEpicList();
            writeResponse(exchange, "Список родительских задач очищен.", 200);
        }

        private void deleteAllSubTasks(HttpExchange exchange) throws IOException {
            if (exchange.getRequestURI().getQuery() != null) {
                deleteSubTask(exchange);
                return;
            }
            manager.clearSubTaskList();
            writeResponse(exchange, "Список подзадач очищен.", 200);
        }


        private void getSubTaskOfEpic(HttpExchange exchange) throws IOException {
            Optional<Integer> id = getId(exchange);
            if (id.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор задачи.", 400);
                return;
            }
            try {
                writeResponse(exchange, gson.toJson(manager.getSubTasksOfEpic(id.get())), 200);
            } catch (NullPointerException e) {
                writeResponse(exchange, e.getMessage(), 400);
            }
        }

        private void getHistory(HttpExchange exchange) throws IOException {
            try {
                writeResponse(exchange, gson.toJson(manager.getHistory()), 200);
            } catch (NullPointerException e) {
                writeResponse(exchange, e.getMessage(), 400);
            }
        }

        private void getPrioritized(HttpExchange exchange) throws IOException {
            try {
                writeResponse(exchange, gson.toJson(manager.getPrioritizedTasks()), 200);
            } catch (NullPointerException e) {
                writeResponse(exchange, e.getMessage(), 400);
            }
        }

        private Optional<Integer> getId(HttpExchange exchange) {
            try {
                return Optional.of(Integer.parseInt(exchange
                        .getRequestURI()
                        .getQuery()
                        .replace("id=", "")
                        .replace("/", "")));
            } catch (NumberFormatException exception) {
                return Optional.empty();
            }
        }

        private void writeResponse(HttpExchange exchange,
                                   String responseString,
                                   int responseCode) throws IOException {
            if (responseString.isBlank()) {
                exchange.sendResponseHeaders(responseCode, 0);
            } else {
                byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
                exchange.sendResponseHeaders(responseCode, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            }
            exchange.close();
        }
    }
}

