import manager.Manager;

import tasks.epic.Epic;
import tasks.subtask.SubTask;
import tasks.task.Task;
import tasks.Status;

public class Main {
    static Manager manager = new Manager();

    public static void main(String[] args) {
        // Для проверок
        manager.addTask(new Task("Задача 1", "Описание задачи 1"));
        manager.addTask(new Task("Задача 2", "Описание задачи 2"));
        manager.addEpic(new Epic("Эпик 1", "Описание эпика 1"));
        manager.addSubTask(new SubTask("Подзадача 1", "Описание подзадачи 1", 3));
        manager.addSubTask(new SubTask("Подзадача 2", "Описание подзадачи 2", 3));
        manager.addEpic(new Epic("Эпик 2", "Описание эпика 2"));
        manager.addSubTask(new SubTask("Подзадача 1", "Описание подзадачи 1", 6));
        manager.addTask(new Task("Задача 3", "Описание задачи 3"));
        manager.addEpic(new Epic("Эпик 3", "Описание эпика 3"));

        manager.addSubTask(new SubTask("Подзадача 1", "Описание подзадачи 1", 9));
        manager.addSubTask(new SubTask("Подзадача 2", "Описание подзадачи 2", 9));
        manager.addSubTask(new SubTask("Подзадача 3", "Описание подзадачи 3", 9));
        manager.addSubTask(new SubTask("Подзадача 4", "Описание подзадачи 4", 9));
        manager.addSubTask(new SubTask("Подзадача 5", "Описание подзадачи 5", 9));

        manager.addSubTask(new SubTask("Подзадача 2", "Описание подзадачи 2", 6));

        System.out.println(manager.getTaskList());
        System.out.println(manager.getEpicList());
        System.out.println(manager.getSubTaskList());

        manager.addSubTask(new SubTask("Подзадача 6", "Описание подзадачи 6", 9));

        System.out.println(manager.getTaskList());
        System.out.println(manager.getEpicList());
        System.out.println(manager.getSubTaskList());

        manager.updateTask(2, new Task(Status.DONE, null, "Надо шо-то поделать опять"));
        manager.updateSubTask(4, new SubTask(Status.IN_PROGRESS, null, null, 3));
        manager.updateSubTask(5, new SubTask(Status.DONE, "Шо-то сделал", null, 3));
        manager.updateEpic(9, new Epic(null, "Надо шо-то поделать"));

        System.out.println(manager.getTaskList());
        System.out.println(manager.getEpicList());
        System.out.println(manager.getSubTaskList());

        manager.updateSubTask(10, new SubTask(Status.DONE, null, null, 9));

        System.out.println(manager.getTaskList());
        System.out.println(manager.getEpicList());
        System.out.println(manager.getSubTaskList());

        System.out.println(manager.getSubTasksOfEpic(3));

        System.out.println(manager.getTask(2));
        System.out.println(manager.getEpic(9));
        System.out.println(manager.getSubTask(10));

        manager.deleteTask(1);
        manager.deleteEpic(3);
        manager.deleteSubTask(13);

        System.out.println(manager.getTaskList());
        System.out.println(manager.getEpicList());
        System.out.println(manager.getSubTaskList());

        manager.clearTaskList();

        manager.clearSubTaskList();

        manager.clearEpicList();

    }
}
