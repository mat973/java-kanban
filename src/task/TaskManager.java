package task;

import dto.EpicDto;
import dto.SubtaskDto;
import dto.TaskDto;

import java.util.ArrayList;
import java.util.HashMap;

public final class TaskManager {
    private int currentId;

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epicTasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subTasks = new HashMap<>();



    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpicTasks() {
        return new ArrayList<>(epicTasks.values());
    }

    public ArrayList<Subtask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    private int generateId() {
        return currentId++;
    }

    public void createTask(TaskDto taskDto) {
        Task task = new Task(generateId(), taskDto.getName(), taskDto.getDescription(), Status.NEW);
        tasks.put(task.id, task);
    }

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

    public void createEpic(EpicDto epicDto) {
        Epic epic = new Epic(generateId(), epicDto.getName(), epicDto.getDescription(), Status.NEW);
        epicTasks.put(epic.getId(), epic);
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public void removeAllEpics() {
        subTasks.clear();
        epicTasks.clear();
    }

    public void removeAllSubTasks() {
        subTasks.clear();
        for (Integer i : epicTasks.keySet()) {
            //epicTasks.get(i).setCondition(Status.NEW); // эта строчка обновляет статусы эпика
            epicTasks.get(i).getSubtasks().clear();
            checkCondition(epicTasks.get(i));
        }
    }

    public Task getTaskById(int id) {
        if (!tasks.containsKey(id)) {
            System.out.println("Такой задачи не существует");
            return null;
        }

        //return new Task(tasks.get(id)); я это делаю что бы нельзя было влиять на обьекты
        // в мапе и как то их изменять вне этого класса, да я возможно проигрываю по памяти, но я думал,
        // что гарбедж коллектор уберет и все будет хорошо. я просто не понимаю на сколько это критичноЮ для меня
        // это было про инкапсуляцию и в моей голове вроде правильно.

        //  По поводу ДТО я хотел спрятать мои классы сущности и что бы для пользователя были только ДТО,
        // типо тоже инкапсуляция ... не уверен как правильно
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        if (!epicTasks.containsKey(id)) {
            System.out.println("Такого эпика не существует");
            return null;
        }
        return epicTasks.get(id);
    }

    public Subtask getSubtaskById(int id) {
        if (!subTasks.containsKey(id)) {
            System.out.println("Такой подзадачи не существует");
            return null;
        }
        return subTasks.get(id);
    }


    public void changeTask(TaskDto taskDto) {

        if (!tasks.containsKey(taskDto.getId())) {
            System.out.println("Такой задачи не существует");
            return;
        }

        tasks.put(taskDto.getId(), new Task(taskDto.getId(), taskDto.getName(), taskDto.getDescription(),
                taskDto.getCondition()));
        System.out.println("Задача была изменена");
    }

    public void changeSubTask(SubtaskDto subtaskDto) {
        if (!subTasks.containsKey(subtaskDto.getId())) {
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

    private void checkCondition(Epic epic) {

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

    public void removeTaskById(int id) {
        if (!tasks.containsKey(id)) {
            System.out.println("Такой задачи не существует");
            return;
        }
        tasks.remove(id);
    }

    public void removeEpicById(int id) {
        if (!epicTasks.containsKey(id)) {
            System.out.println("Такого эпика не существует");
            return;
        }
        Epic epic = epicTasks.get(id);
        epic.getSubtasks().forEach(x -> subTasks.remove(x.getId()));
        epicTasks.remove(epic.getId());
        epicTasks.remove(id);
    }

    public void removeSubtaskById(int id) {
        if (!subTasks.containsKey(id)) {
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

}
