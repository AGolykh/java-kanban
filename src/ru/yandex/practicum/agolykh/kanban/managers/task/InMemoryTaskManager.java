package ru.yandex.practicum.agolykh.kanban.managers.task;

import ru.yandex.practicum.agolykh.kanban.managers.history.HistoryManager;
import ru.yandex.practicum.agolykh.kanban.managers.Managers;
import ru.yandex.practicum.agolykh.kanban.tasks.Epic;
import ru.yandex.practicum.agolykh.kanban.tasks.SubTask;
import ru.yandex.practicum.agolykh.kanban.tasks.Task;
import ru.yandex.practicum.agolykh.kanban.tasks.Status;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private static int nextId = 0;
    protected final Map<Integer, Task> taskList;
    protected final Map<Integer, Epic> epicList;
    protected final Map<Integer, SubTask> subTaskList;
    protected final HistoryManager history;

    public InMemoryTaskManager() {
        taskList = new TreeMap<>();
        epicList = new TreeMap<>();
        subTaskList = new TreeMap<>();
        history = Managers.getDefaultHistory();
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
                    result.add(subTaskList.get(subTaskId));
                }
            }
        }
        return result;
    }

    // Получение задачи по идентификатору
    public Task getTask(int id) {
        if (taskList.containsKey(id)) {
            history.add(taskList.get(id));
        }
        return taskList.get(id);
    }

    // Получение родительской задачи по идентификатору
    public Epic getEpic(int id) {
        if (epicList.containsKey(id)) {
            history.add(epicList.get(id));
        }
        return epicList.get(id);
    }

    // Получение подзадачи по идентификатору
    public SubTask getSubTask(int id) {
        if (subTaskList.containsKey(id)) {
            history.add(subTaskList.get(id));
        }
        return subTaskList.get(id);
    }

    // Расстановка id
    private Integer makeId(Integer id) {
        int result;
        result = Objects.requireNonNullElseGet(id, () -> ++nextId);
        return result;
    }

    // Добавить задачу
    public void addTask(Task task) {
        task.setId(makeId(task.getId()));
        taskList.put(task.getId(), task);
        System.out.println("Задача " + task.getId() + " добавлена.");
    }

    // Добавить родительскую задачу
    public void addEpic(Epic epic) {
        epic.setId(makeId(epic.getId()));
        epicList.put(epic.getId(), epic);
        System.out.println("Родительская задача " + epic.getId() + " добавлена.");
    }

    // Добавить подзадачу
    public void addSubTask(SubTask subTask) {
        subTask.setId(makeId(subTask.getId()));
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
            Task task = taskList.get(id);
            boolean changeFlag = false;

            if ((newTask.getStatus() != null)
                    && !newTask.getStatus().equals(task.getStatus())) {
                task.setStatus(newTask.getStatus());
                changeFlag = true;
            }

            if ((newTask.getName() != null)
                    && !newTask.getName().equals(task.getName())) {
                task.setName(newTask.getName());
                changeFlag = true;
            }

            if ((newTask.getDescription() != null)
                    && !newTask.getDescription().equals(task.getDescription())) {
                task.setDescription(newTask.getDescription());
                changeFlag = true;
            }

            if (changeFlag) {
                taskList.put(id, task);
                System.out.println("Задача " + id + " обновлена.");
            }
        } else {
            System.out.println("Задача " + id + " не найдена.");
        }
    }

    // Обновить родительскую задачу
    public void updateEpic(int id, Epic newEpic) {
        if (epicList.containsKey(id)) {
            Epic epic = epicList.get(id);
            boolean changeFlag = false;

            if ((newEpic.getName() != null)
                    && !newEpic.getName().equals(epic.getName())) {
                epic.setName(newEpic.getName());
                changeFlag = true;
            }

            if ((newEpic.getDescription() != null)
                    && !newEpic.getDescription().equals(epic.getDescription())) {
                epic.setDescription(newEpic.getDescription());
                changeFlag = true;
            }

            if (changeFlag) {
                epicList.put(id, epic);
                System.out.println("Родительская задача " + id + " обновлена.");
            }
        } else {
            System.out.println("Родительская задача " + id + " не найдена.");
        }
    }

    // Обновить подзадачу
    public void updateSubTask(int id, SubTask newSubTask) {
        if (subTaskList.containsKey(id)) {
            SubTask subTask = subTaskList.get(id);
            int epicId = subTask.getEpicId();
            boolean changeFlag = false;

            if ((newSubTask.getStatus() != null)
                    && !newSubTask.getStatus().equals(subTask.getStatus())) {
                subTask.setStatus(newSubTask.getStatus());
                changeFlag = true;
            }

            if ((newSubTask.getName() != null)
                    && !newSubTask.getName().equals(subTask.getName())) {
                subTask.setName(newSubTask.getName());
                changeFlag = true;
            }

            if ((newSubTask.getDescription() != null)
                    && !newSubTask.getDescription().equals(subTask.getDescription())) {
                subTask.setDescription(newSubTask.getDescription());
                changeFlag = true;
            }

            if (changeFlag) {
                subTaskList.put(id, subTask);
                System.out.println("Подзадача " + id + " обновлена.");
                checkStatus(epicId);
            }
        } else {
            System.out.println("Подзадача " + id + " не найдена.");
        }
    }

    // Удалить задачу
    public void deleteTask(int id) {
        if (taskList.containsKey(id)) {
            taskList.remove(id);
            history.remove(id);
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
                    history.remove(subTaskId);
                    System.out.println("Подзадача " + subTaskId + " удалена.");
                }
            }
            epicList.remove(id);
            history.remove(id);
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
            history.remove(id);
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
    public void checkStatus(int id) {
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

    @Override
    public ArrayList<Task> getHistory() {
        return history.getHistory();
    }

    @Override
    public int countOfNodes() {
        return history.countOfNodes();
    }
}
