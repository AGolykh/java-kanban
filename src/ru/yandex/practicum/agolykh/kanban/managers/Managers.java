package ru.yandex.practicum.agolykh.kanban.managers;

import ru.yandex.practicum.agolykh.kanban.managers.file.FileBackedTaskManager;
import ru.yandex.practicum.agolykh.kanban.managers.history.HistoryManager;
import ru.yandex.practicum.agolykh.kanban.managers.history.InMemoryHistoryManager;
import ru.yandex.practicum.agolykh.kanban.managers.http.HttpTaskManager;
import ru.yandex.practicum.agolykh.kanban.managers.memory.InMemoryTaskManager;

public class Managers {

    private Managers() {}

    public static TaskManager getDefault(String host) {
        return new HttpTaskManager(host);
    }

    public static TaskManager getInMemory() {
        return new InMemoryTaskManager();
    }

    public static TaskManager getFileBacked(String fileName) {
        return new FileBackedTaskManager(fileName);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
