package ru.yandex.practicum.agolykh.kanban.managers.history;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.agolykh.kanban.managers.Managers;
import ru.yandex.practicum.agolykh.kanban.tasks.Status;
import ru.yandex.practicum.agolykh.kanban.tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    private static HistoryManager testHistoryManager;
    private static final Task task1 = new Task(
            Status.NEW,
            "Задача 1",
            "Описание задачи 1");
    private static final Task task2 = new Task(
            Status.NEW,
            "Задача 2",
            "Описание задачи 2");
    private static final Task task3 = new Task(
            Status.NEW,
            "Задача 3",
            "Описание задачи 3");

    @BeforeEach
    void beforeEach() {
        testHistoryManager = Managers.getDefaultHistory();
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);
    }

    @Test
    void getHistory_returnEmpty_emptyHistory(){
        assertTrue(testHistoryManager.getHistory().isEmpty());
    }

    @Test
    void addTaskInHistory_returnTask_emptyHistory(){
        testHistoryManager.add(task1);
        assertEquals(1, testHistoryManager.getHistory().size());
        assertEquals(task1, testHistoryManager.getHistory().get(0));
    }

    @Test
    void addCopyInHistory_returnThreeTasks_HistoryWithThreeTasks(){
        testHistoryManager.add(task1);
        testHistoryManager.add(task2);
        testHistoryManager.add(task3);
        testHistoryManager.add(task1); // копия
        assertEquals(3, testHistoryManager.getHistory().size());
        assertArrayEquals(new Task[]{task1, task3, task2},
                testHistoryManager.getHistory().toArray(new Task[0]));
    }

    @Test
    void removeFirstTask_returnTasksExceptFirst_HistoryWithThreeTasks(){
        testHistoryManager.add(task1);
        testHistoryManager.add(task2);
        testHistoryManager.add(task3);
        testHistoryManager.remove(task3.getId());
        assertEquals(2, testHistoryManager.getHistory().size());
        assertArrayEquals(new Task[]{task2, task1},
                testHistoryManager.getHistory().toArray(new Task[0]));
    }

    @Test
    void removeMidTask_returnTasksExceptMid_HistoryWithThreeTasks(){
        testHistoryManager.add(task1);
        testHistoryManager.add(task2);
        testHistoryManager.add(task3);
        testHistoryManager.remove(task2.getId());
        assertNotNull(testHistoryManager.getHistory());
        assertEquals(2, testHistoryManager.getHistory().size());
        assertArrayEquals(new Task[]{task3, task1},
                testHistoryManager.getHistory().toArray(new Task[0]));
    }

    @Test
    void removeLastTask_returnTasksExceptLast_HistoryWithThreeTasks(){
        testHistoryManager.add(task1);
        testHistoryManager.add(task2);
        testHistoryManager.add(task3);
        testHistoryManager.remove(task1.getId());
        assertEquals(2, testHistoryManager.getHistory().size());
        assertArrayEquals(new Task[]{task3, task2},
                testHistoryManager.getHistory().toArray(new Task[0]));
    }
}