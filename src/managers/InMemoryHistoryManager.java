package managers;

import task.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private final int HISTORY_LENGTH = 10;

    private final ArrayList<Task> history = new ArrayList<>();

    @Override
    public void addToHistory(Task task) {
        if (task == null) {
            System.out.println("Задача не может быть Null");
            //по логике эта строчка не может быть, тк она вызывается из getById, а я проверяю их на contain in Map...
            // короче не могу представить этот кейс, я понимаю что я не всегда буду знать код наизусть
            // и он может быть и не мой
            // и хороша практика делать так. окей
            return;
        }
        history.add(task);
        if (history.size() > HISTORY_LENGTH) {
            do {
                history.removeFirst();
            } while (history.size() == HISTORY_LENGTH);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }

}
