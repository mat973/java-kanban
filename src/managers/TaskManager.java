package managers;

import dto.EpicDto;
import dto.SubtaskDto;
import dto.TaskDto;
import exeptions.*;
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

    Task createTask(TaskDto taskDto) throws TaskIntersectionException;

    Subtask createSubTusk(SubtaskDto subtaskDto) throws EpicNotExistException, TaskIntersectionException;

    Epic createEpic(EpicDto epicDto) throws ManagerSaveException;

    void removeAllTasks() throws ManagerSaveException;

    void removeAllEpics() throws ManagerSaveException;

    void removeAllSubTasks() throws ManagerSaveException;

    Optional<Task> getTaskById(int id);

    Optional<Epic> getEpicById(int id);

    Optional<Subtask> getSubtaskById(int id);

    Task changeTask(TaskDto taskDto) throws TaskNotFoundException, TaskIntersectionException, ManagerSaveException;

    Subtask changeSubTask(SubtaskDto subtaskDto) throws SubtaskNotFoundException, TaskIntersectionException, ManagerSaveException, EpicNotExistException;

    Epic changeEpic(EpicDto epicDto) throws EpicNotExistException, ManagerSaveException;

    void removeTaskById(int id) throws TaskNotFoundException, ManagerSaveException;

    void removeEpicById(int id) throws EpicNotExistException, ManagerSaveException;

    void removeSubtaskById(int id) throws SubtaskNotFoundException, ManagerSaveException;

    Set<Task> getPrioritizedTasks();

    HistoryManager getHistoryManager();
}
