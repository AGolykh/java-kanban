package ru.yandex.practicum.agolykh.kanban.managers.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.agolykh.kanban.tasks.Epic;
import ru.yandex.practicum.agolykh.kanban.tasks.Status;
import ru.yandex.practicum.agolykh.kanban.tasks.SubTask;
import ru.yandex.practicum.agolykh.kanban.tasks.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class TaskManagerTest<T extends TaskManager> {
    static TaskManager taskManager;
    static ArrayList<Task> taskArray;
    static ArrayList<Epic> epicArray;
    static ArrayList<SubTask> subTaskArray;

    public TaskManager getManager() {
        return taskManager;
    }

    public void setManager(TaskManager manager) {
        this.taskManager = manager;
    }

    @BeforeEach
    void beforeEach() {
        taskManager = getManager();
    }

    @Test
    void getTaskList() {
        addTasksTest();
        assertEquals(3, taskManager.getTaskList().size());
        assertArrayEquals(taskArray.toArray(new Task[0]),
                taskManager.getTaskList().toArray(new Task[0]));
    }

    @Test
    void getNullTaskList() {
        assertEquals(0, taskManager.getTaskList().size());
    }

    @Test
    void getEpicList() {
        addEpicsTest();
        assertEquals(3, taskManager.getEpicList().size());
        assertArrayEquals(epicArray.toArray(new Epic[0]),
                taskManager.getEpicList().toArray(new Epic[0]));
    }

    @Test
    void getNullEpicList() {
        assertEquals(0, taskManager.getEpicList().size());
    }

    @Test
    void getSubTaskList() {
        addEpicsTest();
        addSubTasksTest();
        assertEquals(6, taskManager.getSubTaskList().size());
        assertArrayEquals(subTaskArray.toArray(new SubTask[0]),
                taskManager.getSubTaskList().toArray(new SubTask[0]));
    }

    @Test
    void getSubTasksOfEpic() {
        addEpicsTest();
        addSubTasksTest();
        assertNotNull(taskManager.getEpic(4));
        assertEquals("[SubTask{id=7," +
                " type=SUBTASK," +
                " status=NEW," +
                " name='Подзадача 1," +
                " description='Описание подзадачи 1," +
                " epicId='4," +
                " duration='0," +
                " dateTime='01.01.2000 00:00," +
                " endTime='01.01.2000 00:00}," +
                " SubTask{id=8," +
                " type=SUBTASK," +
                " status=NEW," +
                " name='Подзадача 2," +
                " description='Описание подзадачи 2," +
                " epicId='4," +
                " duration='0," +
                " dateTime='01.01.2000 00:00," +
                " endTime='01.01.2000 00:00}]",
                taskManager.getSubTasksOfEpic(4).toString());
        assertEquals("[7, 8]", taskManager.getEpic(4).getListSubTaskId().toString());
    }

    @Test
    void getTask() {
        addTasksTest();
        assertEquals(2, taskManager.getTask(2).getId());
    }

    @Test
    void getTaskWithWrongId() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> {
                    addTasksTest();
                    taskManager.getTask(100);
                }
        );
        assertEquals("Задача " + 100 + " не найдена.", exception.getMessage());
    }

    @Test
    void getEpic() {
        addEpicsTest();
        assertEquals(4, taskManager.getEpic(4).getId());
    }

    @Test
    void getEpicWithWrongId() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> {
                    addEpicsTest();
                    taskManager.getEpic(100);
                }
        );
        assertEquals("Родительская задача " + 100 + " не найдена.", exception.getMessage());
    }

    @Test
    void getSubTask() {
        addEpicsTest();
        addSubTasksTest();
        assertEquals(8, taskManager.getSubTask(8).getId());
    }

    @Test
    void getSubTaskWithWrongId() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> {
                    addEpicsTest();
                    addSubTasksTest();
                    taskManager.getSubTask(100);
                }
        );
        assertEquals("Подзадача " + 100 + " не найдена.", exception.getMessage());
    }

    @Test
    void addTask() {
        assertEquals(0, taskManager.getTaskList().size());
        addTasksTest();
        assertEquals(3, taskManager.getTaskList().size());
    }

    @Test
    void addEpic() {
        assertEquals(0, taskManager.getEpicList().size());
        addEpicsTest();
        assertEquals(3, taskManager.getEpicList().size());
    }

    @Test
    void addSubTask() {
        assertEquals(0, taskManager.getSubTaskList().size());
        addEpicsTest();
        addSubTasksTest();
        assertEquals(6, taskManager.getSubTaskList().size());
    }

    @Test
    void checkSubTaskInEpic() {
        addEpicsTest();
        assertEquals("[]", taskManager.getEpic(4).getListSubTaskId().toString());
        addSubTasksTest();
        assertEquals("[7, 8]", taskManager.getEpic(4).getListSubTaskId().toString());
    }

    @Test
    void checkEpicIdInSubTask() {
        addSubTasksTest();
        assertEquals(0, taskManager.getSubTaskList().size());
        addEpicsTest();
        addSubTasksTest();
        assertEquals(4, taskManager.getSubTask(7).getEpicId());
    }

    @Test
    void updateTask() {
        addTasksTest();
        Task newTask = new Task(Status.IN_PROGRESS,
                "Какая-то задача",
                "Какое-то описание");
        newTask.setId(3);
        assertNotEquals(newTask, taskManager.getTask(3));
        taskManager.updateTask(3, newTask);
        assertEquals(newTask, taskManager.getTask(3));
    }

    @Test
    void updateEpic() {
        addEpicsTest();
        Epic newEpic = new Epic("Какая-то задача",
                "Какое-то описание");
        newEpic.setId(4);
        assertNotEquals(newEpic, taskManager.getEpic(4));
        taskManager.updateEpic(4, newEpic);
        assertEquals(newEpic, taskManager.getEpic(4));
    }

    @Test
    void updateSubTask() {
        addEpicsTest();
        addSubTasksTest();
        SubTask newSubTask = new SubTask(Status.IN_PROGRESS,
                "Какая-то задача",
                "Какое-то описание",
                4);
        newSubTask.setId(7);
        assertNotEquals(newSubTask, taskManager.getSubTask(7));
        taskManager.updateSubTask(7, newSubTask);
        assertEquals(newSubTask, taskManager.getSubTask(7));
    }

    @Test
    void deleteTask() {
        addTasksTest();
        assertEquals(2, taskManager.getTask(2).getId());
        taskManager.deleteTask(2);
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> taskManager.getTask(2)
        );
        assertEquals("Задача " + 2 + " не найдена.", exception.getMessage());
    }

    @Test
    void deleteTaskWithWrongId() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> {
                    addTasksTest();
                    taskManager.deleteTask(100);
                }
        );
        assertEquals("Задача " + 100 + " не найдена.", exception.getMessage());
    }

    @Test
    void deleteEpicWithRemoveSubTasks() {
        addEpicsTest();
        addSubTasksTest();
        assertEquals(4, taskManager.getEpic(4).getId());
        assertEquals("[7, 8]", taskManager.getEpic(4).getListSubTaskId().toString());
        assertEquals(7, taskManager.getSubTask(7).getId());
        taskManager.deleteEpic(4);
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> taskManager.getEpic(4)
        );
        assertEquals("Родительская задача " + 4 + " не найдена.", exception.getMessage());

        final NullPointerException exception2 = assertThrows(
                NullPointerException.class,
                () -> taskManager.getSubTask(7)
        );
        assertEquals("Подзадача " + 7 + " не найдена.", exception2.getMessage());
    }

    @Test
    void deleteEpicWithWrongId() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> {
                    addEpicsTest();
                    taskManager.deleteEpic(100);
                }
        );
        assertEquals("Родительская задача " + 100 + " не найдена.", exception.getMessage());
    }

    @Test
    void deleteSubTask() {
        addEpicsTest();
        addSubTasksTest();
        assertEquals(12, taskManager.getSubTask(12).getId());
        taskManager.deleteSubTask(12);
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> {
                    addEpicsTest();
                    addSubTasksTest();
                    taskManager.deleteSubTask(100);
                }
        );
        assertEquals("Подзадача " + 100 + " не найдена.", exception.getMessage());
    }

    @Test
    void deleteSubTaskWithWrongId() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> {
                    addEpicsTest();
                    addSubTasksTest();
                    taskManager.deleteSubTask(100);
                }
        );
        assertEquals("Подзадача " + 100 + " не найдена.", exception.getMessage());
    }

    @Test
    void clearTaskList() {
        addTasksTest();
        assertNotEquals(0, taskManager.getTaskList().size());
        taskManager.clearTaskList();
        assertEquals(0, taskManager.getTaskList().size());
    }

    @Test
    void clearEpicList() {
        addEpicsTest();
        assertNotEquals(0, taskManager.getEpicList().size());
        taskManager.clearEpicList();
        assertEquals(0, taskManager.getEpicList().size());
    }

    @Test
    void clearSubTaskList() {
        addEpicsTest();
        addSubTasksTest();
        assertNotEquals(0, taskManager.getSubTaskList().size());
        taskManager.clearSubTaskList();
        assertEquals(0, taskManager.getSubTaskList().size());
    }

    @Test
    void clearSubTaskListAfterClearEpicList() {
        addEpicsTest();
        addSubTasksTest();
        taskManager.clearEpicList();
        assertEquals(0, taskManager.getSubTaskList().size());
    }

    @Test
    void checkStatusChangeInProgressAnDone() {
        addEpicsTest();
        addSubTasksTest();
        Epic epic = taskManager.getEpic(4);
        assertEquals(Status.NEW, epic.getStatus());
        taskManager.updateSubTask(7, new SubTask(Status.IN_PROGRESS, null, null, 4));
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
        taskManager.updateSubTask(7, new SubTask(Status.DONE, null, null, 4));
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
        taskManager.updateSubTask(8, new SubTask(Status.DONE, null, null, 4));
        assertEquals(Status.DONE, epic.getStatus());

    }

    @Test
    void getHistoryAndCountOfNodes() {
        addTasksTest();
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getTask(3);
        taskManager.getTask(2);
        assertEquals(3, taskManager.getHistory().size());
        assertEquals(3, taskManager.countOfNodes());
    }

    void addTasksTest() {
        taskArray = new ArrayList<>();
        taskArray.add(new Task("Задача 1", "Описание задачи 1"));
        taskArray.add(new Task("Задача 2", "Описание задачи 2"));
        taskArray.add(new Task("Задача 3", "Описание задачи 3"));
        for (int i = 0; i <= 2; i++) {
            taskArray.get(i).setId(i + 1);
        }

        for (Task task : taskArray) {
            taskManager.addTask(task);
        }
    }

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

    void addSubTasksTest() {
        subTaskArray = new ArrayList<>();
        subTaskArray.add(new SubTask("Подзадача 1", "Описание подзадачи 1", 4));
        subTaskArray.add(new SubTask("Подзадача 2", "Описание подзадачи 2", 4));
        subTaskArray.add(new SubTask("Подзадача 3", "Описание подзадачи 3", 5));
        subTaskArray.add(new SubTask("Подзадача 4", "Описание подзадачи 4", 5));
        subTaskArray.add(new SubTask("Подзадача 5", "Описание подзадачи 5", 6));
        subTaskArray.add(new SubTask("Подзадача 6", "Описание подзадачи 6", 6));

        for (int i = 0; i <= 2; i++) {
            subTaskArray.get(i).setId(i + 7);
            subTaskArray.get(i + 3).setId(i + 10);
        }

        for (SubTask subTask : subTaskArray) {
            taskManager.addSubTask(subTask);
        }
    }
}

