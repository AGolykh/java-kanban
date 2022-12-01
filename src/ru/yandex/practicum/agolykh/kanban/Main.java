package ru.yandex.practicum.agolykh.kanban;

import ru.yandex.practicum.agolykh.kanban.managers.Managers;
import ru.yandex.practicum.agolykh.kanban.managers.task.TaskManager;
import ru.yandex.practicum.agolykh.kanban.tasks.Epic;
import ru.yandex.practicum.agolykh.kanban.tasks.SubTask;
import ru.yandex.practicum.agolykh.kanban.tasks.Task;
import ru.yandex.practicum.agolykh.kanban.tasks.Status;

public class Main {
    static TaskManager taskManager;
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        taskManager = Managers.getDefault();

        // Для проверок
        taskManager.addTask(new Task("Задача 1", "Описание задачи 1"));
        taskManager.addTask(new Task("Задача 2", "Описание задачи 2"));
        taskManager.addEpic(new Epic("Эпик 1", "Описание эпика 1"));
        taskManager.addSubTask(new SubTask("Подзадача 1", "Описание подзадачи 1", 3));
        taskManager.addSubTask(new SubTask("Подзадача 2", "Описание подзадачи 2", 3));
        taskManager.addEpic(new Epic("Эпик 2", "Описание эпика 2"));
        taskManager.addSubTask(new SubTask("Подзадача 1", "Описание подзадачи 1", 6));
        taskManager.addSubTask(new SubTask("Подзадача 2", "Описание подзадачи 2", 6));
        taskManager.addTask(new Task("Задача 3", "Описание задачи 3"));
        taskManager.addEpic(new Epic("Эпик 3", "Описание эпика 3"));
        taskManager.addSubTask(new SubTask("Подзадача 1", "Описание подзадачи 1", 10));
        taskManager.addSubTask(new SubTask("Подзадача 2", "Описание подзадачи 2", 10));
        taskManager.addSubTask(new SubTask("Подзадача 3", "Описание подзадачи 3", 10));
        taskManager.addSubTask(new SubTask("Подзадача 4", "Описание подзадачи 4", 10));
        taskManager.addSubTask(new SubTask("Подзадача 5", "Описание подзадачи 5", 10));
        taskManager.addSubTask(new SubTask("Подзадача 6", "Описание подзадачи 6", 10));

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
        System.out.println(taskManager.getSubTask(13));
        System.out.println(taskManager.getSubTask(15));
        System.out.println(taskManager.getSubTask(13));
        System.out.println(taskManager.getSubTask(16));
        System.out.println(taskManager.getSubTask(12));
        System.out.println(taskManager.getSubTask(13));


        taskManager.deleteEpic(10);
        System.out.println(taskManager.getHistory());

        long time = System.currentTimeMillis() - startTime;
        System.out.println(time);
    }
}
