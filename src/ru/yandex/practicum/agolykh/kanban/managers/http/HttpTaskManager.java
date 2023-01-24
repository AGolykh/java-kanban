package ru.yandex.practicum.agolykh.kanban.managers.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.yandex.practicum.agolykh.kanban.managers.file.FileBackedTaskManager;
import ru.yandex.practicum.agolykh.kanban.managers.http.adapters.DurationAdapter;
import ru.yandex.practicum.agolykh.kanban.managers.http.adapters.LocalDateTimeAdapter;
import ru.yandex.practicum.agolykh.kanban.tasks.Epic;
import ru.yandex.practicum.agolykh.kanban.tasks.SubTask;
import ru.yandex.practicum.agolykh.kanban.tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

public class HttpTaskManager extends FileBackedTaskManager {
    KVTaskClient kvTaskClient;
    Gson gson;

    public HttpTaskManager(String host) {
        kvTaskClient = new KVTaskClient(host);
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }

    public void load() {
        if (kvTaskClient.load("tasks") != null) {
            ArrayList<Task> tasksFromServer = gson.fromJson(kvTaskClient.load("Tasks"),
                    new TypeToken<ArrayList<Task>>() {
                    }.getType());
            for (Task task : tasksFromServer) {
                super.taskList.put(task.getId(), task);
            }
        }

        if (kvTaskClient.load("epics") != null) {
            ArrayList<Epic> epicsFromServer = gson.fromJson(kvTaskClient.load("Epics"),
                    new TypeToken<ArrayList<Epic>>() {
                    }.getType());
            for (Epic epic : epicsFromServer) {
                super.epicList.put(epic.getId(), epic);
            }

            if (kvTaskClient.load("subtasks") != null) {
                ArrayList<SubTask> subTasksFromServer = gson.fromJson(kvTaskClient.load("SubTasks"),
                        new TypeToken<ArrayList<SubTask>>() {
                        }.getType());
                for (SubTask subTask : subTasksFromServer) {
                    super.subTaskList.put(subTask.getId(), subTask);
                }
            }
        }

        if (kvTaskClient.load("history") == null) {
            return;
        }
        ArrayList<Integer> historyFromServer = gson.fromJson(kvTaskClient.load("history"),
                new TypeToken<ArrayList<Integer>>() {
                }.getType());
        for (int i = historyFromServer.size() - 1; i >= 0; i--) {
            if (taskList.containsKey(historyFromServer.get(i))) {
                super.history.add(taskList.get(historyFromServer.get(i)));
            } else if (epicList.containsKey(historyFromServer.get(i))) {
                super.history.add(epicList.get(historyFromServer.get(i)));
            } else if (subTaskList.containsKey(historyFromServer.get(i))) {
                super.history.add(subTaskList.get(historyFromServer.get(i)));
            }
        }
    }

    @Override
    public void save() {
        ArrayList<Integer> history = new ArrayList<>();
        for (Task task : super.getHistory()) {
            history.add(task.getId());
        }
        kvTaskClient.put("tasks", gson.toJson(super.getTaskList()));
        kvTaskClient.put("epics", gson.toJson(super.getEpicList()));
        kvTaskClient.put("subtasks", gson.toJson(super.getSubTaskList()));
        kvTaskClient.put("history", gson.toJson(history));
    }

    @Override
    public ArrayList<Task> getTaskList() {
        load();
        return super.getTaskList();
    }

    @Override
    public ArrayList<Epic> getEpicList() {
        load();
        return super.getEpicList();
    }

    @Override
    public ArrayList<SubTask> getSubTaskList() {
        load();
        return super.getSubTaskList();
    }

    @Override
    public ArrayList<SubTask> getSubTasksOfEpic(int id) {
        load();
        return super.getSubTasksOfEpic(id);
    }

    @Override
    public Task getTask(int id) {
        load();
        return super.getTask(id);
    }

    @Override
    public Epic getEpic(int id) {
        load();
        return super.getEpic(id);
    }

    @Override
    public SubTask getSubTask(int id) {
        load();
        return super.getSubTask(id);
    }

    @Override
    public void addTask(Task task) {
        load();
        super.addTask(task);
    }

    @Override
    public void addEpic(Epic epic) {
        load();
        super.addEpic(epic);
    }

    @Override
    public void addSubTask(SubTask subTask) {
        load();
        super.addSubTask(subTask);
    }

    @Override
    public void updateTask(Task newTask) {
        load();
        super.updateTask(newTask);
    }

    @Override
    public void updateEpic(Epic newEpic) {
        load();
        super.updateEpic(newEpic);
    }

    @Override
    public void updateSubTask(SubTask newSubTask) {
        load();
        super.updateSubTask(newSubTask);
    }

    @Override
    public void deleteTask(int id) {
        load();
        super.deleteTask(id);
    }

    @Override
    public void deleteEpic(int id) {
        load();
        super.deleteEpic(id);
    }

    @Override
    public void deleteSubTask(int id) {
        load();
        super.deleteSubTask(id);
    }


    @Override
    public void clearTaskList() {
        load();
        super.clearTaskList();
    }

    @Override
    public void clearEpicList() {
        load();
        super.clearEpicList();
    }

    @Override
    public void clearSubTaskList() {
        load();
        super.clearSubTaskList();
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        load();
        return super.getPrioritizedTasks();
    }

    @Override
    public ArrayList<Task> getHistory() {
        load();
        return super.getHistory();
    }
}
