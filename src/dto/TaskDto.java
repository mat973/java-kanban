package dto;

import task.Status;

public class TaskDto {
    private int id;
    private String description;
    private Status status;
    private String name;
    private Long duration;
    private String startTime;


    public TaskDto(int id, String name, String description, Status status, Long minutes, String startTime) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.name = name;
        this.duration = minutes;
        this.startTime = startTime;
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

    public Long getDuration() {
        return duration;
    }

    public String getStartTime() {
        return startTime;
    }
}
