package ru.yandex.practicum.agolykh.kanban.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class Task {
    protected Integer id;
    protected TaskTypes type;
    protected Status status;
    protected String name;
    protected String description;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;

    public void setEndTime() {
        this.endTime = this.getEndTime();
    }

    public Task(String name, String description) {
        this.status = Status.NEW;
        this.name = name;
        this.description = description;
    }

    public Task(String name, String description, int duration, String startTime) {
        this.type = TaskTypes.TASK;
        this.status = Status.NEW;
        this.name = name;
        this.description = description;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = LocalDateTime.parse(startTime, Task.getFormatter());
        this.endTime = getEndTime();
    }

    public Task(Status status, String name, String description, int duration, String startTime) {
        this(name, description, duration, startTime);
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskTypes getType() {
        return type;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        if ((this.startTime == null) || (this.duration == null)) {
            return null;
        }
        return startTime.plusMinutes(duration.toMinutes());
    }

    public static DateTimeFormatter getFormatter() {
        return DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    }

    @Override
    public String toString() {
        Long optionalDuration = Optional.ofNullable(duration)
                .map(Duration::toMinutes)
                .orElse(null);
        String optionalStartTime = Optional.ofNullable(startTime)
                .map((time) -> time.format(Task.getFormatter()))
                .orElse(null);
        String optionalEndTime = Optional.ofNullable(getEndTime())
                .map((time) -> time.format(Task.getFormatter()))
                .orElse(null);

        return "Task{"
                + "id=" + id
                + ", type=" + type
                + ", status=" + status
                + ", name='" + name
                + ", description='" + description
                + ", duration='" + optionalDuration
                + ", startTime='" + optionalStartTime
                + ", endTime='" + optionalEndTime
                + '}';
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return id.equals(task.id)
                && type.equals(task.type)
                && status.equals(task.status)
                && name.equals(task.name)
                && description.equals(task.description);
    }
}

