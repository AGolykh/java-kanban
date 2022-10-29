package Tasks;

public class SubTask extends Task {
    protected int idEpic;

    // Конструктор для addTask
    public SubTask(int idEpic, String name, String description) {
        super(name, description);
        this.idEpic = idEpic;
    }

    // Конструктор для mergeTask
    protected SubTask(int idEpic, ProgressType type, String name, String description) {
        super(type, name, description);
        this.idEpic = idEpic;
    }

    // Конструкторы для update
    public SubTask(int idEpic, ProgressType type) {
        super(type);
        this.idEpic = idEpic;
    }

    protected SubTask(int idEpic, String name) {
        super(name);
        this.idEpic = idEpic;
    }

    public int getIdEpic() {
        return idEpic;
    }

    @Override
    Object mergeTask(Object object) {
        SubTask subTask = (SubTask) object;
        this.idEpic = subTask.getIdEpic();

        if (subTask.getType() != null) {
            this.type = subTask.getType();
        }

        if (subTask.getName() != null) {
            this.name = subTask.getName();
        }

        if (subTask.getDescription() != null) {
            this.description = subTask.getDescription();
        }
        return new SubTask(this.idEpic, this.type, this.name, this.description);
    }
}
