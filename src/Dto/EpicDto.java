package Dto;

import task.Condition;

public class EpicDto extends TaskDto{

    public EpicDto(int id, String name, String description, Condition condition) {
        super(id, name, description, condition);
    }

    public EpicDto(int id) {
        super(id, null, null, null);
    }

    public EpicDto(int id, String name, String description) {
        super(id, name, description, null);
    }
}
