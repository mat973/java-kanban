package task;

import dto.EpicDto;
import dto.SubtaskDto;
import dto.TaskDto;

import java.util.ArrayList;

public interface TaskManager{
    ArrayList<Task> getTasks();

    ArrayList<Epic> getEpicTasks();

    ArrayList<Subtask> getSubTasks();

    void createTask(TaskDto taskDto);

    void createSubTusk(SubtaskDto subtaskDto);

    void createEpic(EpicDto epicDto);

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubTasks();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    void changeTask(TaskDto taskDto);

    void changeSubTask(SubtaskDto subtaskDto);

    void changeEpic(EpicDto epicDto);

    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubtaskById(int id);


}
