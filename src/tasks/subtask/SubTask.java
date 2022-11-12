package tasks.subtask;

import tasks.DefaultTask;
import tasks.Status;

public class SubTask extends DefaultTask {
    protected final int epicId;

    public SubTask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public SubTask(Status status, String name, String description, int epicId) {
        super(status, name, description);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "SubTask{"
                + "epicId=" + epicId
                + ", id=" + id
                + ", status=" + status
                + ", name='" + name
                + '\'' + ", description='" + description
                + '\'' + '}';
    }
}
