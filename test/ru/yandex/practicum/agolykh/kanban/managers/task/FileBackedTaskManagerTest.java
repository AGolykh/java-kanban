package ru.yandex.practicum.agolykh.kanban.managers.task;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.agolykh.kanban.exceptions.ManagerSaveException;
import ru.yandex.practicum.agolykh.kanban.managers.Managers;
import ru.yandex.practicum.agolykh.kanban.tasks.Epic;
import ru.yandex.practicum.agolykh.kanban.tasks.Status;
import ru.yandex.practicum.agolykh.kanban.tasks.SubTask;
import ru.yandex.practicum.agolykh.kanban.tasks.Task;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest extends TaskManagerTest {
    public FileBackedTaskManagerTest() {
        super.setManager(Managers.getFileBacked());
    }

    @Test
    void importFromFile_returnRightManager_normalFile() {
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
    void importFromFile_returnManagerWitOutSubTasks_withOutSubTasksFile() {
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
    void importFromFile_returnManagerWitOutHistory_withOutHistoryFile() {
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
    void importFromFile_returnEmptyManager_emptyFile() {
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
    void importFromFile_returnManagerSaveException_nullFile() {
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
    void checkSaveEmptyManager_returnEmptyManager_emptyData() {
        final String dir = System.getProperty("user.dir") + "\\resources\\";
        final String fileName = "FileForEmptyFileBackedManager.csv";
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

    @Test
    void checkSaveManager_returnManagerWithThreeTasks_managerWithThreeTasks() {
        final String dir = System.getProperty("user.dir") + "\\resources\\";
        final String fileName = "FileForSavingFileBackedManager.csv";
        final String path = dir + fileName;
        FileBackedTaskManager toFile = new FileBackedTaskManager();
        toFile.setPath(path);

        Task task = new Task(Status.DONE, "Задача 1", "Описание задачи 1");
        Epic epic = new Epic(Status.DONE, "Родительская задача 1", "Описание родительской задачи 1");
        SubTask subTask = new SubTask(Status.IN_PROGRESS, "Подзадача 1", "Описание подзадачи 1", 2);
        toFile.addTask(task);
        toFile.addEpic(epic);
        toFile.addSubTask(subTask);
        toFile.getTask(1);
        toFile.getSubTask(3);
        toFile.save();
        FileBackedTaskManager fromFile = FileBackedTaskManager.loadFromFile(new File(path));
        assertEquals(2, fromFile.getHistory().size());
        assertEquals(task, fromFile.getTask(1));
        assertEquals(epic, fromFile.getEpic(2));
        assertEquals(1, fromFile.getSubTaskList().size());

    }
}

