package tasks.epic;

import tasks.DefaultTask;

import java.util.ArrayList;

public class Epic extends DefaultTask {
    public ArrayList<Integer> listSubTaskId;

    public Epic(String name, String description) {
        super(name, description);
        this.listSubTaskId = new ArrayList<>();
    }

    // Получение списка подзадач
    public ArrayList<Integer> getListSubTaskId() {
        return listSubTaskId;
    }

    // Добавление id подзадачи в список родительской
    public void addSubTaskId(int id) {
        listSubTaskId.add(id);
    }

    // Удаление id из списка подзадач эпика
    public void delSubTaskId(int id) {
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

