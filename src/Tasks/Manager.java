package Tasks;

import java.util.ArrayList;
import java.util.TreeMap;

public class Manager {
    protected static int nextID;
    public static ArrayList<Integer> listID;
    static TreeMap<Integer, Task> taskList;
    static TreeMap<Integer, Epic> epicList;
    static TreeMap<Integer, SubTask> subTaskList;

    public Manager() {
        listID = new ArrayList<>();
        taskList = new TreeMap<>();
        epicList = new TreeMap<>();
        subTaskList = new TreeMap<>();
    }

    @Override
    public String toString() {
        String result = "";
        for (Integer ID : listID) {
            if (taskList.containsKey(ID)) {
                result += taskList.get(ID);
            } else if (epicList.containsKey(ID)) {
                result += epicList.get(ID);
            } else if (subTaskList.containsKey(ID)) {
                result += subTaskList.get(ID);
            }
        }
        return result;
    }

    // Вывод списка задач
    public String showTask() {
        String result = "";
        for(Integer ID : taskList.keySet()) {
            result += taskList.get(ID);
        }
        return result;
    }

    // Вывод списка родительских задач
    public String showEpic() {
        String result = "";
        for(Integer ID : epicList.keySet()) {
            result += epicList.get(ID);
        }
        return result;
    }

    // Вывод списка подзадач
    public String showSubTask() {
        String result = "";
        for(Integer ID : subTaskList.keySet()) {
            result += subTaskList.get(ID);
        }
        return result;
    }

    // Удаление ID из списка
    private void delFromListID(int ID) {
        for (Integer IDFromListID : listID) {
            if (IDFromListID == ID) {
                listID.remove(IDFromListID);
                break;
            }
        }
    }

    // Создание сквозного идентификатора задач
    public static int createID() {
        final int firstId = 1;
        if (nextID == Integer.MAX_VALUE || nextID < firstId) {
            nextID = firstId;
        }
        return nextID++;
    }

    // Добавить задачу
    public void addTask(Object object) {
        if (object.getClass().equals(Task.class)) {
            Task task = (Task) object;
            int ID = task.getID();
            taskList.put(ID, task);
            listID.add(ID);
        } else if (object.getClass().equals(Epic.class)) {
            Epic epic = (Epic) object;
            int ID = epic.getID();
            epicList.put(ID, epic);
            listID.add(ID);
        } else {
            SubTask subTask = (SubTask) object;
            int ID = subTask.getID();
            int epicID = subTask.getEpicID();
            if (epicList.containsKey(epicID)) {
                Epic epic = epicList.get(epicID);
                subTaskList.put(ID, subTask);
                epic.addSubTaskID(ID);
                listID.add(ID);
                checkStatus(ID);
            } else {
                System.out.println("Родительская с номером " + epicID + " отсутствует.");
            }
        }

    }

    public void update(int ID, Status status, String name, String description) {
        if (taskList.containsKey(ID)) {
            taskList.get(ID).updateTask(status, name, description);
            System.out.println("Задача обновлена.");
        } else if (epicList.containsKey(ID)) {
            epicList.get(ID).updateTask(name, description);
            if (status != null) {
                System.out.println("У родительской задачи нельзя менять статус.");
            }
            System.out.println("Задача обновлена.");
        } else if (subTaskList.containsKey(ID)) {
            subTaskList.get(ID).updateTask(status, name, description);
            checkStatus(subTaskList.get(ID).getEpicID());
            System.out.println("Задача обновлена.");
        } else {
            System.out.println("Задача отсутствует.");
        }
    }

    // Удалить задачу
    public void delTask(int ID) {
        if (taskList.containsKey(ID)) {
            taskList.remove(ID);
            delFromListID(ID);
            System.out.println("Задача " + ID + " удалена");
        } else if (epicList.containsKey(ID)) {
            Epic epic = epicList.get(ID);
            for (Integer subTaskID : epic.getSubTasksID()) {
                delFromListID(subTaskID);
            }
            epicList.remove(ID);
            delFromListID(ID);
            System.out.println("Родительская " + ID + " удалена");
        } else if (subTaskList.containsKey(ID)) {
            SubTask subTask = subTaskList.get(ID);
            int epicID = subTask.getEpicID();
            if (epicList.containsKey(epicID)) {
                Epic epic = epicList.get(epicID);
                subTaskList.remove(ID);
                delFromListID(ID);
                epic.delFromListSubTasksID(ID);
                epicList.put(epicID, epic);
                System.out.println("Подзадача " + ID + " удалена");
                checkStatus(ID);
            } else {
                System.out.println("Подзадача не обнаружена.");
            }
        } else {
            System.out.println("Задача не обнаружена.");
        }
    }

    // Очистить список обычных задач
    public void clearTaskList() {
        for (Integer taskID : taskList.keySet()) {
            delFromListID(taskID);
        }
        taskList.clear();
        System.out.println("Список обычных задач удален.");
    }

    // Очистить список родительских задач
    public void clearEpicList() {
        for (Integer epicID : epicList.keySet()) {
            delFromListID(epicID);
        }
        epicList.clear();
        for (Integer subTaskID : subTaskList.keySet()) {
            delFromListID(subTaskID);
        }
        subTaskList.clear();
        System.out.println("Список родительских задач c подзадачами удален.");
    }

    // Очистить список подзадач
    public void clearSubTaskList() {
        for (Integer subTaskID : subTaskList.keySet()) {
            delFromListID(subTaskID);
        }
        subTaskList.clear();
        for (Integer ID : epicList.keySet()) {
            Epic epic = epicList.get(ID);
            epic.listSubTasksID.clear();
            epicList.put(ID, epic);
            checkStatus(ID);
        }
        System.out.println("Список подзадач удален.");
    }

    // Проверка статуса эпика
    private void checkStatus(int ID) {
        if (epicList.containsKey(ID)) {
            int countOfNew = 0;
            int countOfDone = 0;
            Epic epic = epicList.get(ID);
            for (Integer idSubTask : epic.getSubTasksID()) {
                SubTask subTask = subTaskList.get(idSubTask);
                Status status = subTask.getStatus();
                if (status.equals(Status.NEW)) {
                    countOfNew++;
                } else if (status.equals(Status.DONE)) {
                    countOfDone++;
                }
            }

            if (countOfNew == epic.getSubTasksID().size()
                    || epic.getSubTasksID().isEmpty()) {
                epic.setStatus(Status.NEW);
            } else if (countOfDone == epic.getSubTasksID().size()) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
            epicList.put(ID, epic);
        }
    }

    // Получение всех подзадач определенного эпика
    public String getSubTaskOfEpic(int ID) {
        String result = "";
        if (epicList.containsKey(ID)) {
            Epic epic = epicList.get(ID);
            for (Integer subTaskID : epic.getSubTasksID()) {
                if (subTaskList.containsKey(subTaskID)) {
                    result += subTaskList.get(subTaskID);
                }
            }
        }
        return result;
    }
}
