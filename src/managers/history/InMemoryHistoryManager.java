package managers.history;

import tasks.DefaultTask;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    public static ArrayList<DefaultTask> history = new ArrayList<>();

    // Добавление элемента в историю
    @Override
    public void add(DefaultTask task) {
        if (history.size() < 10) {
            history.add(task);
        } else {
            history.remove(0);
            history.add(task);
        }
    }

    // Вывод истории
    @Override
    public ArrayList<DefaultTask> getHistory() {
        return history;
    }
}
