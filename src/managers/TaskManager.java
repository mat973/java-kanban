package managers;

import dto.EpicDto;
import dto.SubtaskDto;
import dto.TaskDto;
import exeptions.EpicNotExistException;
import exeptions.SubtaskNotFoundException;
import exeptions.TaskIntersectionException;
import exeptions.TaskNotFoundException;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TaskManager {
    List<Task> getTasks();

    List<Epic> getEpicTasks();

    List<Subtask> getSubTasks();

    void createTask(TaskDto taskDto) throws TaskIntersectionException;

    void createSubTusk(SubtaskDto subtaskDto) throws EpicNotExistException, TaskIntersectionException;

    void createEpic(EpicDto epicDto);

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubTasks();

    Optional<Task> getTaskById(int id) throws TaskNotFoundException;

    Optional<Epic> getEpicById(int id) throws EpicNotExistException;

    Optional<Subtask> getSubtaskById(int id) throws EpicNotExistException;

    void changeTask(TaskDto taskDto) throws TaskNotFoundException, TaskIntersectionException;

    void changeSubTask(SubtaskDto subtaskDto) throws SubtaskNotFoundException, TaskIntersectionException;

    void changeEpic(EpicDto epicDto);

    void removeTaskById(int id) throws TaskNotFoundException;

    void removeEpicById(int id) throws EpicNotExistException;

    void removeSubtaskById(int id) throws SubtaskNotFoundException;

    Set<Task> getPrioritizedTasks();
}
