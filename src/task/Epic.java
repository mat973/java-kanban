package task;

import java.util.ArrayList;


public class Epic extends Task {
    private ArrayList<Subtask> subtasks;

     Epic(String name, String description, Condition condition) {
        super(name, description, condition);
        this.subtasks = new ArrayList<>();
    }

    protected Epic(Epic epic) {
        super(epic);
        this.subtasks = epic.subtasks;
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }


    @Override
    public String toString() {
         String result ="[Название:" + getName() + ", описание:" + getDescription() + ", состояние:"
                 + getCondition().name() + ". Подзадачи:";
        for (int i = 0; i < subtasks.size(); i++) {
            result = result +"ID: "+ subtasks.get(i).getId() + ", имя: " + subtasks.get(i).getName() + ", состояние: "
            + subtasks.get(i).getCondition();
            if (i < subtasks.size() - 1)
                result+= ", ";

        }





        return result+ "]";
    }
}
