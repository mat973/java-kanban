package dto;

import task.Status;

public class EpicDto extends TaskDto {

    public EpicDto(int id, String name, String description, Status status,long minutes, String startTime) {
        super(id, name, description, status, minutes, startTime);
    }


}
