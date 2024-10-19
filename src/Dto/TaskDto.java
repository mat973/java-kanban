package Dto;

import task.Condition;

public class TaskDto {
    private int id;
    private String description;
    private Condition condition;
    private String name;

    public TaskDto(int id, String name , String description, Condition condition) {
        this.id = id;
        this.description = description;
        this.condition = condition;
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

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
