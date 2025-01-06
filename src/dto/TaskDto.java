package dto;

import task.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class TaskDto {
    private int id;
    private String description;
    private Status status;
    private String name;
    private Optional<Long> duration;
    private Optional<String> startTime;




    public TaskDto(int id, String name, String description, Status status,Long minutes, String startTime) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.name = name;
        this.duration = Optional.ofNullable(minutes);
        this.startTime = Optional.ofNullable(startTime);
    }

    public TaskDto(int id, String description, String name, Status status) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setCondition(Status status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Optional<Long> getDuration() {
        return duration;
    }

    public Optional<String> getStartTime() {
        return startTime;
    }
}
