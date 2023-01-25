package ru.yandex.practicum.agolykh.kanban;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.yandex.practicum.agolykh.kanban.adapters.DurationAdapter;
import ru.yandex.practicum.agolykh.kanban.adapters.EpicAdapter;
import ru.yandex.practicum.agolykh.kanban.adapters.LocalDateTimeAdapter;
import ru.yandex.practicum.agolykh.kanban.local_server.HttpTaskServer;
import ru.yandex.practicum.agolykh.kanban.remote_server.KVServer;
import ru.yandex.practicum.agolykh.kanban.tasks.Epic;
import ru.yandex.practicum.agolykh.kanban.tasks.Status;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(Epic.class, new EpicAdapter())
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        epic.setStatus(Status.IN_PROGRESS);
        epic.setId(1);
        epic.setDuration(Duration.ofMinutes(45));
        epic.getListSubTaskId().add(2);
        epic.getListSubTaskId().add(3);
        System.out.println(epic);
        String json = gson.toJson(epic);
        String setJson = gson.toJson(epic.getListSubTaskId());

        Set<Integer> set = gson.fromJson(setJson, new TypeToken<HashSet<Integer>>() {}.getType());
        Epic epicDes = (gson.fromJson(json, Epic.class));

        System.out.println(epicDes);

        KVServer kvServer = new KVServer();
        kvServer.start();

        HttpTaskServer httpTaskServer = new HttpTaskServer(8080);
        httpTaskServer.start();

        long time = System.currentTimeMillis() - startTime;
        System.out.println(time);
    }
}
