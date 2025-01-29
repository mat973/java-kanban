package dto;

import task.Status;

public class SubtaskDto extends TaskDto {
    private Integer epicId;


    public SubtaskDto(Integer id, String description, String name, Status status, Integer epicId) {
        super(id, description, name, status);
        this.epicId = epicId;
    }

    public SubtaskDto(Integer id, String name, String description, Status status, Integer epicId,
                      long minutes, String startTime) {
        super(id, name, description, status, minutes, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

}
