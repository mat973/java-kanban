package task;

public class Subtask extends Task {
    private int epicId;

     Subtask(String name, String description, Condition condition, Epic epic) {
        super(name, description, condition);
        this.epicId = epic.getId();
    }


    protected int getEpicId() {
        return epicId;
    }
}
