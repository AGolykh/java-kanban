package ru.yandex.practicum.agolykh.kanban.managers.file;

import ru.yandex.practicum.agolykh.kanban.exceptions.ManagerSaveException;
import ru.yandex.practicum.agolykh.kanban.managers.history.HistoryManager;
import ru.yandex.practicum.agolykh.kanban.managers.memory.InMemoryTaskManager;
import ru.yandex.practicum.agolykh.kanban.tasks.Epic;
import ru.yandex.practicum.agolykh.kanban.tasks.SubTask;
import ru.yandex.practicum.agolykh.kanban.tasks.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private String path;

    public FileBackedTaskManager(String fileName) {
        final String dir = System.getProperty("user.dir") + "\\resources\\";
        this.path = dir + fileName;
    }

    public FileBackedTaskManager() {
    }

    // Создание экземпляра класса с данными из файла ***************** юзать стрим
    public static FileBackedTaskManager load(File file) {
        FileBackedTaskManager result = new FileBackedTaskManager("Tasks.csv");
        ArrayList<SubTask> tempSubTaskList = new ArrayList<>();
        try (FileReader reader = new FileReader(file, StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(reader)) {
            while (br.ready()) {
                String line = br.readLine();
                if (!line.isEmpty()
                        && !line.equals("id,type,status,name,description,epicId,duration,dateTime,endTime")) {
                    String typeTask = line.split(",")[1];
                    switch (typeTask) {
                        case "TASK" -> result.addTask(Converter.taskFromString(line));
                        case "SUBTASK" -> {
                            SubTask subTask = Converter.subTaskFromString(line);
                            if(result.epicList.containsKey(subTask.getEpicId())) {
                                result.addSubTask(subTask);
                            } else {
                                tempSubTaskList.add(subTask);
                            }
                        }
                        case "EPIC" -> result.addEpic(Converter.epicFromString(line));
                        default -> {
                            for (Integer id : historyFromString(line)) {
                                if (result.taskList.containsKey(id)) {
                                    result.history.add(result.taskList.get(id));
                                } else if (result.epicList.containsKey(id)) {
                                    result.history.add(result.epicList.get(id));
                                } else if (result.subTaskList.containsKey(id)) {
                                    result.history.add(result.subTaskList.get(id));
                                }
                                result.save();
                            }
                        }
                    }
                }
            }
            for (SubTask subTask : tempSubTaskList) {
                if(result.epicList.containsKey(subTask.getEpicId())) {
                    Epic epic = result.epicList.get(subTask.getEpicId());
                    epic.addSubTaskId(subTask.getId());
                    result.epicList.put(epic.getId(), epic);
                }
            }
            return result;
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при импорте данных из файла.");
        }
    }

    // Сохранение в файл ***************** юзать стрим
    public void save() {
        try (Writer fw = new FileWriter(path)) {
            fw.write("id,type,status,name,description,epicId,duration,dateTime,endTime\n");
            for (Task value : taskList.values()) {
                fw.write(fromTask(value));
            }
            for (Task value : epicList.values()) {
                fw.write(fromTask(value));
            }
            for (Task value : subTaskList.values()) {
                fw.write(fromTask(value));
            }
            fw.write("\n");
            fw.write(historyToString(history));
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при записи.");
        }
    }

    //Получение строки в зависимости от типа задачи
    private String fromTask(Task task) {
        return switch (task.getType()) {
            case TASK, EPIC -> Converter.taskToString(task);
            case SUBTASK -> Converter.subTaskToString((SubTask) task);
        };
    }

    //Получение идентификаторов задач из истории в виде строки
    static String historyToString(HistoryManager manager) {
        StringBuilder idsFromHistory = new StringBuilder();
        for (Task task : manager.getHistory()) {
            idsFromHistory.append(task.getId())
                    .append(",");
        }
        return idsFromHistory.toString();
    }

    // Восстановление истории с сохранением позиций в списке ***************** юзать стрим
    private static List<Integer> historyFromString(String value) {
        List<Integer> result = new ArrayList<>();
        String[] ids = value.split(",");

        for (int i = ids.length - 1; i >= 0; i--) {
            result.add(Integer.parseInt(ids[i]));
        }
        return result;
    }

    @Override
    public Task getTaskById(Integer taskId) {
        Task task = super.getTaskById(taskId);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(Integer epicId) {
        Epic epic = super.getEpicById(epicId);
        save();
        return epic;
    }

    @Override
    public SubTask getSubTaskById(Integer subTaskId) {
        SubTask subTask = super.getSubTaskById(subTaskId);
        save();
        return subTask;
    }

    @Override
    public void updateBindList(Integer epicId) {
        super.updateBindList(epicId);
        save();
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
    public void updateTask(Task newTask) {
        super.updateTask(newTask);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        save();
    }

    @Override
    public void updateSubTask(SubTask newSubTask) {
        super.updateSubTask(newSubTask);
        save();
    }

    @Override
    public void deleteTaskById(Integer taskId) {
        super.deleteTaskById(taskId);
        save();
    }

    @Override
    public void deleteEpicById(Integer epicId) {
        super.deleteEpicById(epicId);
        save();
    }

    @Override
    public void deleteSubTaskById(Integer subTaskId) {
        super.deleteSubTaskById(subTaskId);
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
}
