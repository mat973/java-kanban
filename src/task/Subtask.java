package task;

public class Subtask extends Task {
    private int epicId;


    public Subtask(int id, String name, String description, Status status, int epicId, long minutes, String startTime) {
        super(id, description, status, name, minutes, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }


}
