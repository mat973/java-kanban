package dto;

import task.Status;

public class SubtaskDto extends TaskDto {
    private int epicId;


    public SubtaskDto(int id, String name, String description, Status status, EpicDto epicDto) {
        super(id, name, description, status);
        this.epicId = epicDto.getId();
    }


    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
