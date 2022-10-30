package Tasks;

public class Task {
    protected final int ID;
    protected Status status;
    protected String name;
    protected String description;

    public Task(String name, String description) {
        this.ID = Manager.createID();
        this.status = Status.NEW;
        this.name = name;
        this.description = description;
    }

    public Task(int ID, Status status, String name, String description) {
        this.ID = ID;
        this.status = status;
        this.name = name;
        this.description = description;
    }

    int getID() {
        return ID;
    }

    Status getStatus() {
        return status;
    }

    void setStatus(Status status) {
        this.status = status;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    // Получить объект задачи
    Task getTask() {
        return this;
    }

    // Обновленной задачи
    void updateTask(Status status, String name, String description) {
        this.setStatus(status == null ? this.status : status);
        this.setName(name == null ? this.name : name);
        this.setDescription(description == null ? this.description : description);
    }

    @Override
    public String toString() {
        return ID + ". " + "Задача" +
                ", (" + status + ") " + name +
                ": " + description + ". \n";
    }
}

