package ru.yandex.practicum.agolykh.kanban.managers.task;

import ru.yandex.practicum.agolykh.kanban.tasks.*;

public class Converter {
    public static Task taskFromString(String value) {
        String[] elements = value.split(",");
        Task result = new Task(
                Status.of(elements[2]),
                elements[3],
                elements[4]);
        result.setId(Integer.parseInt(elements[0]));
        return result;
    }

    public static Epic epicFromString(String value) {
        String[] elements = value.split(",");
        Epic result = new Epic(
                Status.of(elements[2]),
                elements[3],
                elements[4]);
        result.setId(Integer.parseInt(elements[0]));
        return result;
    }

    public static SubTask subTaskFromString(String value) {
        String[] elements = value.split(",");
        SubTask result = new SubTask(
                Status.of(elements[2]),
                elements[3],
                elements[4],
                Integer.parseInt(elements[5]));
        result.setId(Integer.parseInt(elements[0]));
        return result;
    }

    public static String taskToString(Task task) {
        return String.valueOf(task.getId()) + ',' +
                task.getType() + ',' +
                task.getStatus() + ',' +
                task.getName() + ',' +
                task.getDescription() + "\n";
    }

    public static String subTaskToString(SubTask subTask) {
        return String.valueOf(subTask.getId()) + ',' +
                subTask.getType() + ',' +
                subTask.getStatus() + ',' +
                subTask.getName() + ',' +
                subTask.getDescription() + ',' +
                subTask.getEpicId() + "\n";
    }
}
