package ru.yandex.practicum.agolykh.kanban.managers.task;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
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
    String host;

    public HttpTaskManager(String host) {
        this.host = host;
        kvTaskClient = new KVTaskClient(this.host);
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new Adapters.LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new Adapters.DurationAdapter())
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }

    public void loadManager() {
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

    public void saveManager() {
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
        loadManager();
        return super.getTaskList();
    }

    @Override
    public ArrayList<Epic> getEpicList() {
        loadManager();
        return super.getEpicList();
    }

    @Override
    public ArrayList<SubTask> getSubTaskList() {
        loadManager();
        return super.getSubTaskList();
    }

    @Override
    public ArrayList<SubTask> getSubTasksOfEpic(int id) {
        loadManager();
        return super.getSubTasksOfEpic(id);
    }

    @Override
    public Task getTask(int id) {
        loadManager();
        Task task = super.getTask(id);
        saveManager();
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        loadManager();
        Epic epic = super.getEpic(id);
        saveManager();
        return epic;
    }

    @Override
    public SubTask getSubTask(int id) {
        loadManager();
        SubTask subTask = super.getSubTask(id);
        saveManager();
        return subTask;
    }

    @Override
    public void addTask(Task task) {
        loadManager();
        super.addTask(task);
        saveManager();
    }

    @Override
    public void addEpic(Epic epic) {
        loadManager();
        super.addEpic(epic);
        saveManager();
    }

    @Override
    public void addSubTask(SubTask subTask) {
        loadManager();
        super.addSubTask(subTask);
        saveManager();
    }

    @Override
    public void updateTask(Task newTask) {
        loadManager();
        super.updateTask(newTask);
        saveManager();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        loadManager();
        super.updateEpic(newEpic);
        saveManager();
    }

    @Override
    public void updateSubTask(SubTask newSubTask) {
        loadManager();
        super.updateSubTask(newSubTask);
        saveManager();
    }

    @Override
    public void deleteTask(int id) {
        loadManager();
        super.deleteTask(id);
        saveManager();
    }

    @Override
    public void deleteEpic(int id) {
        loadManager();
        super.deleteEpic(id);
        saveManager();
    }

    @Override
    public void deleteSubTask(int id) {
        loadManager();
        super.deleteSubTask(id);
        saveManager();
    }


    @Override
    public void clearTaskList() {
        loadManager();
        super.clearTaskList();
        saveManager();
    }

    @Override
    public void clearEpicList() {
        loadManager();
        super.clearEpicList();
        saveManager();
    }

    @Override
    public void clearSubTaskList() {
        loadManager();
        super.clearSubTaskList();
        saveManager();
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        loadManager();
        return super.getPrioritizedTasks();
    }

    @Override
    public ArrayList<Task> getHistory() {
        loadManager();
        return super.getHistory();
    }
}
