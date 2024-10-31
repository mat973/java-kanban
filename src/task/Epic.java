package task;

import java.util.ArrayList;


public class Epic extends Task {
    private ArrayList<Subtask> subtasks;

    protected Epic(Epic epic) {
        super(epic);
        this.subtasks = epic.subtasks;
    }

    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, status);
        this.subtasks = new ArrayList<>();
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }


    @Override
    public String toString() {
        String result = "ID:" + getId() + " [Название:" + getName() + ", описание:" + getDescription() + ", состояние:"
                + getStatus().name() + ". Подзадачи:";
        for (int i = 0; i < subtasks.size(); i++) {
            result = result + "ID: " + subtasks.get(i).getId() + ", имя: " + subtasks.get(i).getName() + ", состояние: "
                    + subtasks.get(i).getStatus();
            if (i < subtasks.size() - 1)
                result += ", ";

        }


        return result + "]";
    }
}
