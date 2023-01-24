package ru.yandex.practicum.agolykh.kanban.managers.file;

import ru.yandex.practicum.agolykh.kanban.tasks.*;

import java.time.Duration;
import java.util.Optional;

public class Converter {
    public static Task taskFromString(String value) {
        Task result;
        String[] elements = value.split(",");
        if (elements[5].equals("null")) {
            result = new Task(
                    Enum.valueOf(Status.class, elements[2]),
                    elements[3],
                    elements[4]);
        } else {
            result = new Task(
                    Enum.valueOf(Status.class, elements[2]),
                    elements[3],
                    elements[4],
                    Integer.parseInt(elements[5]),
                    elements[6]);
        }
        result.setId(Integer.parseInt(elements[0]));
        return result;
    }

    public static Epic epicFromString(String value) {
        String[] elements = value.split(",");
        Epic result = new Epic(
                Enum.valueOf(Status.class, elements[2]),
                elements[3],
                elements[4]);
        result.setId(Integer.parseInt(elements[0]));
        return result;
    }

    public static SubTask subTaskFromString(String value) {
        SubTask result;
        String[] elements = value.split(",");
        if (elements[6].equals("null")) {
            result = new SubTask(
                    Enum.valueOf(Status.class, elements[2]),
                    elements[3],
                    elements[4],
                    Integer.parseInt(elements[5]));
        } else {
            result = new SubTask(
                    Enum.valueOf(Status.class, elements[2]),
                    elements[3],
                    elements[4],
                    Integer.parseInt(elements[5]),
                    Integer.parseInt(elements[6]),
                    elements[7]);
        }
        result.setId(Integer.parseInt(elements[0]));
        return result;
    }

    public static String taskToString(Task task) {
        Long optionalDuration = Optional.ofNullable(task.getDuration())
                .map(Duration::toMinutes).orElse(null);
        String optionalStartTime = Optional.ofNullable(task.getStartTime())
                .map((time) -> time.format(Task.getFormatter())).orElse(null);
        String optionalEndTime = Optional.ofNullable(task.getEndTime())
                .map((time) -> time.format(Task.getFormatter())).orElse(null);

        StringBuilder result = new StringBuilder()
                .append(task.getId()).append(',')
                .append(task.getType()).append(',')
                .append(task.getStatus()).append(',')
                .append(task.getName()).append(',')
                .append(task.getDescription()).append(',')
                .append(optionalDuration).append(',')
                .append(optionalStartTime).append(',')
                .append(optionalEndTime).append(',');
        return result.append("\n").toString();
    }

    public static String subTaskToString(SubTask subTask) {
        Long optionalDuration = Optional.ofNullable(subTask.getDuration())
                .map(Duration::toMinutes).orElse(null);
        String optionalStartTime = Optional.ofNullable(subTask.getStartTime())
                .map((time) -> time.format(Task.getFormatter())).orElse(null);
        String optionalEndTime = Optional.ofNullable(subTask.getEndTime())
                .map((time) -> time.format(Task.getFormatter())).orElse(null);

        StringBuilder result = new StringBuilder()
                .append(subTask.getId()).append(',')
                .append(subTask.getType()).append(',')
                .append(subTask.getStatus()).append(',')
                .append(subTask.getName()).append(',')
                .append(subTask.getDescription()).append(',')
                .append(subTask.getEpicId()).append(',')
                .append(optionalDuration).append(',')
                .append(optionalStartTime).append(',')
                .append(optionalEndTime).append(',');

        return result.append("\n").toString();
    }
}
