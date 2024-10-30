package task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager{

    private final ArrayList<Task> history = new ArrayList<>();
    @Override
    public void addToHistory(Task task) {
        history.add(task);
        if (history.size() == 11){
            history.removeFirst();
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }

}
