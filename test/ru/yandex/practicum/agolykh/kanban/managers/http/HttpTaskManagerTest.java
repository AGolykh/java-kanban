package ru.yandex.practicum.agolykh.kanban.managers.http;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.agolykh.kanban.local_server.HttpTaskServer;
import ru.yandex.practicum.agolykh.kanban.managers.TaskManager;
import ru.yandex.practicum.agolykh.kanban.managers.TaskManagerTest;
import ru.yandex.practicum.agolykh.kanban.remote_server.KVServer;
import ru.yandex.practicum.agolykh.kanban.tasks.Epic;
import ru.yandex.practicum.agolykh.kanban.tasks.Status;
import ru.yandex.practicum.agolykh.kanban.tasks.SubTask;
import ru.yandex.practicum.agolykh.kanban.tasks.Task;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    public static KVServer kvServer;
    public static HttpTaskServer httpTaskServer;

    public HttpTaskManagerTest() {
        super.setManager(httpTaskServer.getManager());
    }

    @BeforeAll
    static void beforeAll() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HttpTaskServer(8080);
        httpTaskServer.start();
    }

    @BeforeEach
    void beforeEach() {
        super.setManager(httpTaskServer.getManager());
        manager.clearTaskList();
        manager.clearEpicList();
        manager.clearSubTaskList();
    }

    @Test
    void importFromServer_returnRightManager_normalDataSet() {
        addTasksTest();
        addEpicsTest();
        addSubTasksTest();
        manager.getTask(1);
        manager.getEpic(4);
        manager.getSubTask(7);
        TaskManager fromServer = new HttpTaskManager("http://localhost:8078");
        assertEquals(3, fromServer.getHistory().size());
        assertEquals(3, fromServer.getTaskList().size());
        assertEquals(3, fromServer.getEpicList().size());
        assertEquals(6, fromServer.getSubTaskList().size());
        assertEquals(2, fromServer.getEpic(4).getListSubTaskId().size());
        assertTrue(fromServer.getEpic(4).getListSubTaskId().contains(7));
        assertTrue(fromServer.getEpic(5).getListSubTaskId().contains(9));
        assertTrue(fromServer.getEpic(6).getListSubTaskId().contains(11));
    }

    @Test
    void importFromServer_returnManagerWitOutSubTasks_withOutSubTasksDataSet() {
        addTasksTest();
        addEpicsTest();
        manager.getTask(1);
        manager.getEpic(4);
        TaskManager fromServer = new HttpTaskManager("http://localhost:8078");
        assertEquals(2, fromServer.getHistory().size());
        assertEquals(3, fromServer.getTaskList().size());
        assertEquals(3, fromServer.getEpicList().size());
        assertTrue(fromServer.getSubTaskList().isEmpty());
        assertTrue(fromServer.getEpic(4).getListSubTaskId().isEmpty());
    }

    @Test
    void importFromServer_returnManagerWitOutHistory_withOutHistoryDataSet() {
        addTasksTest();
        addEpicsTest();
        addSubTasksTest();
        TaskManager fromServer = new HttpTaskManager("http://localhost:8078");
        assertTrue(fromServer.getHistory().isEmpty());
        assertEquals(3, fromServer.getTaskList().size());
        assertEquals(3, fromServer.getEpicList().size());
        assertEquals(6, fromServer.getSubTaskList().size());
    }

    @Test
    void importFromServer_returnEmptyManager_emptyDataSet() {
        TaskManager fromServer =  new HttpTaskManager("http://localhost:8078");
        assertEquals(0, fromServer.getHistory().size());
        assertEquals(0, fromServer.getTaskList().size());
        assertEquals(0, fromServer.getEpicList().size());
        assertEquals(0, fromServer.getSubTaskList().size());
    }

    @Test
    void saveEmptyManager_returnEmptyManager_emptyDataSet() {
        assertEquals(0, manager.getTaskList().size());
        assertEquals(0, manager.getEpicList().size());
        assertEquals(0, manager.getSubTaskList().size());
        assertEquals(0, manager.getHistory().size());
        TaskManager fromServer =  new HttpTaskManager("http://localhost:8078");
        assertEquals(0, fromServer.getTaskList().size());
        assertEquals(0, fromServer.getEpicList().size());
        assertEquals(0, fromServer.getSubTaskList().size());
        assertEquals(0, fromServer.getHistory().size());
    }

    @Test
    void checkSaveManager_returnManagerWithThreeTasks_managerWithThreeTasks() {
        TaskManager toServer =  new HttpTaskManager("http://localhost:8078");
        Task task = new Task(Status.DONE, "Задача 1", "Описание задачи 1", 45, "16.01.2023 08:30");
        Epic epic = new Epic(Status.DONE, "Родительская задача 1", "Описание родительской задачи 1");
        SubTask subTask = new SubTask(Status.IN_PROGRESS, "Подзадача 1", "Описание подзадачи 1", 2, 45, "16.01.2023 09:30");
        toServer.addTask(task);
        toServer.addEpic(epic);
        toServer.addSubTask(subTask);
        toServer.getTask(1);
        toServer.getSubTask(3);
        TaskManager fromServer =  new HttpTaskManager("http://localhost:8078");
        assertEquals(2, fromServer.getHistory().size());
        assertEquals(task, fromServer.getTask(1));
        assertEquals(epic, fromServer.getEpic(2));
        assertEquals(1, fromServer.getSubTaskList().size());
    }
}