package tasks.task;

import tasks.Status;

public class Task {
    protected int id;
    protected Status status;
    protected String name;
    protected String description;

    public Task(String name, String description) {
        this.status = Status.NEW;
        this.name = name;
        this.description = description;
    }

    public Task(Status status, String name, String description) {
        this.status = status;
        this.name = name;
        this.description = description;
    }

    public int getId() {
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

    @Override
    public String toString() {
        return "Task{"
                + "id=" + id
                + ", status=" + status
                + ", name='" + name
                + '\'' + ", description='" + description
                + '\'' + '}';
    }
}

