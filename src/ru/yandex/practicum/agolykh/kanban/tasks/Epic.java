package ru.yandex.practicum.agolykh.kanban.tasks;

import java.time.Duration;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Epic extends Task {
    public Set<Integer> listSubTaskId;

    public Epic(String name, String description) {
        super(name, description);
        this.type = TaskTypes.EPIC;
        this.listSubTaskId = new HashSet<>();
    }

    public Epic(Status status, String name, String description) {
        this(name, description);
        this.status = status;
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
        Long optionalDuration = Optional.ofNullable(duration)
                .map(Duration::toMinutes).orElse(null);
        String optionalStartTime = Optional.ofNullable(startTime)
                .map((time) -> time.format(formatter)).orElse(null);
        String optionalEndTime = Optional.ofNullable(getEndTime())
                .map((time) -> time.format(formatter)).orElse(null);

        return "Epic{"
                + "id=" + id
                + ", type=" + type
                + ", status=" + status
                + ", name='" + name
                + ", description='" + description
                + ", listSubTasksId=" + listSubTaskId
                + ", duration='" + optionalDuration
                + ", startTime='" + optionalStartTime
                + ", endTime='" + optionalEndTime
                + '}';
    }
}

