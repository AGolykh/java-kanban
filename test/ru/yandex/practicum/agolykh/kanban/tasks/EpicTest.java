package ru.yandex.practicum.agolykh.kanban.tasks;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.agolykh.kanban.managers.Managers;
import ru.yandex.practicum.agolykh.kanban.managers.task.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    private static TaskManager testTaskManager;

    @BeforeAll
    static void beforeAll() {
        testTaskManager = Managers.getDefault();
        testTaskManager.addEpic(
                new Epic("Тестовый эпик", "Описание тестового эпика"));
    }

    @BeforeEach
    void beforeEach() {
        testTaskManager.clearSubTaskList();
    }

    @Test
    void statusNewFromEpicWithSubTaskListIsEmpty() {
        assertEquals(Status.NEW, testTaskManager.getEpic(1).getStatus());
    }

    @Test
    void statusNewFromEpicWithSubTaskListWithNew() {
        testTaskManager.addSubTask(
                new SubTask("Новая подзадача 1",
                        "Описание новой подзадачи 1",
                        1));
        testTaskManager.addSubTask(
                new SubTask("Новая подзадача 2",
                        "Описание новой подзадачи 2",
                        1));
        assertEquals(Status.NEW, testTaskManager.getEpic(1).getStatus());
    }

    @Test
    void statusNewFromEpicWithSubTaskListWithDone() {
        testTaskManager.addSubTask(
                new SubTask(Status.DONE,
                        "Выполненная подзадача 1",
                        "Описание выполненной подзадачи 1",
                        1));
        testTaskManager.addSubTask(
                new SubTask(Status.DONE,
                "Выполненная подзадача 2",
                "Описание выполненной подзадачи 2",
                1));
        assertEquals(Status.DONE, testTaskManager.getEpic(1).getStatus());
    }

    @Test
    void statusNewFromEpicWithSubTaskListWithNewAndDone() {
        testTaskManager.addSubTask(
                new SubTask(Status.NEW,
                        "Новая подзадача 1",
                        "Описание новой подзадачи 1",
                        1));
        testTaskManager.addSubTask(
                new SubTask(Status.DONE,
                        "Выполненная подзадача 2",
                        "Описание выполненной подзадачи 2",
                        1));
        assertEquals(Status.IN_PROGRESS, testTaskManager.getEpic(1).getStatus());
    }

    @Test
    void statusNewFromEpicWithSubTaskListWithInProgress() {
        testTaskManager.addSubTask(
                new SubTask(Status.IN_PROGRESS,
                        "Выполненная подзадача 1",
                        "Описание выполненной подзадачи 1",
                        1));
        testTaskManager.addSubTask(
                new SubTask(Status.IN_PROGRESS,
                        "Выполненная подзадача 2",
                        "Описание выполненной подзадачи 2",
                        1));
        assertEquals(Status.IN_PROGRESS, testTaskManager.getEpic(1).getStatus());
    }

}