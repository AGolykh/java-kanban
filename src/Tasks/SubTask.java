package Tasks;

public class SubTask extends Task {
    protected final int epicID;

    public SubTask(String name, String description, int epicID) {
        super(name, description);
        this.epicID = epicID;
    }

    public SubTask(int ID, Status status, String name, String description, int epicID) {
        super(ID, status, name, description);
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }

    // Получить объект задачи
    SubTask getTask() {
        return this;
    }

    @Override
    public String toString() {
        return ID + ". " + "Подзадача задачи " + epicID +
                ", (" + status + ") " + name +
                ": " + description + ". \n";
    }
}
