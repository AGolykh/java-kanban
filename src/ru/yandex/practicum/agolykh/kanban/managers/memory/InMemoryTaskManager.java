package ru.yandex.practicum.agolykh.kanban.managers.memory;

import ru.yandex.practicum.agolykh.kanban.managers.history.HistoryManager;
import ru.yandex.practicum.agolykh.kanban.managers.Managers;
import ru.yandex.practicum.agolykh.kanban.managers.TaskManager;
import ru.yandex.practicum.agolykh.kanban.tasks.Epic;
import ru.yandex.practicum.agolykh.kanban.tasks.SubTask;
import ru.yandex.practicum.agolykh.kanban.tasks.Task;
import ru.yandex.practicum.agolykh.kanban.tasks.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int nextId = 0;
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
        } else {
            throw new NullPointerException("Задача " + id + " не найдена.");
        }
        return taskList.get(id);
    }

    // Получение родительской задачи по идентификатору
    public Epic getEpic(int id) {
        if (epicList.containsKey(id)) {
            history.add(epicList.get(id));
        } else {
            throw new NullPointerException("Родительская задача " + id + " не найдена.");
        }
        return epicList.get(id);
    }

    // Получение подзадачи по идентификатору
    public SubTask getSubTask(int id) {
        if (subTaskList.containsKey(id)) {
            history.add(subTaskList.get(id));
        } else {
            throw new NullPointerException("Подзадача " + id + " не найдена.");
        }
        return subTaskList.get(id);
    }

    // Расстановка id
    private Integer makeId(Integer id) {
        if (id != null) {
            nextId = id;
            return id;
        }
        return ++nextId;
    }

    // Добавить задачу
    public void addTask(Task task) {
        if (!validateTime(task)) {
            return;
        }
        task.setId(makeId(task.getId()));
        taskList.put(task.getId(), task);
        System.out.println("Задача " + task.getId() + " добавлена.");
    }

    // Добавить родительскую задачу
    public void addEpic(Epic epic) {
        epic.setId(makeId(epic.getId()));
        epicList.put(epic.getId(), epic);
        makeListSubTaskForEpic();
        System.out.println("Родительская задача " + epic.getId() + " добавлена.");
    }

    // Добавить подзадачу
    public void addSubTask(SubTask subTask) {
        if (!validateTime(subTask)) {
            return;
        }
        subTask.setId(makeId(subTask.getId()));
        subTaskList.put(subTask.getId(), subTask);
        makeListSubTaskForEpic();
        System.out.println("Подзадача " + subTask.getId() + " добавлена.");
    }

    public void makeListSubTaskForEpic() {
        for (Epic epic : epicList.values()) {
            for (SubTask subTask : subTaskList.values()) {
                if(subTask.getEpicId() == epic.getId()) {
                    epic.getListSubTaskId().add(subTask.getId());
                    System.out.println("Подзадача " + subTask.getId() +
                            " добавлена в родительскую задачу " + epic.getId() + '.');
                    calculateEpic(epic.getId());
                }
            }
        }
    }

    // Обновить задачу
    public void updateTask(Task newTask) {
        if (taskList.containsKey(newTask.getId())) {
            taskList.put(newTask.getId(), newTask);
            System.out.println("Задача " + newTask.getId() + " обновлена.");
        } else {
            throw new NullPointerException("Задача " + newTask.getId() + " не найдена.");
        }
    }

    // Обновить родительскую задачу
    public void updateEpic(Epic newEpic) {
        if (epicList.containsKey(newEpic.getId())) {
            newEpic.setId(newEpic.getId());
            epicList.put(newEpic.getId(), newEpic);
            System.out.println("Родительская задача " + newEpic.getId() + " обновлена.");
        } else {
            throw new NullPointerException("Родительская задача " + newEpic.getId() + " не найдена.");
        }
    }

    // Обновить подзадачу
    public void updateSubTask(SubTask newSubTask) {
        if (subTaskList.containsKey(newSubTask.getId())) {
            newSubTask.setId(newSubTask.getId());
            subTaskList.put(newSubTask.getId(), newSubTask);
            System.out.println("Подзадача " + newSubTask.getId() + " обновлена.");
            calculateEpic(newSubTask.getEpicId());
        } else {
            throw new NullPointerException("Подзадача " + newSubTask.getId() + " не найдена.");
        }
    }

    // Удалить задачу
    public void deleteTask(int id) {
        if (taskList.containsKey(id)) {
            taskList.remove(id);
            history.remove(id);
            System.out.println("Задача " + id + " удалена.");
        } else {
            throw new NullPointerException("Задача " + id + " не найдена.");
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
            System.out.println("Родительская задача " + id + " удалена.");
        } else {
            throw new NullPointerException("Родительская задача " + id + " не найдена.");
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
            calculateEpic(epicId);
            System.out.println("Подзадача " + id + " удалена.");
        } else {
            throw new NullPointerException("Подзадача " + id + " не найдена.");
        }
    }

    // Очистить список обычных задач
    public void clearTaskList() {
        for (Integer integer : taskList.keySet()) {
            history.remove(integer);
        }
        taskList.clear();
        System.out.println("Список обычных задач удален.");
    }

    // Очистить список родительских задач
    public void clearEpicList() {
        for (Integer integer : subTaskList.keySet()) {
            history.remove(integer);
        }
        for (Integer integer : epicList.keySet()) {
            history.remove(integer);
        }
        epicList.clear();
        subTaskList.clear();
        System.out.println("Список родительских задач c подзадачами удален.");
    }

    // Очистить список подзадач
    public void clearSubTaskList() {
        subTaskList.clear();
        for (Integer id : epicList.keySet()) {
            Epic epic = epicList.get(id);
            epic.getListSubTaskId().clear();
            epicList.put(id, epic);
            calculateEpic(id);
        }
        System.out.println("Список подзадач удален.");
    }

    // Расчет статуса и времени эпика
    private void calculateEpic(int id) {
        checkStatus(id);
        calculateTime(id);
    }

    // Проверка статуса эпика
    private void checkStatus(int id) {
        if (epicList.containsKey(id)) {
            Set<Status> statusSet = new HashSet<>();
            Epic epic = epicList.get(id);
            Status oldStatus = epic.getStatus();

            for (Integer idSubTask : subTaskList.keySet()) {
                for (Integer idSubTaskFromEpic : epic.getListSubTaskId()) {
                    if (idSubTask.equals(idSubTaskFromEpic)) {
                        SubTask subTask = subTaskList.get(idSubTask);
                        statusSet.add(subTask.getStatus());
                    }
                }
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

    // Расчет времени для эпика
    private void calculateTime(int id) {
        TreeSet<SubTask> mapOfSubTask = new TreeSet<>(Comparator.comparing(Task::getStartTime));
        if (epicList.containsKey(id)) {
            Epic epic = epicList.get(id);
            for (Integer idSubTask : subTaskList.keySet()) {
                for (Integer idSubTaskFromEpic : epic.getListSubTaskId()) {
                    if (idSubTask.equals(idSubTaskFromEpic)
                            && subTaskList.get(idSubTask).getStartTime() != null) {
                        mapOfSubTask.add(subTaskList.get(idSubTask));
                    }
                }
            }

            if(!mapOfSubTask.isEmpty()) {
                epic.setStartTime(mapOfSubTask.first().getStartTime());
                epic.setDuration(Duration.between(epic.getStartTime(),
                        mapOfSubTask.last().getEndTime()));
                epic.setEndTime();
            }
        }
    }

    // Проверка свободного времени
    private boolean validateTime(Task task) {
        if (getPrioritizedTasks().isEmpty()) {
            return true;
        }
        for (Task taskFromList : getPrioritizedTasks()) {
            if ((taskFromList.getStartTime() != null) && (task.getStartTime() != null)) {
                boolean startTimeValid = task.getStartTime().isBefore(taskFromList.getStartTime())
                        && task.getEndTime().isBefore(taskFromList.getStartTime());
                boolean endTimeValid = task.getStartTime().isAfter(taskFromList.getEndTime())
                        && task.getEndTime().isAfter(taskFromList.getEndTime());
                if (!startTimeValid && !endTimeValid) {
                    return false;
                }
            }
        }
        return true;
    }

    // Вывод списка приоритета
    @Override
    public Set<Task> getPrioritizedTasks() {
        Comparator<Task> comparatorStartTime = (e1, e2) -> Comparator
                .<LocalDateTime>nullsLast(Comparator.naturalOrder())
                .compare(e1.getStartTime(), e2.getStartTime());

        Comparator<Task> comparatorId = (e1, e2) -> Comparator
                .<Integer>nullsLast(Comparator.naturalOrder())
                .compare(e1.getId(), e2.getId());

        Comparator<Task> generalComparator = (e1, e2) -> {
            if (e1.getStartTime() == null || e2.getStartTime() == null) {
                return comparatorId.compare(e1, e2);
            } else {
                return comparatorStartTime.compare(e1, e2);
            }
        };

        TreeSet<Task> treeSet = new TreeSet<>(generalComparator);
        treeSet.addAll(taskList.values());
        treeSet.addAll(subTaskList.values());
        return treeSet;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history.getHistory();
    }

}
