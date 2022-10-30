import Tasks.*;

public class Main {
    static Manager manager = new Manager();
    public static void main(String[] args) {
    // Для проверок
        manager.addTask(new Task("Задача 1", "Описание задачи 1"));
        manager.addTask(new Task("Задача 2", "Описание задачи 2"));
        manager.addTask(new Epic("Эпик 1", "Описание эпика 1"));
        manager.addTask(new SubTask("Подзадача 1", "Описание подзадачи 1", 3));
        manager.addTask(new SubTask("Подзадача 2", "Описание подзадачи 2", 3));
        manager.addTask(new Epic("Эпик 2", "Описание эпика 2"));
        manager.addTask(new SubTask("Подзадача 1", "Описание подзадачи 1", 6));
        manager.addTask(new Task("Задача 3", "Описание задачи 3"));
        manager.addTask(new Epic("Эпик 3", "Описание эпика 3"));

        manager.addTask(new SubTask("Подзадача 1", "Описание подзадачи 1", 9));
        manager.addTask(new SubTask("Подзадача 2", "Описание подзадачи 2", 9));
        manager.addTask(new SubTask("Подзадача 3", "Описание подзадачи 3", 9));
        manager.addTask(new SubTask("Подзадача 4", "Описание подзадачи 4", 9));
        manager.addTask(new SubTask("Подзадача 5", "Описание подзадачи 5", 9));

        System.out.println(manager);
        manager.delTask(2);
        manager.delTask(6);
        manager.delTask(11);

        manager.update(3, Status.DONE, null, null);



        System.out.println(manager);

        System.out.println(manager.getSubTaskOfEpic(8));

        //manager.clearTaskList();
        System.out.println(manager);

        System.out.println(manager.showTask());
        System.out.println(manager.showEpic());
        System.out.println(manager.showSubTask());
    }
}
