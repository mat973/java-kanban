package task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;


public class Epic extends Task {
    private ArrayList<Subtask> subtasks;
    private LocalDateTime endTime;



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

        result +=   (startTime.isPresent() && duration.isPresent() ?", время начала: "
                + startTime.get().format(outputFormater) + ", продолжительность: "
                + (duration.get().toHours() >= 1? (duration.get().toHours() + " часов ") : "")+
                (duration.get().toMinutesPart() >= 1? (duration.get().toHours() + " минут" ) : "")+ "]" : "]");


        return result;
    }

    @Override
    public LocalDateTime getEndTime() {
        return super.getEndTime();
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
