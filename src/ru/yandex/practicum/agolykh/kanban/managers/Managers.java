package ru.yandex.practicum.agolykh.kanban.managers;

import ru.yandex.practicum.agolykh.kanban.managers.file.FileBackedTaskManager;
import ru.yandex.practicum.agolykh.kanban.managers.history.HistoryManager;
import ru.yandex.practicum.agolykh.kanban.managers.history.InMemoryHistoryManager;
import ru.yandex.practicum.agolykh.kanban.managers.http.HttpTaskManager;
import ru.yandex.practicum.agolykh.kanban.managers.memory.InMemoryTaskManager;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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

    public static Map<LocalDateTime, Boolean> getTimeList(Integer year) {
        Map<LocalDateTime, Boolean> timeList = new HashMap<>();
        LocalDateTime timeStamp = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime lastTimeStamp = LocalDateTime.of(year, 12, 31, 23, 45);
        while(timeStamp.isBefore(lastTimeStamp)) {
            timeList.put(timeStamp, false);
            timeStamp = timeStamp.plusMinutes(15);
        }
        return timeList;
    }
}
