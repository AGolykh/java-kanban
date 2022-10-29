package Tasks;

import java.util.TreeMap;

public class Epic extends Task {
    public TreeMap<Integer, SubTask> subTaskList;
    int nextIdSubs;
    // Конструктор для addTask
    public Epic(String name, String description) {
        super(name, description);
        subTaskList = new TreeMap<>();
    }

    // Конструктор для mergeTask
    protected Epic(ProgressType type, String name, String description, TreeMap<Integer, SubTask> subTaskList) {
    super(type, name, description);
    this.subTaskList = subTaskList;
}

    // Конструкторы для update
    protected Epic(ProgressType type) {
        super(type);
        this.subTaskList = null;
    }

    protected Epic(String name) {
        super(name);
        this.subTaskList = null;
    }

    // Список подзадач родительской задачи
    public String subTaskListToString() {
        StringBuilder result = new StringBuilder();
        for (Integer idSubTask : subTaskList.keySet()) {
            SubTask subTask = subTaskList.get(idSubTask);
            result.append(idSubTask).append(". ").append(subTask.toString());
        }
        return result.toString();
    }

    public int createId() {
        final int valueSubs = 1;

        if (nextIdSubs == Integer.MAX_VALUE || nextIdSubs < valueSubs) {
            nextIdSubs = valueSubs;
        }
        return nextIdSubs++;
    }

    @Override
    Object mergeTask(Object object) {
        Epic epic = (Epic) object;

        if (epic.getType() != null) {
            this.type = epic.getType();
        }

        if (epic.getName() != null) {
            this.name = epic.getName();
        }

        if (epic.getDescription() != null) {
            this.description = epic.getDescription();
        }

        if (epic.subTaskList != null) {
            this.subTaskList = epic.subTaskList;
        }
        return new Epic(this.type, this.name, this.description, this.subTaskList);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("(").
                append(type.toString()).
                append(") ").
                append(name).
                append(": ").
                append(description).
                append(".\n");
        for (Integer id : subTaskList.keySet()) {
            SubTask subtask = subTaskList.get(id);
            if (subtask.getType() != null) {
                result.append("-- ").
                        append(id).
                        append(". ").
                        append(subTaskList.get(id));
            }
        }
        return result.toString();
    }
}

