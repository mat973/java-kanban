package managers;

import task.Task;

import java.util.List;

public interface HistoryManager {

    void addToHistory(Task task);

    List<Task> getHistory();

    void remove(int id);

}
