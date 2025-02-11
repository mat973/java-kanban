package managers;

import dto.EpicDto;
import dto.SubtaskDto;
import dto.TaskDto;
import exeptions.AlotOfPlanExeption;
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
    public boolean createTask(TaskDto taskDto) {
        Task task;
        if (taskDto.getStartTime() != null && taskDto.getDuration() != null) {
            task = new Task(generateId(), taskDto.getName(), taskDto.getDescription(), Status.NEW,
                    taskDto.getDuration(), taskDto.getStartTime());
            try {
                checkIntersectionTask(task);
            } catch (TaskIntersectionExeption e) {
                System.out.println("Здача " + task.getName() + " не может быть созданна т.к." + e.getMessage());
                return false;
            } catch (AlotOfPlanExeption e) {
                System.out.println("Если хочешь насмешить бога, расскажи ему о своих планах." + e.getMessage());
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
        if (subtaskDto.getStartTime() != null && subtaskDto.getDuration() != null) {
            subtask = new Subtask(generateId(), subtaskDto.getName(), subtaskDto.getDescription(), Status.NEW,
                    epic.getId(), subtaskDto.getDuration(), subtaskDto.getStartTime());
            try {
                checkIntersectionTask(subtask);
            } catch (TaskIntersectionExeption e) {
                System.out.println("Подзадача " + subtask.getName() + " не может быть созданна т.к. " + e.getMessage());
                return false;
            } catch (AlotOfPlanExeption e) {
                System.out.println("Если хочешь насмешить бога, расскажи ему о своих планах." + e.getMessage());
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
        getTasks().forEach(this::freeingMemory);
        tasks.clear();
        return true;
    }

    @Override
    public boolean removeAllEpics() {
        getSubTasks().forEach(this::freeingMemory);
        subTasks.clear();
        epicTasks.clear();
        return true;
    }

    @Override
    public boolean removeAllSubTasks() {
        getSubTasks().forEach(this::freeingMemory);
        subTasks.clear();
        for (Integer i : epicTasks.keySet()) {
            epicTasks.get(i).getSubtasks().clear();
            checkCondition(epicTasks.get(i));
        }
        return true;
    }

    @Override
    public Optional<Task> getTaskById(int id) {
        if (!tasks.containsKey(id)) {
            System.out.println("Такой задачи не существует");
            return Optional.empty();
        }
        Task task = tasks.get(id);
        historyManager.addToHistory(task);
        return Optional.of(task);
    }

    @Override
    public Optional<Epic> getEpicById(int id) {
        if (!epicTasks.containsKey(id)) {
            System.out.println("Такого эпика не существует");
            return Optional.empty();
        }
        Epic epic = epicTasks.get(id);
        historyManager.addToHistory(epic);
        return Optional.of(epic);
    }

    @Override
    public Optional<Subtask> getSubtaskById(int id) {
        if (!subTasks.containsKey(id)) {
            System.out.println("Такой подзадачи не существует");
            return Optional.empty();
        }
        Subtask subtask = subTasks.get(id);
        historyManager.addToHistory(subtask);
        return Optional.of(subtask);
    }


    @Override
    public boolean changeTask(TaskDto taskDto) throws TaskIntersectionExeption {

        if (!tasks.containsKey(taskDto.getId())) {
            System.out.println("Такой задачи не существует");
            return false;
        }
        Task oldTask = tasks.get(taskDto.getId());
        if (sortedTasks.contains(oldTask)) {
            sortedTasks.remove(oldTask);
            freeingMemory(oldTask);
        }
        Task task;
        if (taskDto.getDuration() != null && taskDto.getStartTime() != null) {
            task = new Task(taskDto.getId(), taskDto.getName(), taskDto.getDescription(), taskDto.getStatus(),
                    taskDto.getDuration(), taskDto.getStartTime());
            try {
                checkIntersectionTask(task);
            } catch (TaskIntersectionExeption e) {
                System.out.println("Задача " + task.getName() + "не омжет быть онавлена т.к." + e.getMessage());
                return false;
            } catch (AlotOfPlanExeption e) {
                System.out.println("Если хочешь насмешить бога, расскажи ему о своих планах." + e.getMessage());
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
        if (sortedTasks.contains(oldSubtask)) {
            sortedTasks.remove(oldSubtask);
            freeingMemory(oldSubtask);
        }
        Subtask subtask;
        if (subtaskDto.getStartTime() != null && subtaskDto.getDuration() != null) {
            subtask = new Subtask(subtaskDto.getId(), subtaskDto.getName(),
                    subtaskDto.getDescription(), subtaskDto.getStatus(), subtaskDto.getEpicId(),
                    subtaskDto.getDuration(), subtaskDto.getStartTime());
            try {
                checkIntersectionTask(subtask);
            } catch (TaskIntersectionExeption e) {
                System.out.println("Подзадача " + subtask.getName() + " неможет быть обновлена т.к. " + e.getMessage());
                return false;
            } catch (AlotOfPlanExeption e) {
                System.out.println("Если хочешь насмешить бога, расскажи ему о своих планах." + e.getMessage());
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
        Task task = getTaskById(id).get();
        if (sortedTasks.contains(task)) {
            sortedTasks.remove(task);
            freeingMemory(task);
        }
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
        if (sortedTasks.contains(subtask)) {
            sortedTasks.remove(subtask);
            freeingMemory(subtask);
        }
        subTasks.remove(subtask.getId());
        checkCondition(epic);
        epicTasks.put(epic.getId(), epic);
        return true;
    }


    private void checkCondition(Epic epic) {
        if (epic.getStartTime() != null && epic.getDuration() != null) {
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
                .filter(obj -> obj.getStartTime() != null && obj.getDuration() != null)
                .toList();
        if (validSubTask.isEmpty()) {
            return;
        }
        LocalDateTime startTIme = validSubTask.stream()
                .map(sub -> sub.getStartTime())
                .min(LocalDateTime::compareTo).get();
        epic.setStartTime(startTIme);
        LocalDateTime endTime = validSubTask.stream()
                .max(Task::compareTo)
                .get().getEndTime();
        epic.setDuration(Duration.between(startTIme, endTime));
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

    private void checkIntersectionTask(Task task) throws TaskIntersectionExeption, AlotOfPlanExeption {
        LocalDateTime taskStart = task.getStartTime();
        LocalDateTime taskEnd = task.getEndTime();
        if (taskStart.getYear() < 2025 || taskStart.getYear() > 2035 || taskEnd.getYear() > 2035) {
            throw new AlotOfPlanExeption("Мы не строим планы так на долго!");
        }
        LocalDateTime newStartTime = taskStart.minusMinutes(taskStart.getMinute() % 15);
        LocalDateTime newEndTime = taskEnd.minusMinutes(taskEnd.getMinute() % 15).plusMinutes(15);
        LocalDateTime checkTime = newStartTime;
        while (!checkTime.equals(newEndTime)) {
            if (!timeMap.get(newStartTime)) {
                throw new TaskIntersectionExeption("на это время запланированно выполнение другой задачи.");
            }
            checkTime = checkTime.plusMinutes(15);
        }
        while (!newStartTime.equals(newEndTime)) {
            timeMap.put(newStartTime, false);
            newStartTime = newStartTime.plusMinutes(15);
        }
    }

    private void freeingMemory(Task task) {
        LocalDateTime taskStart = task.getStartTime();
        LocalDateTime taskEnd = task.getEndTime();
        taskStart = taskStart.minusMinutes(taskStart.getMinute() % 15);
        taskEnd = taskEnd.minusMinutes(taskEnd.getMinute() % 15).plusMinutes(15);
        while (!taskStart.equals(taskEnd)) {
            timeMap.put(taskStart, true);
            taskStart = taskStart.plusMinutes(15);
        }
    }
}
