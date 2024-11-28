package managers;

import dto.EpicDto;
import dto.SubtaskDto;
import dto.TaskDto;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getTasks();

    List<Epic> getEpicTasks();

    List<Subtask> getSubTasks();

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
