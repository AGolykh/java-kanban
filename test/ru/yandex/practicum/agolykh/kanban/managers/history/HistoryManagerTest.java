package ru.yandex.practicum.agolykh.kanban.managers.history;

import org.junit.jupiter.api.BeforeAll;
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

    @BeforeAll
    static void beforeAll() {
        testHistoryManager = Managers.getDefaultHistory();
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);
    }

    @BeforeEach
    void beforeEach() {
        for (Task task : testHistoryManager.getHistory()) {
            testHistoryManager.remove(task.getId());
        }
    }

    @Test
    void getHistoryFromEmptyHistoryManager(){
        assertEquals(0, testHistoryManager.getHistory().size());
        assertEquals(0, testHistoryManager.countOfNodes());
    }

    @Test
    void addHistoryToEmptyHistoryManager(){
        testHistoryManager.add(task1);
        assertNotNull(testHistoryManager.getHistory());
        assertEquals(1, testHistoryManager.getHistory().size());
        assertEquals(1, testHistoryManager.countOfNodes());
        assertArrayEquals(new Task[]{task1},
                testHistoryManager.getHistory().toArray(new Task[0]));
    }

    @Test
    void addCopyHistoryToHistoryManager(){
        testHistoryManager.add(task1);
        testHistoryManager.add(task2);
        testHistoryManager.add(task3);
        testHistoryManager.add(task1); // копия
        assertNotNull(testHistoryManager.getHistory());
        assertEquals(3, testHistoryManager.getHistory().size());
        assertEquals(3, testHistoryManager.countOfNodes());
        assertArrayEquals(new Task[]{task1, task3, task2},
                testHistoryManager.getHistory().toArray(new Task[0]));
    }

    @Test
    void removeFirstFromHistoryManager(){
        testHistoryManager.add(task1);
        testHistoryManager.add(task2);
        testHistoryManager.add(task3);
        testHistoryManager.remove(task3.getId());
        assertNotNull(testHistoryManager.getHistory());
        assertEquals(2, testHistoryManager.getHistory().size());
        assertEquals(2, testHistoryManager.countOfNodes());
        assertArrayEquals(new Task[]{task2, task1},
                testHistoryManager.getHistory().toArray(new Task[0]));
    }

    @Test
    void removeMidFromHistoryManager(){
        testHistoryManager.add(task1);
        testHistoryManager.add(task2);
        testHistoryManager.add(task3);
        testHistoryManager.remove(task2.getId());
        assertNotNull(testHistoryManager.getHistory());
        assertEquals(2, testHistoryManager.getHistory().size());
        assertEquals(2, testHistoryManager.countOfNodes());
        assertArrayEquals(new Task[]{task3, task1},
                testHistoryManager.getHistory().toArray(new Task[0]));
    }

    @Test
    void removeLastFromHistoryManager(){
        testHistoryManager.add(task1);
        testHistoryManager.add(task2);
        testHistoryManager.add(task3);
        testHistoryManager.remove(task1.getId());
        assertNotNull(testHistoryManager.getHistory());
        assertEquals(2, testHistoryManager.getHistory().size());
        assertEquals(2, testHistoryManager.countOfNodes());
        assertArrayEquals(new Task[]{task3, task2},
                testHistoryManager.getHistory().toArray(new Task[0]));
    }
}