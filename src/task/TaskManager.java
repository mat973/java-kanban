package task;

import dto.EpicDto;
import dto.SubtaskDto;
import dto.TaskDto;

import java.util.ArrayList;
import java.util.HashMap;

public final class TaskManager {
    private  int currentId;

    private  final HashMap<Integer, Task> tasks;
    private  final HashMap<Integer, Epic> epicTasks;
    private  final HashMap<Integer, Subtask> subTasks;

    public TaskManager() {
        this.currentId = 0;
        epicTasks = new HashMap<>();
        tasks = new HashMap<>();
        subTasks = new HashMap<>();
    }
    public  ArrayList<Task> getTasks() {
        ArrayList<Task> list = new ArrayList<>();
        tasks.keySet().forEach(x -> list.add(tasks.get(x)));
        return list;
    }

    public ArrayList<Epic> getEpicTasks() {
        ArrayList<Epic> list = new ArrayList<>();
        epicTasks.keySet().forEach(x -> list.add(epicTasks.get(x)));
        return list;
    }

    public  ArrayList<Subtask> getSubTasks() {
        ArrayList<Subtask> list = new ArrayList<>();
        subTasks.keySet().forEach(x -> list.add(subTasks.get(x)));
        return list;
    }

    public int generateId() {
        return currentId++;
    }

    public  void createTask(TaskDto taskDto) {
        Task task = new Task(generateId(), taskDto.getName(), taskDto.getDescription(), Status.NEW);
        tasks.put(task.id, task);
    }

    public  void createSubTusk(SubtaskDto subtaskDto) {
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

    public  void createEpic(EpicDto epicDto) {
        Epic epic = new Epic(generateId(), epicDto.getName(), epicDto.getDescription(), Status.NEW);
        epicTasks.put(epic.getId(), epic);
    }

    public  void removeAllTasks() {
        tasks.clear();
    }

    public  void removeAllEpics() {
        subTasks.clear();
        epicTasks.clear();
    }

    public  void removeAllSubTasks() {
        subTasks.clear();
        for (Integer i : epicTasks.keySet()) {
            epicTasks.get(i).setCondition(Status.NEW);
            epicTasks.get(i).getSubtasks().clear();
        }
    }

    public  Task getTaskByID(int id){
        if (!existedTask(id)  || !tasks.containsKey(id)) {
            System.out.println("Такой задачи не существует");
            return null;
        }
        return new Task(tasks.get(id));
    }
    public  Epic getEpicByID(int id){
        if (!existedTask(id)  || !epicTasks.containsKey(id)) {
            System.out.println("Такого эпика не существует");
            return null;
        }
        return new Epic(epicTasks.get(id));
    }
    public Subtask getSubtaskByID(int id){
        if (!existedTask(id)  || !subTasks.containsKey(id)) {
            System.out.println("Такой подзадачи не существует");
            return null;
        }
        return new Subtask(subTasks.get(id));
    }
    public  boolean existedTask(int id) {
        if (tasks.containsKey(id)) {
            return true;
        } else if (epicTasks.containsKey(id)) {
            return true;
        } else if (subTasks.containsKey(id)) {
            return true;
        } else {
            System.out.println("Задачи с таким id не существует");
            return false;
        }
    }

    public  void changeTask(TaskDto taskDto) {

        if (!existedTask(taskDto.getId())  || !tasks.containsKey(taskDto.getId())) {
            System.out.println("Такой задачи не существует");
            return;
        }

        tasks.put(taskDto.getId(), new Task(taskDto.getId(), taskDto.getName(), taskDto.getDescription(),
                taskDto.getCondition()));
        System.out.println("Задача была изменена");
    }

    public  void changeSubTask(SubtaskDto subtaskDto) {
        if (!existedTask(subtaskDto.getId()) || !subTasks.containsKey(subtaskDto.getId())) {
            System.out.println("Такой подзадачи не существует");
            return;
        }
        Subtask subtask = new Subtask(subtaskDto.getId(), subtaskDto.getName()
                , subtaskDto.getDescription(), subtaskDto.getCondition(), subtaskDto.getEpicId());
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

    private  void checkCondition(Epic epic) {

        if ((epic.getSubtasks().isEmpty()
                || epic.getSubtasks().stream().allMatch(x -> x.getCondition() == Status.NEW))) {
            epic.setCondition(Status.NEW);
        } else if (epic.getSubtasks().stream().allMatch(x -> x.getCondition() == Status.DONE)
                && epic.getCondition() != Status.DONE) {
            epic.setCondition(Status.DONE);
        } else {
            epic.setCondition(Status.IN_PROGRESS);
        }
        epicTasks.put(epic.getId(), epic);
    }

    public  void changeEpic(EpicDto epicDto) {
        if (!existedTask(epicDto.getId()) || !epicTasks.containsKey(epicDto.getId())) {
            System.out.println("Такого Эпика не существует");
            return;
        }
        Epic epic = epicTasks.get(epicDto.getId());
        epic.setName(epicDto.getName());
        epic.setDescription(epicDto.getDescription());
        epicTasks.put(epic.getId(), epic);
        System.out.println("Эпик был обновлен");
    }

    public  void removeTaskById(int id){
        if (!tasks.containsKey(id)){
            System.out.println("Такой задачи не существует");
            return;
        }
        tasks.remove(id);
    }
    public  void removeEpicById(int id){
        if (!epicTasks.containsKey(id)){
            System.out.println("Такого эпика не существует");
            return;
        }
        Epic epic = epicTasks.get(id);
        epic.getSubtasks().forEach(x -> subTasks.remove(x.getId()));
        epicTasks.remove(epic.getId());
        epicTasks.remove(id);
    }

    public  void removeSubtaskById(int id){
        if (!subTasks.containsKey(id)){
            System.out.println("Такой подзадачи не существует");
            return;
        }
        Subtask subtask = subTasks.get(id);
        Epic epic = epicTasks.get(subtask.getEpicId());
        epic.getSubtasks().remove(subTasks.get(subtask.getId()));
        subTasks.remove(subtask.getId());
        checkCondition(epic);
        epicTasks.put(epic.getId(), epic);
    }

    public  void removeById(int id){
        Task task = null;
        if (task == null){
            System.out.println("Такой задачи не сущесвует.");
            return;
        }
        if (task instanceof Subtask){
            Subtask subtask = (Subtask) task;
            Epic epic = epicTasks.get(subtask.getEpicId());
            epic.getSubtasks().remove(subTasks.get(subtask.getId()));
            subTasks.remove(subtask.getId());
            checkCondition(epic);
            epicTasks.put(epic.getId(), epic);
        } else if (task instanceof Epic) {
            Epic epic = (Epic) task;
            epic.getSubtasks().forEach(x -> subTasks.remove(x.getId()));
            epicTasks.remove(epic.getId());
        }else {
            tasks.remove(task.getId());
        }

    }


}
