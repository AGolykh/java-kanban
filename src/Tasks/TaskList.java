package Tasks;

import java.util.TreeMap;

public class TaskList {
    static TreeMap<Integer, Object> taskList;

    public TaskList() {
        taskList = new TreeMap<>();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (!taskList.isEmpty()) {
            for (Integer id : taskList.keySet()) {
                Task task = (Task) taskList.get(id);
                result.append(id).append(". (").
                        append(task.getType()).
                        append(") ").
                        append(task.getName()).
                        append(": ").
                        append(task.getDescription()).
                        append(".\n");
            }
            return result.toString();
        } else {
            return "Список задач пуст.";
        }
    }

    // Подробный toString
    public String showList() {
        if (!taskList.isEmpty()) {
            StringBuilder result = new StringBuilder();
            for (Integer id : taskList.keySet()) {
                result.append(id).
                        append(". ").
                        append(taskList.get(id).toString());
            }
            return result.toString();
        } else {
            return "Список задач пуст.";
        }
    }

    // Получить объект задачи по id
    public Object getTask(int id) {
        Object result = null;
        if (!isSubTask(id) && taskList.containsKey(id)) {
            result =  taskList.get(id);
        } else if (isSubTask(id) && taskList.containsKey(id /1000)) {
            result = getSubTasks(id);
        }
        return result;
    }

    private SubTask getSubTasks(int id) {
        SubTask result = null;
        if (taskList.get(id / 1000).getClass().equals(Epic.class)) {
            Epic epic = (Epic) taskList.get(id / 1000);
            if (epic.subTaskList.containsKey(id)) {
                result = epic.subTaskList.get(id);
            }
        }
        return result;
    }

    public String subList(int id) {
        String result = null;
        if (taskList.containsKey(id)
                && taskList.get(id).getClass().equals(Epic.class)) {
            Epic epic = (Epic) taskList.get(id);
            result = epic.subTaskListToString();
        }
        return result;
    }
    // Является ли id идентификатором подзадачи
    private boolean isSubTask(int id) {
        return id > 1000;
    }

    // Очистить список
    public void clear() {
        taskList.clear();
        System.out.println("Список удален.");
    }

    // Удалить задачу
    public void delTask(int id) {
        if (!isSubTask(id) && taskList.containsKey(id)) {
            taskList.remove(id);
            System.out.println("Задача c идентификатором " + id + " удалена\n");
        } else if (isSubTask(id) && taskList.containsKey(id / 1000)) {
            Epic epic = (Epic) taskList.get(id / 1000);
            if (epic.subTaskList.containsKey(id)) {
                epic.subTaskList.remove(id);
                System.out.println("Подзадача c идентификатором " + id + " удалена\n");
            } else {
                System.out.println("Подзадача c идентификатором " + id + " отсутствует\n");
            }
        } else {
            System.out.println("Задача c идентификатором " + id + " отсутствует\n");
        }
    }

    // Добавить задачу
    public void addTask(Object task) {
        if (task.getClass().equals(Epic.class)) {
            Epic epic = (Epic) task;
            int id = Id.instance.createId();
            taskList.put(id, epic);
        } else if (task.getClass().equals(Task.class)) {
            Task epic = (Task) task;
            int id = Id.instance.createId();
            taskList.put(id, epic);
        } else {
            SubTask subTask = (SubTask) task;
            if (taskList.containsKey(subTask.getIdEpic())) {
                Epic epic = (Epic) taskList.get(subTask.getIdEpic());
                int id = subTask.getIdEpic() * 1000 + epic.createId();
                epic.subTaskList.put(id, subTask);
                checkStatus(subTask.getIdEpic());
            } else {
                System.out.println("Нет родительской задачи для " + subTask.getIdEpic());
            }
        }
    }

    // Обновление записи в списке
    public void update(int id, Object object) {
        if (object.getClass().equals(Task.class)) {
            updateTask(id, object);
        } else if (object.getClass().equals(Epic.class)) {
            updateEpic(id, object);
        } else if (object.getClass().equals(SubTask.class)) {
            updateSubTask(id, object);
        }
    }

