package ru.yandex.practicum.agolykh.kanban.tasks;

public class Task {
    protected Integer id;
    protected TaskTypes type;
    protected Status status;
    protected String name;
    protected String description;

    public Task(String name, String description) {
        this.type = TaskTypes.TASK;
        this.status = Status.NEW;
        this.name = name;
        this.description = description;
    }

    public Task(Status status, String name, String description) {
        this.type = TaskTypes.TASK;
        this.status = status;
        this.name = name;
        this.description = description;
    }

    public Task(String value) {
        String[] elements = value.split(",");
        this.id = Integer.parseInt(elements[0]);
        this.type = TaskTypes.TASK;
        this.status = Status.of(elements[2]);
        this.name = elements[3];
        this.description = elements[4];
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

    @Override
    public String toString() {
        return "Task{"
                + "id=" + id
                + ", type=" + type
                + ", status=" + status
                + ", name='" + name
                + ", description='" + description
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
        return id.equals(task.id);
    }

    public static String toString(Task task) {
        return String.valueOf(task.getId()) +
                ',' +
                task.getType() +
                ',' +
                task.getStatus() +
                ',' +
                task.getName() +
                ',' +
                task.getDescription();
    }
}

