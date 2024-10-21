package dto;

import task.Status;

public class TaskDto {
    private int id;
    private String description;
    private Status status;
    private String name;

    public TaskDto(int id, String name, String description, Status status) {
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

    public Status getCondition() {
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
}