    // Обновления задачи
    private void updateTask(int id, Object object) {
        if (!isSubTask(id)
                && taskList.containsKey(id)
                && taskList.get(id).getClass().equals(Task.class)) {

            Task newTask = (Task) object;
            Task oldTask = (Task) taskList.get(id);
            Task mergedTask = (Task) oldTask.mergeTask(newTask);

            taskList.put(id, mergedTask);
            System.out.println("Задача <<" + mergedTask.getName() + ">> обновлена");
        } else {
            System.out.println("Задача " + id + "  отсутствует или не является обычной");
        }
    }

    // Обновления Эпик-задачи
    private void updateEpic(int id, Object object) {
        if (!isSubTask(id)
                && taskList.containsKey(id)
                && taskList.get(id).getClass().equals(Epic.class)) {

            Epic newEpic = (Epic) object;
            Epic oldEpic = (Epic) taskList.get(id);
            ProgressType oldEpicStatus = oldEpic.getType();
            String oldEpicName = oldEpic.getName();
            Epic mergedEpic = (Epic) oldEpic.mergeTask(newEpic);

            if (!oldEpicStatus.equals(mergedEpic.getType())
                    || !oldEpicName.equals(mergedEpic.getName())) {
                System.out.println("Задача <<" + mergedEpic.getName() + ">> обновлена");
            }
        } else {
            System.out.println("Задача " + id + " отсутствует или не является родительской");
        }
    }

    // Обновления подзадачи
    private void updateSubTask(int id, Object object) { // Причесать
        if ((isSubTask(id)
                && taskList.containsKey(id / 1000))
                && taskList.get(id / 1000).getClass().equals(Epic.class)) {

            Epic epic = (Epic) taskList.get(id / 1000);
            SubTask newSubTask = (SubTask) object;
            SubTask oldSubtask = epic.subTaskList.get(id);
            int newIdEpic = newSubTask.getIdEpic();

            if (((id / 1000) == newIdEpic)
                    && taskList.containsKey(newIdEpic)
                    && epic.subTaskList.containsKey(id)) {
                oldSubtask.mergeTask(newSubTask);
                epic.subTaskList.put(id, oldSubtask);
                System.out.println("Подзадача <<" + oldSubtask.getName() + ">> обновлена.");

                checkStatus(id / 1000);
            } else {
                System.out.println("В родительской задаче " + newIdEpic + " нет подзадачи c идентификатором " + id);
            }
        } else {
            System.out.println("Нет задачи c идентификатором " + id / 1000);
        }
    }

    // Проверка статусов подзадач и замена статуса родительской
    private void checkStatus(int id) {
        if (!isSubTask(id)
                && taskList.containsKey(id)
                && taskList.get(id).getClass().equals(Epic.class)) {
            int countOfNew = 0;
            int countOfDone = 0;
            int countOfCancelled = 0;
            Epic epic = (Epic) taskList.get(id);
            for (Integer idSubTask : epic.subTaskList.keySet()) {
                ProgressType type = epic.subTaskList.get(idSubTask).getType();
                if (type.equals(ProgressType.NEW)) {
                    countOfNew++;
                } else if (type.equals(ProgressType.DONE)) {
                    countOfDone++;
                } else if (type.equals(ProgressType.CANCELLED)) {
                    countOfCancelled++;
                }
            }

            if (countOfNew == epic.subTaskList.size()
                    || epic.subTaskList.isEmpty()) {
                epic.mergeTask(new Epic(ProgressType.NEW));
            } else if (countOfDone == epic.subTaskList.size()) {
                epic.mergeTask(new Epic(ProgressType.DONE));
            } else if (countOfCancelled == epic.subTaskList.size()) {
                epic.mergeTask(new Epic(ProgressType.CANCELLED));
            } else {
                epic.mergeTask(new Epic(ProgressType.IN_PROGRESS));
            }

            update(id, epic);
        }
    }
}
