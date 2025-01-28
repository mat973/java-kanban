package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class Epic extends Task {
    private ArrayList<Subtask> subtasks;


    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, status);
        this.subtasks = new ArrayList<>();
    }

    public Epic(int id, String name, String description, Status status, Long minutes, String startTime) {
        super(id, name, description, status, minutes, startTime);
        this.subtasks = new ArrayList<>();
    }


    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }


    @Override
    public String toString() {
        String result = "ID:" + getId() + " [Название:" + getName() + ", описание:" + getDescription() + ", состояние:"
                + getStatus().name() + ", подзадачи:";
        for (int i = 0; i < subtasks.size(); i++) {
            result = result + "ID: " + subtasks.get(i).getId() + ", имя: " + subtasks.get(i).getName() + ", состояние: "
                    + subtasks.get(i).getStatus();
            if (i < subtasks.size() - 1)
                result += ", ";

        }

        result += (startTime != null && duration != null ? ", время начала: "
                + startTime.format(outputFormater) + ", продолжительность: "
                + (duration.toHours() >= 1 ? (duration.toHours() + " часов ") : "") +
                (duration.toMinutesPart() >= 1 ? (duration.toMinutesPart() + " минут") : "") + "]" : "]");


        return result;
    }

    @Override
    public LocalDateTime getEndTime() {
        return super.getEndTime();
    }


}
