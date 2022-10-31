package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> listSubTaskId;

    public Epic(String name, String description) {
        super(name, description);
        this.listSubTaskId = new ArrayList<>();
    }

    ArrayList<Integer> getListSubTaskId() {
        return listSubTaskId;
    }

    void addSubTaskId(int id) {
        listSubTaskId.add(id);
    }

    // Удаление id из списка подзадач эпика
    void delSubTaskId(int id) {
        for (Integer subTaskId : listSubTaskId) {
            if (subTaskId == id) {
                listSubTaskId.remove(subTaskId);
                break;
            }
        }
    }

    @Override
    public String toString() {
        return "Epic{"
                + "id=" + id
                + ", status=" + status
                + ", name='" + name
                + '\'' + ", description='" + description
                + '\'' + "listSubTasksId=" + listSubTaskId
                + '}';
    }
}

