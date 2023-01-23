package ru.yandex.practicum.agolykh.kanban;

import ru.yandex.practicum.agolykh.kanban.http.KVServer;
import ru.yandex.practicum.agolykh.kanban.managers.Managers;
import ru.yandex.practicum.agolykh.kanban.managers.task.FileBackedTaskManager;
import ru.yandex.practicum.agolykh.kanban.managers.task.TaskManager;
import ru.yandex.practicum.agolykh.kanban.tasks.Epic;
import ru.yandex.practicum.agolykh.kanban.tasks.SubTask;
import ru.yandex.practicum.agolykh.kanban.tasks.Task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        TaskManager fileTaskManager = FileBackedTaskManager
                .loadFromFile(new File(System
                        .getProperty("user.dir") + "\\resources\\Normal.csv"));

        KVServer kvServer;
        try {
            kvServer = new KVServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        kvServer.start();

        TaskManager httpManager = Managers.getHttpTaskManager("http://localhost:8078");
        httpManager.addTask(fileTaskManager.getTask(1));

        for (Task task : fileTaskManager.getTaskList()) {
            httpManager.addTask(task);
        }

        for (Epic epic : fileTaskManager.getEpicList()) {
            epic.listSubTaskId.clear();
            httpManager.addEpic(epic);
        }

        for (SubTask subTask : fileTaskManager.getSubTaskList()) {
            httpManager.addSubTask(subTask);
        }


        httpManager.addSubTask(new SubTask("Подзадача 10", "Описание подзадачи 10", 6, 45, "14.01.2023 07:30"));

        System.out.println(httpManager.getTask(1));
        System.out.println(httpManager.getEpic(3));
        System.out.println(httpManager.getSubTask(4));

        System.out.println(httpManager.getTaskList());
        System.out.println(httpManager.getEpicList());
        System.out.println(httpManager.getSubTaskList());
        System.out.println(httpManager.getSubTasksOfEpic(3));
        Task task = fileTaskManager.getTask(1);
        task.setName("Замененная задача");
        httpManager.updateTask(task);
        Epic epic = fileTaskManager.getEpic(3);
        epic.setName("Замененная родительская задача");
        httpManager.updateEpic(epic);
        SubTask subTask = fileTaskManager.getSubTask(5);
        subTask.setName("Замененная подзадача");
        httpManager.updateSubTask(subTask);
        ArrayList<Task> tasks = httpManager.getTaskList();
        httpManager.deleteTask(2);
        ArrayList<Epic> epics = httpManager.getEpicList();
        httpManager.deleteEpic(6);
        ArrayList<SubTask> subTasks = httpManager.getSubTaskList();
        httpManager.deleteSubTask(17);

        System.out.println(httpManager.getHistory());
        System.out.println(httpManager.getPrioritizedTasks());


/*        httpManager.clearTaskList();
        httpManager.clearSubTaskList();
        httpManager.clearEpicList();*/

        long time = System.currentTimeMillis() - startTime;
        System.out.println(time);
    }
}
