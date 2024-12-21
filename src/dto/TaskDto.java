package dto;

import task.Status;

import java.time.Duration;
import java.time.LocalDateTime;

public class TaskDto {
    private int id;
    private String description;
    private Status status;
    private String name;
    private long duration;
    private String startTime;




    public TaskDto(int id, String name, String description, Status status,long minutes, String startTime) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.name = name;
        this.duration = minutes;
        this.startTime = startTime;
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

    public long getDuration() {
        return duration;
    }

    public String getStartTime() {
        return startTime;
    }
}
