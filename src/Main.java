import Tasks.*;

public class Main {
    static TaskList taskList = new TaskList();
    public static void main(String[] args) {
    // Для проверок
        taskList.addTask(new Task("Задача 1", "Описание задачи 1"));
        taskList.addTask(new Task("Задача 2", "Описание задачи 2"));
        taskList.addTask(new Epic("Эпик 1", "Описание эпика 1"));
        taskList.addTask(new SubTask(3, "Подзадача 1", "Описание подзадача 1"));
        taskList.addTask(new SubTask(3, "Подзадача 2", "Описание подзадача 2"));
        taskList.addTask(new Epic("Эпик 2", "Описание эпика 2"));
        taskList.addTask(new SubTask(4, "Подзадача 1", "Описание подзадачи 1"));
        taskList.addTask(new Epic("Эпик 3", "Описание эпика 3"));

        taskList.addTask(new SubTask(5, "Подзадача 1", "Описание подзадачи 1"));
        taskList.addTask(new SubTask(5, "Подзадача 2", "Описание подзадачи 2"));
        taskList.addTask(new SubTask(5, "Подзадача 3", "Описание подзадачи 3"));
        taskList.addTask(new SubTask(5, "Подзадача 4", "Описание подзадачи 4"));

        System.out.println(taskList.showList());

        taskList.update(3002,  new SubTask(3, ProgressType.IN_PROGRESS));
        taskList.update(4001,  new SubTask(4, ProgressType.DONE));

        System.out.println(taskList.showList());

        taskList.delTask(3001);
        taskList.delTask(4);

        System.out.println(taskList.showList());

        taskList.addTask(new SubTask(3, "ШОТО", "Надо шото поделать"));
        taskList.addTask(new Task("Шото 2","Снова шото надо поделать"));
        taskList.addTask(new SubTask(5, "Подзадача 5", "Описание подзадачи 5"));
        System.out.println(taskList.showList());

        taskList.delTask(5001);

        taskList.delTask(5005);

        taskList.addTask(new SubTask(5, "Подзадача 6", "Описание подзадачи 6"));
        System.out.println(taskList.showList());

        System.out.println(taskList.getTask(1));
        System.out.println(taskList.getTask(3002));

        System.out.println(taskList.subList(3));

        taskList.clear();

        System.out.println(taskList.showList());
    }

}
