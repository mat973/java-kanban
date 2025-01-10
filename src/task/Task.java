package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task implements Comparable<Task> {
    public static DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd MM yyyy HH:mm");
    public static DateTimeFormatter outputFormater = DateTimeFormatter.ofPattern(
            "Вермя начала запланированно на HH часов mm минут dd-го MMMM yyyy года");
    protected int id;
    protected String description;
    protected Status status;
    protected String name;
    protected Duration duration;
    protected LocalDateTime startTime;


    public Task(int id, String name, String description, Status status, Long minutes, String startTime) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.name = name;
        this.duration = Duration.ofMinutes(minutes);
        this.startTime = LocalDateTime.parse(startTime, inputFormatter);
    }

    public Task(int id, String name, String description, Status status) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.name = name;
    }

    public LocalDateTime getEndTime() {
        return this.startTime.plus(this.duration);
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCondition(Status status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ID:" + getId() + " [Название:" + getName() + ", описание:" + getDescription() + ", состояние:"
                + getStatus().name() +
                (startTime != null && duration != null ? ", время начала: "
                        + startTime.format(outputFormater) + ", продолжительность: "
                        + (duration.toHours() >= 1 ? (duration.toHours() + " часов ") : "") +
                        (duration.toMinutesPart() >= 1 ? (duration.toMinutesPart() + " минут") : "") + "]" :
                        "]");
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


    @Override
    public int compareTo(Task o) {
        if (this.getStartTime().isAfter(o.getStartTime())) {
            return 1;
        } else if (this.startTime.isBefore(o.startTime)) {
            return -1;
        } else {
//            if (o instanceof Epic || this instanceof Epic) {
            if (this.getId() == o.getId()) {
                return 0;
            } else {
                return Integer.compare(this.getId(), o.getId());
            }
//            }else {
//                return 0;
//            }
        }

    }
}
