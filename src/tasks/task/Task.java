package tasks.task;

import tasks.DefaultTask;
import tasks.Status;

public class Task extends DefaultTask {
    public Task(String name, String description) {
        super(name, description);
    }

    public Task(Status status, String name, String description) {
        super(status, name, description);
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

