package Tasks;

public class Task {
    protected ProgressType type;
    protected String name;
    protected String description;

    // Конструктор для addTask
    public Task(String name, String description) {
        this.type = ProgressType.NEW;
        this.name = name;
        this.description = description;
    }

    // Конструктор для mergeTask
    protected Task(ProgressType type, String name, String description) {
        this.type = type;
        this.name = name;
        this.description = description;
    }

    // Конструкторы для update
    protected Task(ProgressType type) {
        this.type = type;
        this.name = null;
        this.description = null;
    }

    protected Task(String name) {
        this.type = null;
        this.name = name;
        this.description = null;
    }

    ProgressType getType() {
        return type;
    }

    String getName() {
        return name;
    }

    String getDescription() {
        return description;
    }

    // Слияние задач
    Object mergeTask(Object object) {
        Task task = (Task) object;
        if (task.getType() != null) {
            this.type = task.getType();
        }
        if (task.getName() != null) {
            this.name = task.getName();
        }
        if (task.getDescription() != null) {
            this.description = task.getDescription();
        }
        return new Task(this.type, this.name, this.description);
    }

    @Override
    public String toString() {
        return "(" + type.toString() + ") " + name + ": " + description + ".\n";
    }
}

