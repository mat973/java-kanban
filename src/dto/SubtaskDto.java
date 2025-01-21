package dto;

import task.Status;

public class SubtaskDto extends TaskDto {
    private int epicId;

    public SubtaskDto(int id, String description, String name, Status status, EpicDto epicDto) {
        super(id, description, name, status);
        this.epicId = epicDto.getId();
    }

    public SubtaskDto(int id, String name, String description, Status status, EpicDto epicDto,
                      long minutes, String startTime) {
        super(id, name, description, status, minutes, startTime);
        this.epicId = epicDto.getId();
    }

    public int getEpicId() {
        return epicId;
    }

}
