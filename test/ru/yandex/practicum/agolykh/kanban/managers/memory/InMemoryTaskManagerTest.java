package ru.yandex.practicum.agolykh.kanban.managers.memory;

import ru.yandex.practicum.agolykh.kanban.managers.Managers;
import ru.yandex.practicum.agolykh.kanban.managers.TaskManagerTest;

class InMemoryTaskManagerTest extends TaskManagerTest {
    public InMemoryTaskManagerTest() {
        super.setManager(Managers.getInMemory());
    }
}


