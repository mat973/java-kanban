package dto;

import task.Status;

public class EpicDto extends TaskDto{

    public EpicDto(int id, String name, String description, Status status) {
        super(id, name, description, status);
    }


}
