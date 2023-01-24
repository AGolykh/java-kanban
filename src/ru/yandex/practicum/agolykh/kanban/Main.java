package ru.yandex.practicum.agolykh.kanban;

import ru.yandex.practicum.agolykh.kanban.managers.http.HttpTaskServer;
import ru.yandex.practicum.agolykh.kanban.server.KVServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();

        KVServer kvServer = new KVServer();
        kvServer.start();

        HttpTaskServer httpTaskServer = new HttpTaskServer(8080);
        httpTaskServer.start();

        httpTaskServer.stop();
        kvServer.stop();

        long time = System.currentTimeMillis() - startTime;
        System.out.println(time);
    }
}
