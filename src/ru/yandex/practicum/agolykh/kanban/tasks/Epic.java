package ru.yandex.practicum.agolykh.kanban.tasks;

import java.util.HashSet;
import java.util.Set;

public class Epic extends Task {
    public Set<Integer> listSubTaskId;

    public Epic(String name, String description) {
        super(name, description);
        this.type = TaskTypes.EPIC;
        this.listSubTaskId = new HashSet<>();
    }

    public Epic(Status status, String name, String description) {
        super(status, name, description);
        this.type = TaskTypes.EPIC;
        this.listSubTaskId = new HashSet<>();
    }

    // Получение списка подзадач
    public Set<Integer> getListSubTaskId() {
        return listSubTaskId;
    }

    // Добавление id подзадачи в список родительской
    public void addSubTaskId(int id) {
        listSubTaskId.add(id);
    }

    // Удаление id из списка подзадач эпика
    public void delSubTaskId(int id) {
        for (Integer subTaskId : listSubTaskId) {
            if (subTaskId == id) {
                listSubTaskId.remove(subTaskId);
                break;
            }
        }
    }

    @Override
    public String toString() {
        return "Epic{"
                + "id=" + id
                + ", type=" + type
                + ", status" + status
                + ", name='" + name
                + ", description='" + description
                + ", listSubTasksId=" + listSubTaskId
                + ", duration='" + duration.toMinutes()
                + ", startTime='" + startTime.format(formatter)
                + ", endTime='" + getEndTime().format(formatter)
                + '}';
    }
}

