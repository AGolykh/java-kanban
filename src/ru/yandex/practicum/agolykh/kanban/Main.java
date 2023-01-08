package ru.yandex.practicum.agolykh.kanban;

import ru.yandex.practicum.agolykh.kanban.exceptions.ManagerSaveException;
import ru.yandex.practicum.agolykh.kanban.managers.task.FileBackedTaskManager;
import ru.yandex.practicum.agolykh.kanban.managers.task.TaskManager;
import ru.yandex.practicum.agolykh.kanban.tasks.Epic;
import ru.yandex.practicum.agolykh.kanban.tasks.SubTask;
import ru.yandex.practicum.agolykh.kanban.tasks.Task;
import ru.yandex.practicum.agolykh.kanban.tasks.Status;

import java.io.File;
import java.util.Set;

public class Main {
    static TaskManager taskManager;
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        taskManager = new FileBackedTaskManager();
                //Managers.getDefault();

        // Для проверок
        taskManager.addTask(new Task("Задача 1", "Описание задачи 1", 45, "14.01.2023 08:30"));
        taskManager.addTask(new Task("Задача 2", "Описание задачи 2", 45, "14.01.2023 09:30"));
        taskManager.addEpic(new Epic("Эпик 1", "Описание эпика 1"));
        taskManager.addSubTask(new SubTask("Подзадача 1", "Описание подзадачи 1", 3, 45, "14.01.2023 20:30"));
        taskManager.addSubTask(new SubTask("Подзадача 2", "Описание подзадачи 2", 3, 45, "14.01.2023 19:30"));
        taskManager.addSubTask(new SubTask("Подзадача 28", "Описание подзадачи 28", 3, 45, "14.01.2023 19:30"));
        taskManager.addEpic(new Epic("Эпик 2", "Описание эпика 2"));
        taskManager.addSubTask(new SubTask("Подзадача 3", "Описание подзадачи 3", 6, 45, "14.01.2023 18:30"));
        taskManager.addSubTask(new SubTask("Подзадача 4", "Описание подзадачи 4", 6));
        taskManager.addTask(new Task("Задача 3", "Описание задачи 3"));
        taskManager.addEpic(new Epic("Эпик 3", "Описание эпика 3"));
        taskManager.addSubTask(new SubTask("Подзадача 5", "Описание подзадачи 5", 10, 45, "14.01.2023 16:30"));
        taskManager.addSubTask(new SubTask("Подзадача 6", "Описание подзадачи 6", 10, 45, "14.01.2023 13:30"));
        taskManager.addSubTask(new SubTask("Подзадача 7", "Описание подзадачи 7", 10, 45, "14.01.2023 12:30"));
        taskManager.addSubTask(new SubTask("Подзадача 8", "Описание подзадачи 8", 10, 45, "14.01.2023 11:30"));
        taskManager.addSubTask(new SubTask("Подзадача 9", "Описание подзадачи 9", 10, 45, "14.01.2023 10:30"));
        taskManager.addSubTask(new SubTask("Подзадача 10", "Описание подзадачи 10", 10, 45, "14.01.2023 07:30"));

        System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getEpicList());
        System.out.println(taskManager.getSubTaskList());

        System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getEpicList());
        System.out.println(taskManager.getSubTaskList());

        taskManager.updateTask(2, new Task(Status.DONE, null, "Надо шо-то поделать опять"));
        taskManager.updateSubTask(4, new SubTask(Status.IN_PROGRESS, null, null, 3));
        taskManager.updateSubTask(5, new SubTask(Status.DONE, "Шо-то сделал", null, 3));
        taskManager.updateEpic(10, new Epic(null, "Надо шо-то поделать"));

        System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getEpicList());
        System.out.println(taskManager.getSubTaskList());

        taskManager.updateSubTask(11, new SubTask(Status.DONE, null, null, 10));

        System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getEpicList());
        System.out.println(taskManager.getSubTaskList());

        System.out.println(taskManager.getSubTasksOfEpic(3));

        // Проверка истории
        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getEpic(3));
        System.out.println(taskManager.getSubTask(11)); // 1 элемент в истории
        System.out.println(taskManager.getTask(2));
        System.out.println(taskManager.getEpic(6));
        System.out.println(taskManager.getSubTask(12));
        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getEpic(3));
        System.out.println(taskManager.getSubTask(13));
        System.out.println(taskManager.getTask(2));
        System.out.println(taskManager.getEpic(10));
        System.out.println(taskManager.getSubTask(14));
        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getEpic(3));
        System.out.println(taskManager.getSubTask(11));
        System.out.println(taskManager.getTask(2));
        System.out.println(taskManager.getEpic(6));
        System.out.println(taskManager.getSubTask(12));
        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getEpic(3));
        System.out.println(taskManager.getSubTask(13));
        System.out.println(taskManager.getTask(2));
        System.out.println(taskManager.getEpic(10));
        System.out.println(taskManager.getSubTask(14));
        System.out.println(taskManager.getSubTask(14));
        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getEpic(3));
        System.out.println(taskManager.getSubTask(11));
        System.out.println(taskManager.getTask(2));
        System.out.println(taskManager.getEpic(6));
        System.out.println(taskManager.getSubTask(12));
        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getEpic(3));
        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getTask(2));
        System.out.println(taskManager.getSubTask(7));
        System.out.println(taskManager.getSubTask(8));
        System.out.println(taskManager.getSubTask(7));
        System.out.println(taskManager.getSubTask(13));
        System.out.println(taskManager.getSubTask(15));
        System.out.println(taskManager.getSubTask(13));
        System.out.println(taskManager.getSubTask(16));
        System.out.println(taskManager.getSubTask(12));
        System.out.println(taskManager.getSubTask(13));

        System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getEpicList());
        System.out.println(taskManager.getSubTaskList());
        System.out.println(taskManager.getHistory());

        taskManager.deleteEpic(10);
        System.out.println(taskManager.getHistory());
        Set<Task> setTask = taskManager.getPrioritizedTasks();

        final String dir = System.getProperty("user.dir") + "\\resources\\";
        final String fileName = "Tasks.csv"; // Тут можно заменить на Tasks.csv
        final String path = dir + fileName;
        TaskManager fromFile;
        try {
            fromFile = FileBackedTaskManager.loadFromFile(new File(path));
            System.out.println(fromFile.getTaskList());
            System.out.println(fromFile.getEpicList());
            System.out.println(fromFile.getSubTaskList());
            System.out.println(fromFile.getHistory());
            fromFile.addSubTask(new SubTask("Подзадача 10", "Описание подзадачи 6", 3, 45, "14.01.2023 08:30"));
            System.out.println(fromFile.getEpic(3));
            System.out.println(fromFile.getSubTask(4));
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }


        Task taskers = new Task("Новая", "Jgbcfybt");
        System.out.println(taskers);

        Task taskers2 = new Task("Задача 1", "Описание задачи 1", 45, "14.01.2023 08:30");
        System.out.println(taskers2);
        long time = System.currentTimeMillis() - startTime;
        System.out.println(time);
    }
}
