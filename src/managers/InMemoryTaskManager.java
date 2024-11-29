package managers;

import dto.EpicDto;
import dto.SubtaskDto;
import dto.TaskDto;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class InMemoryTaskManager implements TaskManager {
    private int currentId;

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epicTasks = new HashMap<>();
    private final Map<Integer, Subtask> subTasks = new HashMap<>();

    private final HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
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
    public void createTask(TaskDto taskDto) {
        Task task = new Task(generateId(), taskDto.getName(), taskDto.getDescription(), Status.NEW);
        tasks.put(task.getId(), task);
    }

    @Override
    public void createSubTusk(SubtaskDto subtaskDto) {
        if (!epicTasks.containsKey(subtaskDto.getEpicId())) {
            System.out.println("Подзадача не может сущесвовать самастоятельно");
            return;
        }
        Epic epic = epicTasks.get(subtaskDto.getEpicId());
        Subtask subtask = new Subtask(generateId(), subtaskDto.getName(), subtaskDto.getDescription(), Status.NEW,
                epic.getId());
        subTasks.put(subtask.getId(), subtask);
        epic.getSubtasks().add(subtask);
        checkCondition(epic);
        epicTasks.put(epic.getId(), epic);
    }

    @Override
    public void createEpic(EpicDto epicDto) {
        Epic epic = new Epic(generateId(), epicDto.getName(), epicDto.getDescription(), Status.NEW);
        epicTasks.put(epic.getId(), epic);
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        subTasks.clear();
        epicTasks.clear();
    }

    @Override
    public void removeAllSubTasks() {
        subTasks.clear();
        for (Integer i : epicTasks.keySet()) {
            epicTasks.get(i).getSubtasks().clear();
            checkCondition(epicTasks.get(i));
        }
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
    public void changeTask(TaskDto taskDto) {

        if (!tasks.containsKey(taskDto.getId())) {
            System.out.println("Такой задачи не существует");
            return;
        }

        tasks.put(taskDto.getId(), new Task(taskDto.getId(), taskDto.getName(), taskDto.getDescription(),
                taskDto.getCondition()));
        System.out.println("Задача была изменена");
    }

    @Override
    public void changeSubTask(SubtaskDto subtaskDto) {
        if (!subTasks.containsKey(subtaskDto.getId())) {
            System.out.println("Такой подзадачи не существует");
            return;
        }
        Subtask subtask = new Subtask(subtaskDto.getId(), subtaskDto.getName(),
                subtaskDto.getDescription(), subtaskDto.getCondition(), subtaskDto.getEpicId());
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
        System.out.println("Подзадача была изменена");
    }

    private void checkCondition(Epic epic) {

        if ((epic.getSubtasks().isEmpty()
                || epic.getSubtasks().stream().allMatch(x -> x.getStatus() == Status.NEW))) {
            epic.setCondition(Status.NEW);
        } else if (epic.getSubtasks().stream().allMatch(x -> x.getStatus() == Status.DONE)
                && epic.getStatus() != Status.DONE) {
            epic.setCondition(Status.DONE);
        } else {
            epic.setCondition(Status.IN_PROGRESS);
        }
        epicTasks.put(epic.getId(), epic);
    }

    @Override
    public void changeEpic(EpicDto epicDto) {
        if (!epicTasks.containsKey(epicDto.getId())) {
            System.out.println("Такого Эпика не существует");
            return;
        }
        Epic epic = epicTasks.get(epicDto.getId());
        epic.setName(epicDto.getName());
        epic.setDescription(epicDto.getDescription());
        epicTasks.put(epic.getId(), epic);
        System.out.println("Эпик был обновлен");
    }

    @Override
    public void removeTaskById(int id) {
        if (!tasks.containsKey(id)) {
            System.out.println("Такой задачи не существует");
            return;
        }
        historyManager.remove(id);
        tasks.remove(id);
    }

    @Override
    public void removeEpicById(int id) {
        if (!epicTasks.containsKey(id)) {
            System.out.println("Такого эпика не существует");
            return;
        }
        Epic epic = epicTasks.get(id);
        epic.getSubtasks().forEach(x -> {
            subTasks.remove(x.getId());
            historyManager.remove(x.getId());
        });
        historyManager.remove(id);
        epicTasks.remove(id);
    }

    @Override
    public void removeSubtaskById(int id) {
        if (!subTasks.containsKey(id)) {
            System.out.println("Такой подзадачи не существует");
            return;
        }
        Subtask subtask = subTasks.get(id);
        Epic epic = epicTasks.get(subtask.getEpicId());
        epic.getSubtasks().remove(subTasks.get(subtask.getId()));
        historyManager.remove(id);
        subTasks.remove(subtask.getId());
        checkCondition(epic);
        epicTasks.put(epic.getId(), epic);
    }


}
