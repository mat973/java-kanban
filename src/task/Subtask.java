package task;

public class Subtask extends Task {
    private int epicId;


    protected Subtask(int id, String name, String description, Status status, int epicId) {
        super(id, name, description, status);
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
