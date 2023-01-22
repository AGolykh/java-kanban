package ru.yandex.practicum.agolykh.kanban.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class SubTask extends Task {
    protected final int epicId;

    public SubTask(String name, String description, int epicId) {
        super(name, description);
        this.type = TaskTypes.SUBTASK;
        this.epicId = epicId;
    }

    public SubTask(Status status, String name, String description, int epicId) {
        this(name, description, epicId);
        this.status = status;
    }

    public SubTask(String name, String description, int epicId, int duration, String startTime) {
        this(name, description, epicId);
        this.duration = Duration.ofMinutes(duration);
        this.startTime = LocalDateTime.parse(startTime,
                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        this.endTime = getEndTime();
    }

    public SubTask(Status status, String name, String description, int epicId, int duration, String startTime) {
        this(name, description, epicId, duration, startTime);
        this.status = status;
    }

    public int getEpicId() {
        return epicId;
    }

    public String toString() {
        Long optionalDuration = Optional.ofNullable(duration)
                .map(Duration::toMinutes).orElse(null);
        String optionalStartTime = Optional.ofNullable(startTime)
                .map((time) -> time.format(Task.getFormatter()))
                .orElse(null);
        String optionalEndTime = Optional.ofNullable(getEndTime())
                .map((time) -> time.format(Task.getFormatter()))
                .orElse(null);

        return "SubTask{"
                + "id=" + id
                + ", type=" + type
                + ", status=" + status
                + ", name='" + name
                + ", description='" + description
                + ", epicId='" + epicId
                + ", duration='" + optionalDuration
                + ", startTime='" + optionalStartTime
                + ", endTime='" + optionalEndTime
                + '}';
    }
}
