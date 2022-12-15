package ru.yandex.practicum.agolykh.kanban.tasks;


public class SubTask extends Task {
    protected final int epicId;

    public SubTask(String name, String description, int epicId) {
        super(name, description);
        this.type = TaskTypes.SUBTASK;
        this.epicId = epicId;
    }

    public SubTask(Status status, String name, String description, int epicId) {
        super(status, name, description);
        this.type = TaskTypes.SUBTASK;
        this.epicId = epicId;
    }

    public SubTask(String value) {
        super(value);
        this.type = TaskTypes.SUBTASK;
        this.epicId = Integer.parseInt(value.split(",")[5]);
    }

    public int getEpicId() {
        return epicId;
    }

    public String toString() {
        return "SubTask{"
                + "id=" + id
                + ", type=" + type
                + ", status=" + status
                + ", name='" + name
                + ", description='" + description
                + ", epicId='" + epicId
                + '}';
    }

    public static String toString(SubTask subTask) {
        return String.valueOf(subTask.getId()) +
                ',' +
                subTask.getType() +
                ',' +
                subTask.getStatus() +
                ',' +
                subTask.getName() +
                ',' +
                subTask.getDescription() +
                ',' +
                subTask.getEpicId();
    }
}
