package ru.yandex.practicum.agolykh.kanban.managers;

import ru.yandex.practicum.agolykh.kanban.tasks.Epic;
import ru.yandex.practicum.agolykh.kanban.tasks.SubTask;
import ru.yandex.practicum.agolykh.kanban.tasks.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public interface TaskManager {
    // Получить список задач
    Collection<Task> getTaskList();

    // Получить список родительски задач
    Collection<Epic> getEpicList();

    // Получить список подзадач
    Collection<SubTask> getSubTaskList();

    // Получение всех подзадач определенного эпика
    Collection<SubTask> getBindList(Integer epicId);

    // Получение задачи по идентификатору
    Task getTaskById(Integer taskId);

    // Получение родительской задачи по идентификатору
    Epic getEpicById(Integer epicId);

    // Получение подзадачи по идентификатору
    SubTask getSubTaskById(Integer subTaskId);

    // Добавить задачу
    void addTask(Task task);

    // Добавить родительскую задачу
    void addEpic(Epic epic);

    // Добавить подзадачу
    void addSubTask(SubTask subTask);

    // Обновить задачу
    void updateTask(Task newTask);

    // Обновить родительскую задачу
    void updateEpic(Epic newEpic);

    // Обновить подзадачу
    void updateSubTask(SubTask newSubTask);

    // Удалить задачу
    void deleteTaskById(Integer taskId);

    // Удалить родительскую задачу и ее подзадачи
    void deleteEpicById(Integer epicId);

    // Удалить подзадачу из основного списка и привязку у эпика
    void deleteSubTaskById(Integer subTaskId);

    void reNewTimeList();

    // Очистить список обычных задач
    void clearTaskList();

    // Очистить список родительских задач
    void clearEpicList();

    // Очистить список подзадач
    void clearSubTaskList();

    // Получение множества задач по приоритету
    Set<Task> getPrioritizedTasks();

    // Получение истории
    ArrayList<Task> getHistory();
}

