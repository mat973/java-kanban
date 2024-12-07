package managers;

public class Managers {
    public static TaskManager getDefault(HistoryManager historyManager) {
        return new InMemoryTaskManager(historyManager);
    }

    public static FileBackedTaskManager getFileTaskManager(HistoryManager historyManager) {
        return new FileBackedTaskManager(historyManager);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
