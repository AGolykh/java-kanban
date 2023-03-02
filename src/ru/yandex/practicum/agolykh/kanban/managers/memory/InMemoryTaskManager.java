package ru.yandex.practicum.agolykh.kanban.managers.memory;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.agolykh.kanban.managers.history.HistoryManager;
import ru.yandex.practicum.agolykh.kanban.managers.Managers;
import ru.yandex.practicum.agolykh.kanban.managers.TaskManager;
import ru.yandex.practicum.agolykh.kanban.tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class InMemoryTaskManager implements TaskManager {
    private int nextId = 0;
    protected final Map<Integer, Task> taskList;
    protected final Map<Integer, Epic> epicList;
    protected final Map<Integer, SubTask> subTaskList;
    protected  Map<LocalDateTime, Boolean> timeList;
    protected final HistoryManager history;

    public InMemoryTaskManager() {
        taskList = new TreeMap<>();
        epicList = new TreeMap<>();
        subTaskList = new TreeMap<>();
        history = Managers.getDefaultHistory();
        timeList = Managers.getTimeList(2023);
    }

    // Получить список задач
    public Collection<Task> getTaskList() {
        return taskList.values();
    }

    public void reNewTimeList() {
        timeList = Managers.getTimeList(2025);
    }

    // Получить список родительски задач
    public Collection<Epic> getEpicList() {
        return epicList.values();
    }

    // Получить список подзадач
    public Collection<SubTask> getSubTaskList() {
        return subTaskList.values();
    }

    // Получение всех подзадач определенного эпика
    public Collection<SubTask> getBindList(Integer epicId) {
        containsTask(TaskTypes.EPIC, epicId);
        return subTaskList
                .values()
                .stream()
                .filter(subTask -> epicList.get(epicId).getListSubTaskId().contains(subTask.getId()))
                .collect(Collectors.toList());
    }

    // Получение задачи по идентификатору
    public Task getTaskById(Integer taskId) {
        containsTask(TaskTypes.TASK, taskId);
        history.add(taskList.get(taskId));
        return taskList.get(taskId);
    }

    // Получение родительской задачи по идентификатору
    public Epic getEpicById(Integer epicId) {
        containsTask(TaskTypes.EPIC, epicId);
        history.add(epicList.get(epicId));
        return epicList.get(epicId);
    }

    // Получение подзадачи по идентификатору
    public SubTask getSubTaskById(Integer subTaskId) {
        containsTask(TaskTypes.SUBTASK, subTaskId);
        history.add(subTaskList.get(subTaskId));
        return subTaskList.get(subTaskId);
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
        updateTimeList(true, task);
        task.setId(makeId(task.getId()));
        taskList.put(task.getId(), task);
        System.out.println("Задача " + task.getId() + " добавлена.");
    }

    // Добавить родительскую задачу
    public void addEpic(Epic epic) {
        epic.setId(makeId(epic.getId()));
        epicList.put(epic.getId(), epic);
        updateBindList(epic.getId());
        System.out.println("Родительская задача " + epic.getId() + " добавлена.");
    }

    // Добавить подзадачу
    public void addSubTask(SubTask subTask) {
        if (!validateTime(subTask)) {
            return;
        }
        updateTimeList(true, subTask);
        subTask.setId(makeId(subTask.getId()));
        subTaskList.put(subTask.getId(), subTask);
        updateBindList(subTask.getEpicId());
        System.out.println("Подзадача " + subTask.getId() + " добавлена.");
    }

    // Добавление в лист эпика подзадачи
    public void updateBindList(Integer epicId) {
        containsTask(TaskTypes.EPIC, epicId);
        subTaskList.values()
                .stream()
                .filter(subTask -> subTask.getEpicId() == epicId)
                .forEach(subTask -> epicList.get(epicId).getListSubTaskId().add(subTask.getId()));
        calculateEpic(epicId);
    }

    // Обновить задачу
    public void updateTask(Task newTask) {
        containsTask(TaskTypes.TASK, newTask.getId());
        updateTimeList(false, taskList.get(newTask.getId()));
        taskList.put(newTask.getId(), newTask);
        updateTimeList(true, newTask);
        System.out.println("Задача " + newTask.getId() + " обновлена.");
    }

    // Обновить родительскую задачу
    public void updateEpic(Epic newEpic) {
        containsTask(TaskTypes.EPIC, newEpic.getId());
        newEpic.setId(newEpic.getId());
        epicList.put(newEpic.getId(), newEpic);
        System.out.println("Родительская задача " + newEpic.getId() + " обновлена.");
    }

    // Обновить подзадачу
    public void updateSubTask(SubTask newSubTask) {
        containsTask(TaskTypes.SUBTASK, newSubTask.getId());
        updateTimeList(false, subTaskList.get(newSubTask.getId()));
        subTaskList.put(newSubTask.getId(), newSubTask);
        updateTimeList(true, newSubTask);
        calculateEpic(newSubTask.getEpicId());
        System.out.println("Подзадача " + newSubTask.getId() + " обновлена.");
    }

    // Удалить задачу
    public void deleteTaskById(Integer taskId) {
        containsTask(TaskTypes.TASK, taskId);
        updateTimeList(false, taskList.get(taskId));
        taskList.remove(taskId);
        history.remove(taskId);
        System.out.println("Задача " + taskId + " удалена.");
    }

    // Удалить родительскую задачу и ее подзадачи
    public void deleteEpicById(Integer epicId) {
        containsTask(TaskTypes.EPIC, epicId);
        for (Integer subTaskId : epicList.get(epicId).getListSubTaskId()) {
            if (subTaskList.containsKey(subTaskId)) {
                subTaskList.remove(subTaskId);
                history.remove(subTaskId);
                System.out.println("Подзадача " + subTaskId + " удалена.");
            }
        }
        epicList.remove(epicId);
        history.remove(epicId);
        System.out.println("Родительская задача " + epicId + " удалена.");
    }

    // Удалить подзадачу из основного списка и привязку у эпика
    public void deleteSubTaskById(Integer subTaskId) {
        containsTask(TaskTypes.SUBTASK, subTaskId);
        Integer epicId = subTaskList.get(subTaskId).getEpicId();
        Epic epic = epicList.get(epicId);
        epic.delSubTaskId(subTaskId);
        updateTimeList(false, subTaskList.get(subTaskId));
        subTaskList.remove(subTaskId);
        history.remove(subTaskId);
        epicList.put(epicId, epic);
        calculateEpic(epicId);
        System.out.println("Подзадача " + subTaskId + " удалена.");
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
    private void calculateEpic(Integer epicId) {
        containsTask(TaskTypes.EPIC, epicId);
        checkStatus(epicId);
        calculateTime(epicId);
    }

    // Проверка статуса эпика   ********************* юзать стрим
    private void checkStatus(Integer epicId) {
        Epic epic = epicList.get(epicId);
        Status oldStatus = epic.getStatus();
        Set<Status> statusSet = subTaskList.values()
                .stream()
                .filter(subTask -> epicList.get(epicId).getListSubTaskId().contains(subTask.getId()))
                .map(SubTask::getStatus)
                .collect(Collectors.toSet());

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
        epicList.put(epicId, epic);
        if (!oldStatus.equals(epic.getStatus())) {
            System.out.println("Статус родительской задачи " + epicId + " обновлен.");
        }
    }

    // Расчет времени для эпика ***************** юзать стрим
    private void calculateTime(Integer epicId) {
        TreeSet<SubTask> mapOfSubTask = new TreeSet<>(Comparator.comparing(Task::getStartTime));
        Epic epic = epicList.get(epicId);
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

    // Проверка свободного времени
    private boolean validateTime(Task task) {
        if(getPrioritizedTasks().isEmpty()) {
            return true;
        }

        LocalDateTime curTimeStamp = task.getStartTime();
        while (curTimeStamp.isBefore(task.getEndTime())) {
            if(timeList.get(curTimeStamp)) {
                log.warn("Текущее время занято " + curTimeStamp);
                return false;
            }
            curTimeStamp = curTimeStamp.plusMinutes(15);
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

        TreeSet<Task> result = new TreeSet<>(generalComparator);
        result.addAll(taskList.values());
        result.addAll(subTaskList.values());
        return result;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history.getHistory();
    }

    private void containsTask(TaskTypes types, Integer id) {
        switch (types) {
            case TASK -> {
                if (!taskList.containsKey(id)) {
                    String message = String.format("Task %d is not found.", id);
                    log.warn(message);
                    throw new NullPointerException(message);
                }
            }
            case EPIC -> {
                if (!epicList.containsKey(id)) {
                    String message = String.format("Epic %d is not found.", id);
                    log.warn(message);
                    throw new NullPointerException(message);
                }
            }
            case SUBTASK -> {
                if (!subTaskList.containsKey(id)) {
                    String message = String.format("Subtask %d is not found.", id);
                    log.warn(message);
                    throw new NullPointerException(message);
                }
            }
        }
    }

    private void updateTimeList(boolean mode, Task task) {
        LocalDateTime curTimeStamp = task.getStartTime();
        if(mode) {
            while (curTimeStamp.isBefore(task.getEndTime())) {
                timeList.put(curTimeStamp, true);
                curTimeStamp = curTimeStamp.plusMinutes(15);
            }
        } else {
            while (curTimeStamp.isBefore(task.getEndTime())) {
                timeList.put(curTimeStamp, false);
                curTimeStamp = curTimeStamp.plusMinutes(15);
            }
        }
    }
}
