package ru.yandex.practicum.agolykh.kanban.managers.task;

import ru.yandex.practicum.agolykh.kanban.tasks.Epic;
import ru.yandex.practicum.agolykh.kanban.tasks.SubTask;
import ru.yandex.practicum.agolykh.kanban.tasks.Task;

import java.util.ArrayList;

public interface TaskManager {
    // Получить список задач
    ArrayList<Task> getTaskList();

    // Получить список родительски задач
    ArrayList<Epic> getEpicList();

    // Получить список подзадач
    ArrayList<SubTask> getSubTaskList();

    // Получение всех подзадач определенного эпика
    ArrayList<SubTask> getSubTasksOfEpic(int id);

    // Получение задачи по идентификатору
    Task getTask(int id);

    // Получение родительской задачи по идентификатору
    Epic getEpic(int id);

    // Получение подзадачи по идентификатору
    SubTask getSubTask(int id);

    // Добавить задачу
    void addTask(Task task);

    // Добавить родительскую задачу
    void addEpic(Epic epic);

    // Добавить подзадачу
    void addSubTask(SubTask subTask);

    // Обновить задачу
    void updateTask(int id, Task newTask);

    // Обновить родительскую задачу
    void updateEpic(int id, Epic newEpic);

    // Обновить подзадачу
    void updateSubTask(int id, SubTask newSubTask);

    // Удалить задачу
    void deleteTask(int id);

    // Удалить родительскую задачу и ее подзадачи
    void deleteEpic(int id);

    // Удалить подзадачу из основного списка и привязку у эпика
    void deleteSubTask(int id);

    // Очистить список обычных задач
    void clearTaskList();

    // Очистить список родительских задач
    void clearEpicList();

    // Очистить список подзадач
    void clearSubTaskList();

    // Проверка статуса эпика
    void checkStatus(int id);

    ArrayList<Task> getHistory();
}

