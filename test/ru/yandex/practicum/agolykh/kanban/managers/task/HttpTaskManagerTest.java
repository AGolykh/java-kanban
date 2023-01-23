package ru.yandex.practicum.agolykh.kanban.managers.task;

import org.junit.jupiter.api.BeforeAll;
import ru.yandex.practicum.agolykh.kanban.http.KVServer;
import ru.yandex.practicum.agolykh.kanban.managers.Managers;
import ru.yandex.practicum.agolykh.kanban.tasks.Epic;
import ru.yandex.practicum.agolykh.kanban.tasks.SubTask;
import ru.yandex.practicum.agolykh.kanban.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;


class HttpTaskManagerTest extends TaskManagerTest {
    public static KVServer kvServer;
    public HttpTaskManagerTest() {
        super.setManager(Managers.getHttpTaskManager("http://localhost:8078"));
    }

    @BeforeAll
    static void beforeAll() {
        try {
            kvServer = new KVServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        kvServer.start();
    }

    @Override
    void addTasksTest() {
        taskArray = new ArrayList<>();
        taskArray.add(new Task("Задача 1", "Описание задачи 1", 45, "14.01.2023 07:30"));
        taskArray.add(new Task("Задача 2", "Описание задачи 2",45, "14.01.2023 08:30"));
        taskArray.add(new Task("Задача 3", "Описание задачи 3", 45, "14.01.2023 09:30"));
        for (int i = 0; i <= 2; i++) {
            taskArray.get(i).setId(i + 1);
        }

        for (Task task : taskArray) {
            taskManager.addTask(task);
        }
    }

    @Override
    void addEpicsTest() {
        epicArray = new ArrayList<>();
        epicArray.add(new Epic("Эпик 1", "Описание эпика 1"));
        epicArray.add(new Epic("Эпик 2", "Описание эпика 2"));
        epicArray.add(new Epic("Эпик 3", "Описание эпика 3"));

        for (int i = 0; i <= 2; i++) {
            epicArray.get(i).setId(i + 4);
        }

        for (Epic epic : epicArray) {
            taskManager.addEpic(epic);
        }
    }

    @Override
    void addSubTasksTest() {
        subTaskArray = new ArrayList<>();
        subTaskArray.add(new SubTask("Подзадача 1", "Описание подзадачи 1", 4, 45, "14.01.2023 10:30"));
        subTaskArray.add(new SubTask("Подзадача 2", "Описание подзадачи 2", 4, 45, "14.01.2023 11:30"));
        subTaskArray.add(new SubTask("Подзадача 3", "Описание подзадачи 3", 5, 45, "14.01.2023 12:30"));
        subTaskArray.add(new SubTask("Подзадача 4", "Описание подзадачи 4", 5, 45, "14.01.2023 13:30"));
        subTaskArray.add(new SubTask("Подзадача 5", "Описание подзадачи 5", 6, 45, "14.01.2023 14:30"));
        subTaskArray.add(new SubTask("Подзадача 6", "Описание подзадачи 6", 6, 45, "14.01.2023 15:30"));

        for (int i = 0; i <= 2; i++) {
            subTaskArray.get(i).setId(i + 7);
            subTaskArray.get(i + 3).setId(i + 10);
        }

        for (SubTask subTask : subTaskArray) {
            taskManager.addSubTask(subTask);
        }
    }
}