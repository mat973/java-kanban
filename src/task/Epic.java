package task;

import java.util.ArrayList;


public class Epic extends Task {
    private ArrayList<Subtask> subtasks;

     Epic(String name, String description, Condition condition) {
        super(name, description, condition);
        this.subtasks = new ArrayList<>();
    }


    protected ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }


    @Override
    public String toString() {
         String result ="[Название:" + getName() + ", описание:" + getDescription() + ", состояние:"
                 + getCondition().name() + ". Подзадачи:";
        for (int i = 0; i < subtasks.size(); i++) {
            result = result + subtasks.get(i).getName();
            if (i < subtasks.size() - 1)
                result+= ", ";

        }





        return result+ "]";
    }
}
