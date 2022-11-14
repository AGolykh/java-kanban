package history;

import tasks.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<Task> history = new ArrayList<>();

    // Добавление элемента в историю
    @Override
    public void add(Task task) {
        if (history.size() < 10) {
            history.add(task);
        } else {
            history.remove(0);
            history.add(task);
        }
    }

    // Вывод истории
    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }
}
