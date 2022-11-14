package ru.yandex.practicum.agolykh.kanban.managers;

import ru.yandex.practicum.agolykh.kanban.managers.history.HistoryManager;
import ru.yandex.practicum.agolykh.kanban.managers.history.InMemoryHistoryManager;
import ru.yandex.practicum.agolykh.kanban.managers.task.InMemoryTaskManager;
import ru.yandex.practicum.agolykh.kanban.managers.task.TaskManager;

public class Managers {

    private Managers() {

    }
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
