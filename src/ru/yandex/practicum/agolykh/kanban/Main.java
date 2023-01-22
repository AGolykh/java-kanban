package ru.yandex.practicum.agolykh.kanban;

import ru.yandex.practicum.agolykh.kanban.http.KVServer;
import ru.yandex.practicum.agolykh.kanban.managers.Managers;
import ru.yandex.practicum.agolykh.kanban.managers.task.FileBackedTaskManager;
import ru.yandex.practicum.agolykh.kanban.managers.task.KVTaskClient;
import ru.yandex.practicum.agolykh.kanban.managers.task.TaskManager;
import ru.yandex.practicum.agolykh.kanban.tasks.Epic;
import ru.yandex.practicum.agolykh.kanban.tasks.SubTask;
import ru.yandex.practicum.agolykh.kanban.tasks.Task;

import java.io.File;
import java.io.IOException;

public class Main {
    static TaskManager taskManager;
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        TaskManager fileTaskManager = FileBackedTaskManager
                .loadFromFile(new File(System
                        .getProperty("user.dir") + "\\resources\\Normal.csv"));

        TaskManager httpManager = Managers.getHttpTaskManager("http://localhost:8080");
        KVServer kvServer = null;
        try {
            kvServer = new KVServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        kvServer.start();

        KVTaskClient client = new KVTaskClient("http://localhost:8078");
        System.out.println(client.getAPI_TOKEN());

        Task task = fileTaskManager.getTask(1);
        task.setName("Замененная задача");
        httpManager.updateTask(task);
        Epic epic = fileTaskManager.getEpic(3);
        epic.setName("Замененная родительская задача");
        httpManager.updateEpic(epic);
        SubTask subTask = fileTaskManager.getSubTask(5);
        subTask.setName("Замененная подзадача");
        httpManager.updateSubTask(subTask);

/*        ArrayList<Task> tasks = httpManager.getTaskList();
        httpManager.deleteTask(1);
        ArrayList<Epic> epics = httpManager.getEpicList();
        httpManager.deleteEpic(6);
        ArrayList<SubTask> subTasks = httpManager.getSubTaskList();
        httpManager.deleteSubTask(17);
        ArrayList<SubTask> subsTasksOfEpic = httpManager.getSubTasksOfEpic(3);
        httpManager.clearTaskList();
        httpManager.clearSubTaskList();
        httpManager.clearEpicList();

        Set<Task> prioritySetFile = fileTaskManager.getPrioritizedTasks();
        Set<Task> prioritySetHttp = httpManager.getPrioritizedTasks();
        System.out.println(httpManager.getPrioritizedTasks());
        System.out.println(httpManager.getHistory());

        httpManager.addTask(fileTaskManager.getTask(1));
        httpManager.addEpic(fileTaskManager.getEpic(3));
        httpManager.deleteSubTask(17);
        httpManager.addSubTask(fileTaskManager.getSubTask(17));*/


        long time = System.currentTimeMillis() - startTime;
        System.out.println(time);
    }
}
