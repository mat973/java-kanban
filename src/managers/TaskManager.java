package managers;

import dto.EpicDto;
import dto.SubtaskDto;
import dto.TaskDto;
import exeptions.TaskIntersectionExeption;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;
import java.util.Set;

public interface TaskManager {
    List<Task> getTasks();

    List<Epic> getEpicTasks();

    List<Subtask> getSubTasks();

    boolean createTask(TaskDto taskDto) throws TaskIntersectionExeption;

    boolean createSubTusk(SubtaskDto subtaskDto);

    boolean createEpic(EpicDto epicDto);

    boolean removeAllTasks();

    boolean removeAllEpics();

    boolean removeAllSubTasks();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    boolean changeTask(TaskDto taskDto);

    boolean changeSubTask(SubtaskDto subtaskDto);

    boolean changeEpic(EpicDto epicDto);

    boolean removeTaskById(int id);

    boolean removeEpicById(int id);

    boolean removeSubtaskById(int id);

    Set<Task> getPrioritizedTasks();
}
