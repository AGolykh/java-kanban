package Tasks;

import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> listSubTasksID;

    public Epic(String name, String description) {
        super(name, description);
        this.listSubTasksID = new ArrayList<>();
    }

    public Epic(int ID, Status status, String name, String description, ArrayList<Integer> listSubTasksID) {
        super(ID, status, name, description);
        this.listSubTasksID = listSubTasksID;
    }

    ArrayList<Integer> getSubTasksID() {
        return listSubTasksID;
    }

    void addSubTaskID(int ID) {
        listSubTasksID.add(ID);
    }

    // Удаление ID из списка подзадач эпика
    void delFromListSubTasksID(int ID) {
        for (Integer IDFromSubTaskListID : listSubTasksID) {
            if (IDFromSubTaskListID == ID) {
                listSubTasksID.remove(IDFromSubTaskListID);
                break;
            }
        }
    }

    // Получить объект задачи
    Epic getTask() {
        return this;
    }

    // Обновленной задачи
    void updateTask(String name, String description) {
        this.setName(name == null ? this.name : name);
        this.setDescription(description == null ? this.description : description);
    }

    @Override
    public String toString() {
        return ID + ". " + "Родительская" +
                ", (" + status + ") " + name +
                ": " + description + ". \n";
    }
}

