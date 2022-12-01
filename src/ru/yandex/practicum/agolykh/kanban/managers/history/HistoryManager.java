package ru.yandex.practicum.agolykh.kanban.managers.history;

import ru.yandex.practicum.agolykh.kanban.tasks.Task;

import java.util.ArrayList;

public interface HistoryManager {

    void add(Task task);

    void remove(int id);

    ArrayList<Task> getHistory();
}
