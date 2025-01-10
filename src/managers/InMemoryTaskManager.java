package managers;

import dto.EpicDto;
import dto.SubtaskDto;
import dto.TaskDto;
import exeptions.EpicNotExistException;
import exeptions.TaskIntersectionExeption;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int currentId;

    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epicTasks = new HashMap<>();
    protected final Map<Integer, Subtask> subTasks = new HashMap<>();
    protected final Set<Task> sortedTasks = new TreeSet<>();
    protected final Map<LocalDateTime, Boolean> timeMap = new HashMap<>();
    LocalDateTime start = LocalDateTime.of(2025, 1, 1, 0, 0);
    LocalDateTime end = start.plusYears(10);

    private final HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
        while (start.isBefore(end)) {
            timeMap.put(start, true);
            start = start.plusMinutes(15);
        }
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpicTasks() {
        return new ArrayList<>(epicTasks.values());
    }

    @Override
    public ArrayList<Subtask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    private int generateId() {
        return currentId++;
    }

    @Override
    public boolean createTask(TaskDto taskDto) throws TaskIntersectionExeption {
        Task task;
        if (taskDto.getStartTime().isPresent() && taskDto.getDuration().isPresent()) {
            task = new Task(generateId(), taskDto.getName(), taskDto.getDescription(), Status.NEW,
                    taskDto.getDuration().get(), taskDto.getStartTime().get());
            try {
                checkIntersectionTask(task);
            } catch (TaskIntersectionExeption e) {
                System.out.println(e.getMessage());
                return false;
            }
            sortedTasks.add(task);
        } else {
            task = new Task(generateId(), taskDto.getName(), taskDto.getDescription(), Status.NEW);
        }
        tasks.put(task.getId(), task);
        return true;
    }

    @Override
    public boolean createSubTusk(SubtaskDto subtaskDto) throws EpicNotExistException, TaskIntersectionExeption {
        if (!epicTasks.containsKey(subtaskDto.getEpicId())) {
            System.out.println("Подзадача не может сущесвовать самастоятельно");
            return false;
        }
        Epic epic = epicTasks.get(subtaskDto.getEpicId());
        Subtask subtask;
        if (subtaskDto.getStartTime().isPresent() && subtaskDto.getDuration().isPresent()) {
            subtask = new Subtask(generateId(), subtaskDto.getName(), subtaskDto.getDescription(), Status.NEW,
                    epic.getId(), subtaskDto.getDuration().get(), subtaskDto.getStartTime().get());
            try {
                checkIntersectionTask(subtask);
            } catch (TaskIntersectionExeption e) {
                System.out.println(e.getMessage());
                return false;
            }
            sortedTasks.add(subtask);
        } else {
            subtask = new Subtask(generateId(), subtaskDto.getName(), subtaskDto.getDescription(), Status.NEW,
                    epic.getId());
        }
        subTasks.put(subtask.getId(), subtask);
        epic.getSubtasks().add(subtask);
        checkCondition(epic);
        epicTasks.put(epic.getId(), epic);
        return true;
    }

    @Override
    public boolean createEpic(EpicDto epicDto) {
        Epic epic = new Epic(generateId(), epicDto.getName(), epicDto.getDescription(),
                epicDto.getStatus());

        epicTasks.put(epic.getId(), epic);
        return true;
    }

    @Override
    public boolean removeAllTasks() {
        tasks.clear();
        return true;
    }

    @Override
    public boolean removeAllEpics() {
        subTasks.clear();
        epicTasks.clear();
        return true;
    }

    @Override
    public boolean removeAllSubTasks() {
        subTasks.clear();
        for (Integer i : epicTasks.keySet()) {
            epicTasks.get(i).getSubtasks().clear();
            checkCondition(epicTasks.get(i));
        }
        return true;
    }

    @Override
    public Task getTaskById(int id) {
        if (!tasks.containsKey(id)) {
            System.out.println("Такой задачи не существует");
            return null;
        }
        Task task = tasks.get(id);
        historyManager.addToHistory(task);
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        if (!epicTasks.containsKey(id)) {
            System.out.println("Такого эпика не существует");
            return null;
        }
        Epic epic = epicTasks.get(id);
        historyManager.addToHistory(epic);
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        if (!subTasks.containsKey(id)) {
            System.out.println("Такой подзадачи не существует");
            return null;
        }
        Subtask subtask = subTasks.get(id);
        historyManager.addToHistory(subtask);
        return subtask;
    }


    @Override
    public boolean changeTask(TaskDto taskDto) throws TaskIntersectionExeption {

        if (!tasks.containsKey(taskDto.getId())) {
            System.out.println("Такой задачи не существует");
            return false;
        }
        Task oldTask = tasks.get(taskDto.getId());
        sortedTasks.remove(oldTask);
        Task task;
        if (taskDto.getDuration().isPresent() && taskDto.getStartTime().isPresent()) {
            task = new Task(taskDto.getId(), taskDto.getName(), taskDto.getDescription(), taskDto.getStatus(),
                    taskDto.getDuration().get(), taskDto.getStartTime().get());
            try {
                checkIntersectionTask(task);
            } catch (TaskIntersectionExeption e) {
                System.out.println(e.getMessage());
                return false;
            }
            sortedTasks.add(task);
        } else {
            task = new Task(taskDto.getId(), taskDto.getName(), taskDto.getDescription(), taskDto.getStatus());
        }
        tasks.put(taskDto.getId(), task);
        System.out.println("Задача была изменена");
        return true;
    }

    @Override
    public boolean changeSubTask(SubtaskDto subtaskDto) throws TaskIntersectionExeption {
        if (!subTasks.containsKey(subtaskDto.getId())) {
            System.out.println("Такой подзадачи не существует");
            return false;
        }
        Subtask oldSubtask = subTasks.get(subtaskDto.getId());
        sortedTasks.remove(oldSubtask);
        Subtask subtask;
        if (subtaskDto.getStartTime().isPresent() && subtaskDto.getDuration().isPresent()) {
            subtask = new Subtask(subtaskDto.getId(), subtaskDto.getName(),
                    subtaskDto.getDescription(), subtaskDto.getStatus(), subtaskDto.getEpicId(),
                    subtaskDto.getDuration().get(), subtaskDto.getStartTime().get());
            try {
                checkIntersectionTask(subtask);
            } catch (TaskIntersectionExeption e) {
                System.out.println(e.getMessage());
                return false;
            }
            sortedTasks.add(subtask);
        } else {
            subtask = new Subtask(subtaskDto.getId(), subtaskDto.getName(),
                    subtaskDto.getDescription(), subtaskDto.getStatus(), subtaskDto.getEpicId());
        }
        subTasks.put(subtaskDto.getId(), subtask);
        Epic epic = epicTasks.get(subtask.getEpicId());
        ArrayList<Subtask> list = epic.getSubtasks();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == subtask.getId()) {
                list.set(i, subtask);
            }
        }
        epicTasks.put(epic.getId(), epic);
        checkCondition(epic);
        return true;
    }

    @Override
    public boolean changeEpic(EpicDto epicDto) {
        if (!epicTasks.containsKey(epicDto.getId())) {
            System.out.println("Такого Эпика не существует");
            return false;
        }
        Epic epic = epicTasks.get(epicDto.getId());
        sortedTasks.remove(epic);
        epic.setName(epicDto.getName());
        epic.setDescription(epicDto.getDescription());
        sortedTasks.add(epic);
        epicTasks.put(epic.getId(), epic);
        return true;

    }

    @Override
    public boolean removeTaskById(int id) {
        if (!tasks.containsKey(id)) {
            System.out.println("Такой задачи не существует");
            return false;
        }
        sortedTasks.remove(getTaskById(id));
        historyManager.remove(id);
        tasks.remove(id);
        return true;
    }

    @Override
    public boolean removeEpicById(int id) {
        if (!epicTasks.containsKey(id)) {
            System.out.println("Такого эпика не существует");
            return false;
        }
        Epic epic = epicTasks.get(id);
        epic.getSubtasks().forEach(x -> {
            subTasks.remove(x.getId());
            historyManager.remove(x.getId());
            sortedTasks.remove(x);
        });
        sortedTasks.remove(epic);
        historyManager.remove(id);
        epicTasks.remove(id);
        return true;
    }

    @Override
    public boolean removeSubtaskById(int id) {
        if (!subTasks.containsKey(id)) {
            System.out.println("Такой подзадачи не существует");
            return false;
        }
        Subtask subtask = subTasks.get(id);
        Epic epic = epicTasks.get(subtask.getEpicId());
        epic.getSubtasks().remove(subTasks.get(subtask.getId()));
        historyManager.remove(id);
        sortedTasks.remove(subtask);
        subTasks.remove(subtask.getId());
        checkCondition(epic);
        epicTasks.put(epic.getId(), epic);
        return true;
    }


    private void checkCondition(Epic epic) {
        if (epic.getStartTime().isPresent() && epic.getDuration().isPresent()) {
            sortedTasks.remove(epic);
        }
        if ((epic.getSubtasks().isEmpty()
                || epic.getSubtasks().stream().allMatch(x -> x.getStatus() == Status.NEW))) {
            epic.setCondition(Status.NEW);
        } else if (epic.getSubtasks().stream().allMatch(x -> x.getStatus() == Status.DONE)
                && epic.getStatus() != Status.DONE) {
            epic.setCondition(Status.DONE);
        } else {
            epic.setCondition(Status.IN_PROGRESS);
        }
        List<Subtask> validSubTask = epic.getSubtasks().stream()
                .filter(obj -> obj.getStartTime().isPresent() && obj.getDuration().isPresent())
                .toList();
        if (validSubTask.isEmpty()) {
            epic.setStartTime(Optional.empty());
            epic.setDuration(Optional.empty());
            return;
        }
        Optional<LocalDateTime> startTIme = validSubTask.stream()
                .map(sub -> sub.getStartTime().get())
                .min(LocalDateTime::compareTo);
        epic.setStartTime(startTIme);
        LocalDateTime endTime = validSubTask.stream()
                .max(Task::compareTo)
                .get().getEndTime();
        epic.setDuration(Optional.of(Duration.between(startTIme.get(), endTime)));
        sortedTasks.add(epic);
    }


    protected void setCurrentId(int currentId) {
        this.currentId = currentId;
    }

    public int getCurrentId() {
        return currentId;
    }

    public Set<Task> getPrioritizedTasks() {
        return sortedTasks;
    }

    public void checkIntersectionTask(Task task) throws TaskIntersectionExeption {
        LocalDateTime taskStart = task.getStartTime().get();
        LocalDateTime taskEnd = task.getEndTime();

        for (Task existingTask : sortedTasks) {
            LocalDateTime existingStart = existingTask.getStartTime().get();
            LocalDateTime existingEnd = existingTask.getEndTime();

            if (existingStart.isAfter(taskEnd)) {
                break;
            }

            if (taskStart.isBefore(existingEnd) && taskEnd.isAfter(existingStart)) {
                throw new TaskIntersectionExeption("На это время запланированно выполнение задачи "
                        + existingTask.getName());
            }
        }

    }
}
