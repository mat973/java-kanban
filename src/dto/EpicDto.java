package dto;

import task.Status;

public class EpicDto extends TaskDto {

    public EpicDto(Integer id, String description, String name, Status status) {
        super(id, description, name, status);
    }

    public EpicDto(Integer id, String name, String description, Status status, long minutes, String startTime) {
        super(id, name, description, status, minutes, startTime);
    }


}
