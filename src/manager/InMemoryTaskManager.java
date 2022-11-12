package manager;

import tasks.epic.Epic;
import tasks.subtask.SubTask;
import tasks.task.Task;
import tasks.Status;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

public class InMemoryTaskManager {
    private static int nextId = 1;
    private TreeMap<Integer, Task> taskList;
    private TreeMap<Integer, Epic> epicList;
    private TreeMap<Integer, SubTask> subTaskList;

    public InMemoryTaskManager() {
        taskList = new TreeMap<>();
        epicList = new TreeMap<>();
        subTaskList = new TreeMap<>();
    }

    // Получить список задач
    public ArrayList<Task> getTaskList() {
        return new ArrayList<>(taskList.values());
    }

    // Получить список родительски задач
    public ArrayList<Epic> getEpicList() {
        return new ArrayList<>(epicList.values());
    }

    // Получить список подзадач
    public ArrayList<SubTask> getSubTaskList() {
        return new ArrayList<>(subTaskList.values());
    }

    // Получение всех подзадач определенного эпика
    public ArrayList<SubTask> getSubTasksOfEpic(int id) {
        ArrayList<SubTask> result = new ArrayList<>();
        if (epicList.containsKey(id)) {
            Epic epic = epicList.get(id);
            for (Integer subTaskId : epic.getListSubTaskId()) {
                if (subTaskList.containsKey(subTaskId)) {
                    result.add(getSubTask(subTaskId));
                }
            }
        }
        return result;
    }

    // Получение задачи по идентификатору
    public Task getTask(int id) {
        return taskList.get(id);
    }

    // Получение родительской задачи по идентификатору
    public Epic getEpic(int id) {
        return epicList.get(id);
    }

    // Получение подзадачи по идентификатору
    public SubTask getSubTask(int id) {
        return subTaskList.get(id);
    }

    // Добавить задачу
    public void addTask(Task task) {
        task.setId(nextId++);
        taskList.put(task.getId(), task);
        System.out.println("Задача " + task.getId() + " добавлена.");
    }

    // Добавить родительскую задачу
    public void addEpic(Epic epic) {
        epic.setId(nextId++);
        epicList.put(epic.getId(), epic);
        System.out.println("Родительская задача " + epic.getId() + " добавлена.");
    }

    // Добавить подзадачу
    public void addSubTask(SubTask subTask) {
        subTask.setId(nextId++);
        int id = subTask.getId();
        int epicId = subTask.getEpicId();
        if (epicList.containsKey(epicId)) {
            Epic epic = epicList.get(epicId);
            subTaskList.put(subTask.getId(), subTask);
            epic.addSubTaskId(id);
            epicList.put(epicId, epic);
            checkStatus(id);
            System.out.println("Подзадача " + id + " добавлена в родительскую задачу " + epicId + '.');
        } else {
            System.out.println("Родительская задача " + epicId + " не обнаружена.");
        }
    }

    // Обновить задачу
    public void updateTask(int id, Task newTask) {
        if (taskList.containsKey(id)) {
            Task task = taskList.get( id);

            if (newTask.getStatus() != null) {
                task.setStatus(newTask.getStatus());
            }

            if (newTask.getName() != null) {
                task.setName(newTask.getName());
            }

            if (newTask.getDescription() != null) {
                task.setDescription(newTask.getDescription());
            }

            taskList.put(id, task);
            System.out.println("Задача " + id + " обновлена.");
        } else {
            System.out.println("Задача " + id + " не найдена.");
        }
    }

    // Обновить родительскую задачу
    public void updateEpic(int id, Epic newEpic) {
        if (epicList.containsKey(id)) {
            Epic epic = epicList.get(id);

            if (newEpic.getName() != null) {
                epic.setName(newEpic.getName());
            }

            if (newEpic.getDescription() != null) {
                epic.setDescription(newEpic.getDescription());
            }

            epicList.put(id, epic);
            System.out.println("Родительская задача " + id + " обновлена.");
        } else {
            System.out.println("Родительская задача " + id + " не найдена.");
        }
    }

    // Обновить подзадачу
    public void updateSubTask(int id, SubTask newSubTask) {
        if (subTaskList.containsKey(id)) {
            SubTask subTask = subTaskList.get(id);
            int epicId = subTask.getEpicId();

            if (newSubTask.getStatus() != null) {
                subTask.setStatus(newSubTask.getStatus());
            }

            if (newSubTask.getName() != null) {
                subTask.setName(newSubTask.getName());
            }

            if (newSubTask.getDescription() != null) {
                subTask.setDescription(newSubTask.getDescription());
            }

            subTaskList.put(id, subTask);
            checkStatus(epicId);
        } else {
            System.out.println("Подзадача " + id + " не найдена.");
        }
    }

    // Удалить задачу
    public void deleteTask(int id) {
        if (taskList.containsKey(id)) {
            taskList.remove(id);
            System.out.println("Задача " + id + " удалена.");
        } else {
            System.out.println("Задача " + id + " не найдена.");
        }
    }

    // Удалить родительскую задачу и ее подзадачи
    public void deleteEpic(int id) {
        if (epicList.containsKey(id)) {
            for (Integer subTaskId : epicList.get(id).getListSubTaskId()) {
                if (subTaskList.containsKey(subTaskId)) {
                    subTaskList.remove(subTaskId);
                    System.out.println("Подзадача " + subTaskId + " удалена.");
                }
            }
            epicList.remove(id);
            System.out.println("Задача " + id + " удалена.");
        } else {
            System.out.println("Задача " + id + " не найдена.");
        }
    }

    // Удалить подзадачу из основного списка и привязку у эпика
    public void deleteSubTask(int id) {
        if (subTaskList.containsKey(id)) {
            SubTask subTask = subTaskList.get(id);
            int epicId = subTask.getEpicId();
            Epic epic = epicList.get(epicId);
            epic.delSubTaskId(id);
            subTaskList.remove(id);
            epicList.put(epicId, epic);
            checkStatus(epicId);
            System.out.println("Подзадача " + id + " удалена.");
        } else {
            System.out.println("Подзадача " + id + " не найдена.");
        }
    }

    // Очистить список обычных задач
    public void clearTaskList() {
        taskList.clear();
        System.out.println("Список обычных задач удален.");
    }

    // Очистить список родительских задач
    public void clearEpicList() {
        epicList.clear();
        subTaskList.clear();
        System.out.println("Список родительских задач c подзадачами удален.");
    }

    // Очистить список подзадач
    public void clearSubTaskList() {
        subTaskList.clear();
        for (Integer id : epicList.keySet()) {
            Epic epic = epicList.get(id);
            epic.listSubTaskId.clear();
            epicList.put(id, epic);
            checkStatus(id);
        }
        System.out.println("Список подзадач удален.");
    }

    // Проверка статуса эпика
    private void checkStatus(int id) {
        if (epicList.containsKey(id)) {
            Set<Status> statusSet = new HashSet<>();
            Epic epic = epicList.get(id);
            Status oldStatus = epic.getStatus();
            for (Integer idSubTask : epic.getListSubTaskId()) {
                SubTask subTask = subTaskList.get(idSubTask);
                statusSet.add(subTask.getStatus());
            }

            if ((statusSet.contains(Status.NEW)
                    && (statusSet.size() == 1))
                    || statusSet.isEmpty()) {
                epic.setStatus(Status.NEW);
            } else if (statusSet.contains(Status.DONE)
                    && (statusSet.size() == 1)) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
            epicList.put(id, epic);

            if (!oldStatus.equals(epic.getStatus())) {
                System.out.println("Статус родительской задачи " + id + " обновлен.");
            }
        }
    }
}