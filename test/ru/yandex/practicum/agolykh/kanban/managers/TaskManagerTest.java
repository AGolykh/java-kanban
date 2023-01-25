package ru.yandex.practicum.agolykh.kanban.managers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.agolykh.kanban.tasks.Epic;
import ru.yandex.practicum.agolykh.kanban.tasks.Status;
import ru.yandex.practicum.agolykh.kanban.tasks.SubTask;
import ru.yandex.practicum.agolykh.kanban.tasks.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class TaskManagerTest<T extends TaskManager> {
    public static TaskManager manager;
    public static ArrayList<Task> taskArray;
    public static ArrayList<Epic> epicArray;
    public static ArrayList<SubTask> subTaskArray;

    public void setManager(TaskManager manager) {
        TaskManagerTest.manager = manager;
    }

    @Test
    void getTaskList_returnListWithThreeTasks_managerWithThreeTasks() {
        addTasksTest();
        assertEquals(3, manager.getTaskList().size());
        assertTrue(manager.getTaskList().contains(taskArray.get(0)));
        assertTrue(manager.getTaskList().contains(taskArray.get(1)));
        assertTrue(manager.getTaskList().contains(taskArray.get(2)));
    }

    @Test
    void getNullTaskList_returnEmptyList_emptyManager() {
        assertTrue(manager.getTaskList().isEmpty());
    }

    @Test
    void getEpicList_returnListWithThreeEpics_managerWithThreeEpics() {
        addEpicsTest();
        assertEquals(3, manager.getEpicList().size());
        assertTrue(manager.getEpicList().contains(epicArray.get(0)));
        assertTrue(manager.getEpicList().contains(epicArray.get(1)));
        assertTrue(manager.getEpicList().contains(epicArray.get(2)));
    }

    @Test
    void getNullEpicList_returnEmptyList_emptyManager() {
        assertTrue(manager.getEpicList().isEmpty());
    }

    @Test
    void getSubTaskList_returnListWithThreeSubTasks_managerWithThreeSubTasks() {
        addEpicsTest();
        addSubTasksTest();
        assertEquals(6, manager.getSubTaskList().size());
        assertTrue(manager.getSubTaskList().contains(subTaskArray.get(0)));
        assertTrue(manager.getSubTaskList().contains(subTaskArray.get(1)));
        assertTrue(manager.getSubTaskList().contains(subTaskArray.get(2)));
        assertTrue(manager.getSubTaskList().contains(subTaskArray.get(3)));
        assertTrue(manager.getSubTaskList().contains(subTaskArray.get(4)));
        assertTrue(manager.getSubTaskList().contains(subTaskArray.get(5)));
    }

    @Test
    void getNullSubTaskList_returnEmptyList_emptyManager() {
        assertTrue(manager.getSubTaskList().isEmpty());
    }

    @Test
    void getSubTasksOfEpic_returnTwoSubTasksOfEpic_managerWithEpicsAndSubTasks() {
        addEpicsTest();
        addSubTasksTest();
        assertTrue(manager.getEpic(4).getListSubTaskId().contains(7));
        assertTrue(manager.getEpic(4).getListSubTaskId().contains(8));
    }

    @Test
    void getTask_returnTasks_managerWithTasks() {
        addTasksTest();
        assertEquals(2, manager.getTask(2).getId());
    }

    @Test
    void getTask_returnException_wrongId() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> {
                    addTasksTest();
                    manager.getTask(100);
                }
        );
        assertEquals("Задача " + 100 + " не найдена.", exception.getMessage());
    }

    @Test
    void getEpic_returnEpics_managerWithEpics() {
        addEpicsTest();
        assertEquals(4, manager.getEpic(4).getId());
    }

    @Test
    void getEpic_returnException_wrongId() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> {
                    addEpicsTest();
                    manager.getEpic(100);
                }
        );
        assertEquals("Родительская задача " + 100 + " не найдена.", exception.getMessage());
    }

    @Test
    void getSubTask_returnSubTasks_managerWithEpicsAndSubTasks() {
        addEpicsTest();
        addSubTasksTest();
        assertEquals(8, manager.getSubTask(8).getId());
    }

    @Test
    void getSub_returnException_wrongId() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> {
                    addEpicsTest();
                    addSubTasksTest();
                    manager.getSubTask(100);
                }
        );
        assertEquals("Подзадача " + 100 + " не найдена.", exception.getMessage());
    }

    @Test
    void addTask_returnListWithThreeTasks_emptyManager() {
        assertEquals(0, manager.getTaskList().size());
        addTasksTest();
        assertEquals(3, manager.getTaskList().size());
    }

    @Test
    void addEpic_returnListWithThreeTasks_emptyManager() {
        assertEquals(0, manager.getEpicList().size());
        addEpicsTest();
        assertEquals(3, manager.getEpicList().size());
    }

    @Test
    void addSubTask_returnListWithThreeTasks_emptyManager() {
        assertEquals(0, manager.getSubTaskList().size());
        addEpicsTest();
        addSubTasksTest();
        assertEquals(6, manager.getSubTaskList().size());
    }

    @Test
    void checkSubTaskInEpic_returnTwoIdSubTasksOfEpic_managerWithEpicsAndSubTasks() {
        addEpicsTest();
        assertTrue(manager.getEpic(4).getListSubTaskId().isEmpty());
        addSubTasksTest();
        assertTrue(manager.getEpic(4).getListSubTaskId().contains(manager.getSubTask(7).getId()));
        assertTrue(manager.getEpic(4).getListSubTaskId().contains(manager.getSubTask(8).getId()));
    }

    @Test
    void checkEpicIdInSubTask_returnEpicIdOfFromSubTask_managerWithEpicsAndSubTasks() {
        addEpicsTest();
        addSubTasksTest();
        assertEquals(6, manager.getSubTaskList().size());
        addEpicsTest();
        addSubTasksTest();
        assertEquals(4, manager.getSubTask(7).getEpicId());
    }

    @Test
    void updateTask_returnUpdatedTask_managerWithTasks() {
        addTasksTest();
        Task task = manager.getTask(3);
        task.setStatus(Status.IN_PROGRESS);
        task.setName("Какая-то задача");
        task.setDescription("Какое-то описание");
        manager.updateTask(task);
        assertEquals(task, manager.getTask(3));
    }

    @Test
    void updateEpic_returnUpdatedEpic_managerWithEpics() {
        addEpicsTest();
        Epic epic = manager.getEpic(4);
        epic.setName("Какая-то задача");
        epic.setDescription("Какое-то описание");
        manager.updateEpic(epic);
        assertEquals(epic, manager.getEpic(4));
    }

    @Test
    void updateSubTask_returnUpdatedSubTask_managerWithEpicsAndSubTasks() {
        addEpicsTest();
        addSubTasksTest();
        SubTask newSubTask = manager.getSubTask(7);
        newSubTask.setStatus(Status.IN_PROGRESS);
        newSubTask.setName("Какая-то задача");
        newSubTask.setDescription("Какое-то описание");
        manager.updateSubTask(newSubTask);
        assertEquals(newSubTask, manager.getSubTask(7));
    }

    @Test
    void deleteTask_returnExceptionAfterGetTask_managerWithTasks() {
        addTasksTest();
        assertEquals(2, manager.getTask(2).getId());
        manager.deleteTask(2);
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> manager.getTask(2)
        );
        assertEquals("Задача " + 2 + " не найдена.", exception.getMessage());
    }

    @Test
    void deleteTask_returnException_wrongId() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> {
                    addTasksTest();
                    manager.deleteTask(100);
                }
        );
        assertEquals("Задача " + 100 + " не найдена.", exception.getMessage());
    }

    @Test
    void deleteEpicWithRemoveSubTasks_returnExceptionAfterGetEpicAndSubTask_managerWithEpicsAndSubTasks() {
        addEpicsTest();
        addSubTasksTest();
        assertEquals(4, manager.getEpic(4).getId());
        assertTrue(manager.getEpic(4).getListSubTaskId().contains(manager.getSubTask(7).getId()));
        assertTrue(manager.getEpic(4).getListSubTaskId().contains(manager.getSubTask(8).getId()));
        assertEquals(7, manager.getSubTask(7).getId());
        manager.deleteEpic(4);
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> manager.getEpic(4)
        );
        assertEquals("Родительская задача " + 4 + " не найдена.", exception.getMessage());

        final NullPointerException exception2 = assertThrows(
                NullPointerException.class,
                () -> manager.getSubTask(7)
        );
        assertEquals("Подзадача " + 7 + " не найдена.", exception2.getMessage());
    }

    @Test
    void deleteEpic_returnException_wrongId() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> {
                    addEpicsTest();
                    manager.deleteEpic(100);
                }
        );
        assertEquals("Родительская задача " + 100 + " не найдена.", exception.getMessage());
    }

    @Test
    void deleteSubTask_returnExceptionAfterGetTask_managerWithEpicsAndSubTasks() {
        addEpicsTest();
        addSubTasksTest();
        assertEquals(12, manager.getSubTask(12).getId());
        manager.deleteSubTask(12);
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> {
                    addEpicsTest();
                    addSubTasksTest();
                    manager.deleteSubTask(100);
                }
        );
        assertEquals("Подзадача " + 100 + " не найдена.", exception.getMessage());
    }

    @Test
    void deleteSubTask_returnException_wrongId() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> {
                    addEpicsTest();
                    addSubTasksTest();
                    manager.deleteSubTask(100);
                }
        );
        assertEquals("Подзадача " + 100 + " не найдена.", exception.getMessage());
    }

    @Test
    void clearTaskList_returnEmptyTaskList_managerWithTasks() {
        addTasksTest();
        assertFalse(manager.getTaskList().isEmpty());
        manager.clearTaskList();
        assertTrue(manager.getTaskList().isEmpty());
    }

    @Test
    void clearEpicList_returnEmptyEpicList_managerWithEpics() {
        addEpicsTest();
        assertFalse(manager.getEpicList().isEmpty());
        manager.clearEpicList();
        assertTrue(manager.getEpicList().isEmpty());
    }

    @Test
    void clearSubTaskList_returnEmptySubTaskList_managerWithEpicsAndSubTasks() {
        addEpicsTest();
        addSubTasksTest();
        assertFalse(manager.getSubTaskList().isEmpty());
        manager.clearSubTaskList();
        assertTrue(manager.getSubTaskList().isEmpty());
    }

    @Test
    void clearSubTaskListAfterClearEpicList_returnEmptySubTaskList_managerWithEpicsAndSubTasks() {
        addEpicsTest();
        addSubTasksTest();
        assertFalse(manager.getSubTaskList().isEmpty());
        manager.clearEpicList();
        assertTrue(manager.getSubTaskList().isEmpty());
    }

    public void addTasksTest() {
        taskArray = new ArrayList<>();
        taskArray.add(new Task("Задача 1", "Описание задачи 1", 45, "16.01.2023 08:30"));
        taskArray.add(new Task("Задача 2", "Описание задачи 2", 45, "16.01.2023 09:30"));
        taskArray.add(new Task("Задача 3", "Описание задачи 3", 45, "16.01.2023 10:30"));
        for (int i = 0; i <= 2; i++) {
            taskArray.get(i).setId(i + 1);
        }

        for (Task task : taskArray) {
            manager.addTask(task);
        }
    }

    public void addEpicsTest() {
        epicArray = new ArrayList<>();
        epicArray.add(new Epic("Эпик 1", "Описание эпика 1"));
        epicArray.add(new Epic("Эпик 2", "Описание эпика 2"));
        epicArray.add(new Epic("Эпик 3", "Описание эпика 3"));

        for (int i = 0; i <= 2; i++) {
            epicArray.get(i).setId(i + 4);
        }

        for (Epic epic : epicArray) {
            manager.addEpic(epic);
        }
    }

    public void addSubTasksTest() {
        subTaskArray = new ArrayList<>();
        subTaskArray.add(new SubTask("Подзадача 1", "Описание подзадачи 1", 4, 45, "16.01.2023 11:30"));
        subTaskArray.add(new SubTask("Подзадача 2", "Описание подзадачи 2", 4, 45, "16.01.2023 12:30"));
        subTaskArray.add(new SubTask("Подзадача 3", "Описание подзадачи 3", 5, 45, "16.01.2023 13:30"));
        subTaskArray.add(new SubTask("Подзадача 4", "Описание подзадачи 4", 5, 45, "16.01.2023 15:30"));
        subTaskArray.add(new SubTask("Подзадача 5", "Описание подзадачи 5", 6, 45, "16.01.2023 16:30"));
        subTaskArray.add(new SubTask("Подзадача 6", "Описание подзадачи 6", 6, 45, "16.01.2023 17:30"));

        for (int i = 0; i <= 2; i++) {
            subTaskArray.get(i).setId(i + 7);
            subTaskArray.get(i + 3).setId(i + 10);
        }

        for (SubTask subTask : subTaskArray) {
            manager.addSubTask(subTask);
        }
    }
}

