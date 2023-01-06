package ru.yandex.practicum.agolykh.kanban.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    protected Integer id;
    protected TaskTypes type;
    protected Status status;
    protected String name;
    protected String description;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public Task(String name, String description) {
        this.type = TaskTypes.TASK;
        this.status = Status.NEW;
        this.name = name;
        this.description = description;
        this.duration = Duration.ofMinutes(0);
        this.startTime = LocalDateTime.of(8888,
                                        8,
                                        8,
                                        8,
                                        8);
    }

    public Task(String name, String description, int duration, String dateTime) {
        this.type = TaskTypes.TASK;
        this.status = Status.NEW;
        this.name = name;
        this.description = description;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = LocalDateTime.parse(dateTime, formatter);
    }

    public Task(Status status, String name, String description) {
        this.type = TaskTypes.TASK;
        this.status = status;
        this.name = name;
        this.description = description;
        this.duration = Duration.ofMinutes(0);
        this.startTime = LocalDateTime.of(8888,
                8,
                8,
                8,
                8);
    }

    public Task(Status status, String name, String description, int duration, String dateTime) {
        this.type = TaskTypes.TASK;
        this.status = status;
        this.name = name;
        this.description = description;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = LocalDateTime.parse(dateTime, formatter);
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration.toMinutes());
    }

    public DateTimeFormatter getFormatter() {
        return formatter;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "Task{"
                + "id=" + id
                + ", type=" + type
                + ", status=" + status
                + ", name='" + name
                + ", description='" + description
                + ", duration='" + duration.toMinutes()
                + ", startTime='" + startTime.format(formatter)
                + ", endTime='" + getEndTime().format(formatter)
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

