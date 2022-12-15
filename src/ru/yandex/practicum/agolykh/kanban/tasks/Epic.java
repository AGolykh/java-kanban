package ru.yandex.practicum.agolykh.kanban.tasks;

import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<Integer> listSubTaskId;

    public Epic(String name, String description) {
        super(name, description);
        this.type = TaskTypes.EPIC;
        this.listSubTaskId = new ArrayList<>();
    }

    public Epic(String value) {
        super(value);
        this.type = TaskTypes.EPIC;
        this.listSubTaskId = new ArrayList<>();
    }

    // Получение списка подзадач
    public ArrayList<Integer> getListSubTaskId() {
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
                + ", name='" + name
                + ", description='" + description
                + ", listSubTasksId=" + listSubTaskId
                + '}';
    }

    public static String toString(Epic epic) {
        return String.valueOf(epic.getId()) +
                ',' +
                epic.getType() +
                ',' +
                epic.getStatus() +
                ',' +
                epic.getName() +
                ',' +
                epic.getDescription();
    }
}

