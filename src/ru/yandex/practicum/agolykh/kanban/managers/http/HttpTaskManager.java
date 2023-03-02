package ru.yandex.practicum.agolykh.kanban.managers.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.yandex.practicum.agolykh.kanban.adapters.DurationAdapter;
import ru.yandex.practicum.agolykh.kanban.adapters.EpicAdapter;
import ru.yandex.practicum.agolykh.kanban.adapters.LocalDateTimeAdapter;
import ru.yandex.practicum.agolykh.kanban.client.KVTaskClient;
import ru.yandex.practicum.agolykh.kanban.managers.file.FileBackedTaskManager;
import ru.yandex.practicum.agolykh.kanban.tasks.Epic;
import ru.yandex.practicum.agolykh.kanban.tasks.SubTask;
import ru.yandex.practicum.agolykh.kanban.tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class HttpTaskManager extends FileBackedTaskManager {
    private final KVTaskClient kvTaskClient;
    private final Gson gson;

    public HttpTaskManager(String host) {
        kvTaskClient = new KVTaskClient(host);
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Epic.class, new EpicAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .setPrettyPrinting()
                .serializeNulls()
                .create();
        load();
    }

    public void load() {
        String jsonTasks = kvTaskClient.load("tasks");
        String jsonEpics = kvTaskClient.load("epics");
        String jsonSubTasks = kvTaskClient.load("subtasks");
        String jsonHistory = kvTaskClient.load("history");

        if (jsonTasks != null) {
            ArrayList<Task> tasksFromServer =
                    gson.fromJson(jsonTasks, new TypeToken<ArrayList<Task>>() {}.getType());
            for (Task task : tasksFromServer) {
                taskList.put(task.getId(), task);
            }
        }

        if (jsonEpics != null) {
            ArrayList<Epic> epicsFromServer =
                    gson.fromJson(jsonEpics, new TypeToken<ArrayList<Epic>>() {
                    }.getType());
            for (Epic epic : epicsFromServer) {
                if (epic == null) {
                    break;
                }
                epicList.put(epic.getId(), epic);
            }
        }

        if (jsonSubTasks != null) {
            ArrayList<SubTask> subTasksFromServer =
                    gson.fromJson(jsonSubTasks, new TypeToken<ArrayList<SubTask>>() {
                    }.getType());
            for (SubTask subTask : subTasksFromServer) {
                addSubTask(subTask);
            }
        }


        if (jsonHistory == null) {
            return;
        }
        ArrayList<Integer> historyFromServer =
                gson.fromJson(jsonHistory, new TypeToken<ArrayList<Integer>>() {}.getType());
        for (int i = historyFromServer.size() - 1; i >= 0; i--) {
            if (taskList.containsKey(historyFromServer.get(i))) {
                history.add(taskList.get(historyFromServer.get(i)));
            } else if (epicList.containsKey(historyFromServer.get(i))) {
                history.add(epicList.get(historyFromServer.get(i)));
            } else if (subTaskList.containsKey(historyFromServer.get(i))) {
                history.add(subTaskList.get(historyFromServer.get(i)));
            }
        }
    }

    @Override
    public void save() {
        ArrayList<Integer> history = new ArrayList<>();
        for (Task task : getHistory()) {
            history.add(task.getId());
        }
        kvTaskClient.put("tasks", gson.toJson(getTaskList()));
        kvTaskClient.put("epics", gson.toJson(getEpicList()));
        kvTaskClient.put("subtasks", gson.toJson(getSubTaskList()));
        kvTaskClient.put("history", gson.toJson(history));
    }
}
