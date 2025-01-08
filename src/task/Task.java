package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class Task implements Comparable<Task> {
    public static DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd MM yyyy HH:mm");
    public static DateTimeFormatter outputFormater = DateTimeFormatter.ofPattern(
            "Вермя начала запланированно на HH часов mm минут dd-го MMMM yyyy года");
    protected int id;
    protected String description;
    protected Status status;
    protected String name;
    protected Optional<Duration> duration;
    protected Optional<LocalDateTime> startTime;





        public Task(int id, String name, String description, Status status,  Long minutes, String startTime) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.name = name;
        this.duration = Optional.of(Duration.ofMinutes(minutes));
        this.startTime = Optional.of(LocalDateTime.parse(startTime, inputFormatter));
    }

    public Task(int id, String name, String description, Status status) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.name = name;
        this.duration = Optional.empty();
        this.startTime = Optional.empty();
    }

    public LocalDateTime getEndTime(){
            return this.startTime.get().plus(this.duration.get());
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

    public Optional<Duration> getDuration() {
        return duration;
    }

    public void setDuration(Optional<Duration> duration) {
        this.duration = duration;
    }

    public Optional<LocalDateTime> getStartTime() {
        return startTime;
    }

    public void setStartTime(Optional<LocalDateTime> startTime) {
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
                (startTime.isPresent() && duration.isPresent() ?", время начала: "
                        + startTime.get().format(outputFormater) + ", продолжительность: "
                + (duration.get().toHours() >= 1? (duration.get().toHours() + " часов ") : "")+
                (duration.get().toMinutesPart() >= 1? (duration.get().toMinutesPart() + " минут" ) : "")+ "]" : "]");
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
        if (this.startTime.get().isAfter(o.startTime.get())){
            return 1;
        } else if (this.startTime.get().isBefore(o.startTime.get())) {
            return -1;
        }else {
            return 0;
        }

    }
}
