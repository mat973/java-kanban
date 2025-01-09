package task;

public class Subtask extends Task {
    private int epicId;

    public Subtask(int id, String name, String description, Status status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public Subtask(int id, String name, String description, Status status, int epicId, Long minutes, String startTime) {
        super(id, name, description, status, minutes, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }


}
