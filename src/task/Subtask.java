package task;

public class Subtask extends Task {
    private int epicId;

     Subtask(String name, String description, Condition condition, Epic epic) {
        super(name, description, condition);
        this.epicId = epic.getId();
    }

    protected Subtask(int id, String name, String description, Condition condition, int epicId) {
        super(id, name, description, condition);
        this.epicId = epicId;
    }

    protected int getEpicId() {
        return epicId;
    }

    protected Subtask(Subtask subtask) {
        super(subtask);
        this.epicId = subtask.epicId;
    }
}
