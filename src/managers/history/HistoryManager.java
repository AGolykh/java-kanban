package managers.history;

import tasks.DefaultTask;

import java.util.ArrayList;

public interface HistoryManager {

    void add(DefaultTask task);

    ArrayList<DefaultTask> getHistory();
}
