package managers;

import task.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private final static int HISTORY_LENGTH = 10;

    private final ArrayList<Task> history = new ArrayList<>();

    @Override
    public void addToHistory(Task task) {
        if (task == null) {
            System.out.println("Задача не может быть Null");
            return;
        }
        history.add(task);
        if (history.size() > HISTORY_LENGTH) {
            do {
                history.removeFirst();
            } while (history.size() > HISTORY_LENGTH);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(history);
    }

}
