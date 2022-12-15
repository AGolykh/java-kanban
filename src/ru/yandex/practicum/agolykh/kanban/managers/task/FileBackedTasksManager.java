package ru.yandex.practicum.agolykh.kanban.managers.task;

import ru.yandex.practicum.agolykh.kanban.managers.history.HistoryManager;
import ru.yandex.practicum.agolykh.kanban.tasks.Epic;
import ru.yandex.practicum.agolykh.kanban.tasks.SubTask;
import ru.yandex.practicum.agolykh.kanban.tasks.Task;
import ru.yandex.practicum.agolykh.kanban.tasks.TaskTypes;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    private final String dir = System.getProperty("user.dir") + "\\resources\\";
    private final String fileName = "Tasks.csv";
    private final String path = dir + fileName;

    private static class ManagerSaveException extends Exception {
        public ManagerSaveException(final String message) {
            super(message);
        }
    }

    // Создание экземпляра класса с данными из файла
    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager result = new FileBackedTasksManager();
        try (FileReader reader = new FileReader(file, StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(reader)) {
            while (br.ready()) {
                String line = br.readLine();
                if (!line.isEmpty()
                        && !line.equals("id,type,status,name,description,epicId")) {
                    String typeTask = line.split(",")[1];
                    switch (typeTask) {
                        case "TASK" -> result.addTask(result.fromString(line));
                        case "SUBTASK" -> result.addSubTask((SubTask) result.fromString(line));
                        case "EPIC" -> result.addEpic((Epic) result.fromString(line));
                        default -> {
                            TreeMap<Integer, Task> allTask = result.mergeTypes();
                            for (Integer id : historyFromString(line)) {
                                result.history.add(allTask.get(id));
                            }
                        }
                    }
                }
            }
            return result;
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время чтения файла.");
        }
        return null;
    }

    // Восстановление истории с сохранением позиций в списке
    public static List<Integer> historyFromString(String value) {
        List<Integer> result = new ArrayList<>();
        String[] ids = value.split(",");

        for(int i = ids.length - 1; i >=0; i--) {
            result.add(Integer.parseInt(ids[i]));
        }
        return result;
    }

    // Сохранение в файл
    private void save() {
        try  {
            try (Writer fw = new FileWriter(path)) {
                TreeMap<Integer, Task> allTasks = mergeTypes();
                fw.write("id,type,status,name,description,epicId\n");
                for (Task task : allTasks.values()) {
                    fw.write(fromTask(task));
                    fw.write("\n");
                }
                fw.write("\n");
                fw.write(historyToString(history));
            } catch (IOException e) {
                throw new ManagerSaveException("Произошла ошибка во время записи в файл.");
            }
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    // Объединение списков
    private TreeMap<Integer, Task> mergeTypes() {
        TreeMap<Integer, Task> treeMap = new TreeMap<>();
        treeMap.putAll(taskList);
        treeMap.putAll(epicList);
        treeMap.putAll(subTaskList);
        return treeMap;
    }

    // Получение экземпляра класса из строки
    private Task fromString(String value) {
        String[] elements = value.split(",");
        return switch(elements[1]) {
            case ("TASK") -> new Task(value);
            case ("EPIC") -> new Epic(value);
            case ("SUBTASK") -> new SubTask(value);
            default -> null;
        };
    }

    //Получение строки в зависимости от типа задачи
    private String fromTask(Task task) {
       String result = "";
       if (task.getType().equals(TaskTypes.TASK)) {
           result = Task.toString(task);
       } else if (task.getType().equals(TaskTypes.EPIC)) {
           result = Epic.toString((Epic) task);
       } else if (task.getType().equals(TaskTypes.SUBTASK)) {
           result = SubTask.toString((SubTask) task);
       }
       return result;
   }

    //Получение идентификаторов задач из истории в виде строки
    static String historyToString(HistoryManager manager) {
        StringBuilder idsFromHistory = new StringBuilder();
        for (Task task: manager.getHistory()) {
            idsFromHistory.append(task.getId())
                            .append(",");
        }
        return idsFromHistory.toString();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save();
    }

    @Override
    public void updateTask(int id, Task newTask) {
        super.updateTask(id, newTask);
        save();
    }

    @Override
    public void updateEpic(int id, Epic newEpic) {
        super.updateEpic(id, newEpic);
        save();
    }

    @Override
    public void updateSubTask(int id, SubTask newSubTask) {
        super.updateSubTask(id, newSubTask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
        save();
    }

    @Override
    public void clearTaskList() {
        super.clearTaskList();
        save();
    }

    @Override
    public void clearEpicList() {
        super.clearEpicList();
        save();
    }

    @Override
    public void clearSubTaskList() {
        super.clearSubTaskList();
        save();
    }

    @Override
    public void checkStatus(int id) {
        super.checkStatus(id);
        save();
    }
}
