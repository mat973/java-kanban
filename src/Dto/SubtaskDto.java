package Dto;

import task.Condition;
import task.Epic;

public class SubtaskDto extends TaskDto{
    private int epicId;


    public SubtaskDto(int id, String name, String description, Condition condition, EpicDto epicDto) {
        super(id, name, description, condition);
        this.epicId = epicDto.getId();
    }


    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
