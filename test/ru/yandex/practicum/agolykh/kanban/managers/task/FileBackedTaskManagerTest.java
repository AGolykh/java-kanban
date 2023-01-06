package ru.yandex.practicum.agolykh.kanban.managers.task;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.agolykh.kanban.exceptions.ManagerSaveException;
import ru.yandex.practicum.agolykh.kanban.managers.Managers;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest extends TaskManagerTest {
    public FileBackedTaskManagerTest() {
        super.setManager(Managers.getFileBacked());
    }

    @Test
    void importFromFile() {
        final String dir = System.getProperty("user.dir") + "\\resources\\";
        final String fileName = "Normal.csv";
        final String path = dir + fileName;
        TaskManager fromFile;
        fromFile = FileBackedTaskManager.loadFromFile(new File(path));
        assertEquals(6, fromFile.getHistory().size());
        assertEquals(3, fromFile.getTaskList().size());
        assertEquals(2, fromFile.getEpicList().size());
        assertEquals(5, fromFile.getSubTaskList().size());
        assertEquals(3, fromFile.getEpic(3).getListSubTaskId().size());
        assertTrue(fromFile.getEpic(3).listSubTaskId.contains(4));
        assertTrue(fromFile.getEpic(3).listSubTaskId.contains(5));
        assertTrue(fromFile.getEpic(3).listSubTaskId.contains(17));
    }

    @Test
    void importFromFileWitOutSubTasks() {
        final String dir = System.getProperty("user.dir") + "\\resources\\";
        final String fileName = "WithOutSubTasks.csv";
        final String path = dir + fileName;
        TaskManager fromFile;
        fromFile = FileBackedTaskManager.loadFromFile(new File(path));
        assertEquals(4, fromFile.getHistory().size());
        assertEquals(3, fromFile.getTaskList().size());
        assertEquals(2, fromFile.getEpicList().size());
        assertEquals(0, fromFile.getSubTaskList().size());
        assertEquals(0, fromFile.getEpic(3).getListSubTaskId().size());
    }

    @Test
    void importFromFileWitOutHistory() {
        final String dir = System.getProperty("user.dir") + "\\resources\\";
        final String fileName = "WithOutHistory.csv";
        final String path = dir + fileName;
        TaskManager fromFile;
        fromFile = FileBackedTaskManager.loadFromFile(new File(path));
        assertEquals(0, fromFile.getHistory().size());
        assertEquals(3, fromFile.getTaskList().size());
        assertEquals(2, fromFile.getEpicList().size());
        assertEquals(5, fromFile.getSubTaskList().size());
    }

    @Test
    void importFromEmptyFile() {
        final String dir = System.getProperty("user.dir") + "\\resources\\";
        final String fileName = "Empty.csv";
        final String path = dir + fileName;
        TaskManager fromFile;
        fromFile = FileBackedTaskManager.loadFromFile(new File(path));
        assertEquals(0, fromFile.getHistory().size());
        assertEquals(0, fromFile.getTaskList().size());
        assertEquals(0, fromFile.getEpicList().size());
        assertEquals(0, fromFile.getSubTaskList().size());
    }

    @Test
    void importFromNullFile() {
        final ManagerSaveException exception = assertThrows(
                ManagerSaveException.class,
                () -> {
                    final String dir = System.getProperty("user.dir") + "\\resources\\";
                    final String fileName = "NullFile.csv";
                    final String path = dir + fileName;
                    TaskManager fromFile;
                    fromFile = FileBackedTaskManager.loadFromFile(new File(path));
                }
        );
        assertEquals("Произошла ошибка при импорте данных из файла.", exception.getMessage());
    }

    @Test
    void checkSaveEmptyManager() {
        final String dir = System.getProperty("user.dir") + "\\resources\\";
        final String fileName = "fileForEmptyFileBackedManager.csv";
        final String path = dir + fileName;
        FileBackedTaskManager toFile = new FileBackedTaskManager();
        toFile.setPath(path);
        assertEquals(0, toFile.getTaskList().size());
        assertEquals(0, toFile.getEpicList().size());
        assertEquals(0, toFile.getSubTaskList().size());
        assertEquals(0, toFile.getHistory().size());
        toFile.save();
        FileBackedTaskManager fromFile = FileBackedTaskManager.loadFromFile(new File(path));
        assertEquals(0, fromFile.getTaskList().size());
        assertEquals(0, fromFile.getEpicList().size());
        assertEquals(0, fromFile.getSubTaskList().size());
        assertEquals(0, fromFile.getHistory().size());

    }
}

