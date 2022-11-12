import manager.Managers;
import manager.task.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.Status;

public class Main {
    static TaskManager taskManager;

    public static void main(String[] args) {
        taskManager = Managers.getDefault();

        // Для проверок
        taskManager.addTask(new Task("Задача 1", "Описание задачи 1"));
        taskManager.addTask(new Task("Задача 2", "Описание задачи 2"));
        taskManager.addEpic(new Epic("Эпик 1", "Описание эпика 1"));
        taskManager.addSubTask(new SubTask("Подзадача 1", "Описание подзадачи 1", 3));
        taskManager.addSubTask(new SubTask("Подзадача 2", "Описание подзадачи 2", 3));
        taskManager.addEpic(new Epic("Эпик 2", "Описание эпика 2"));
        taskManager.addSubTask(new SubTask("Подзадача 1", "Описание подзадачи 1", 6));
        taskManager.addTask(new Task("Задача 3", "Описание задачи 3"));
        taskManager.addEpic(new Epic("Эпик 3", "Описание эпика 3"));

        taskManager.addSubTask(new SubTask("Подзадача 1", "Описание подзадачи 1", 9));
        taskManager.addSubTask(new SubTask("Подзадача 2", "Описание подзадачи 2", 9));
        taskManager.addSubTask(new SubTask("Подзадача 3", "Описание подзадачи 3", 9));
        taskManager.addSubTask(new SubTask("Подзадача 4", "Описание подзадачи 4", 9));
        taskManager.addSubTask(new SubTask("Подзадача 5", "Описание подзадачи 5", 9));

        taskManager.addSubTask(new SubTask("Подзадача 2", "Описание подзадачи 2", 6));

        System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getEpicList());
        System.out.println(taskManager.getSubTaskList());

        taskManager.addSubTask(new SubTask("Подзадача 6", "Описание подзадачи 6", 9));

        System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getEpicList());
        System.out.println(taskManager.getSubTaskList());

        taskManager.updateTask(2, new Task(Status.DONE, null, "Надо шо-то поделать опять"));
        taskManager.updateSubTask(4, new SubTask(Status.IN_PROGRESS, null, null, 3));
        taskManager.updateSubTask(5, new SubTask(Status.DONE, "Шо-то сделал", null, 3));
        taskManager.updateEpic(9, new Epic(null, "Надо шо-то поделать"));

        System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getEpicList());
        System.out.println(taskManager.getSubTaskList());

        taskManager.updateSubTask(10, new SubTask(Status.DONE, null, null, 9));

        System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getEpicList());
        System.out.println(taskManager.getSubTaskList());

        System.out.println(taskManager.getSubTasksOfEpic(3));

        // Проверка истории
        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getEpic(3));
        System.out.println(taskManager.getSubTask(10)); // 1 элемент в истории
        System.out.println(taskManager.getTask(2));
        System.out.println(taskManager.getEpic(6));
        System.out.println(taskManager.getSubTask(11));
        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getEpic(3));
        System.out.println(taskManager.getSubTask(12));
        System.out.println(taskManager.getTask(2));
        System.out.println(taskManager.getEpic(9));
        System.out.println(taskManager.getSubTask(13)); // 10 элемент в списке

        System.out.println(taskManager.getHistory());
    }
}
