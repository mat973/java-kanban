package task;

import Dto.SubtaskDto;
import Dto.TaskDto;

import java.util.ArrayList;
import java.util.HashMap;

public final class TaskManager {
    public static int counter = 0;

    private static final HashMap<Integer, Task> tasks = new HashMap<>();
    private static final HashMap<Integer, Epic> epicTasks = new HashMap<>();
    private static final HashMap<Integer, Subtask> subTasks = new HashMap<>();

    public static HashMap<Integer, Task> getTasks() {
        return (HashMap<Integer, Task>) tasks.clone();
    }

    public static HashMap<Integer, Epic> getEpicTasks() {
        return (HashMap<Integer, Epic>) epicTasks.clone();
    }

    public static HashMap<Integer, Subtask> getSubTasks() {
        return (HashMap<Integer, Subtask>) subTasks.clone();
    }

    static int getId(){
       return counter++;
    }

    public static void createTusk(String name, String description){
        Task task = new Task(name,description, Condition.NEW);
        tasks.put(task.id, task);
    }
    public static void createSubTusk(String name, String description, Epic epic){
        if (!epicTasks.containsKey(epic.getId())) {
            System.out.println("Подзадача не может сущесвовать самастоятельно");
        }
            Subtask subtask = new Subtask(name, description, Condition.NEW, epic);
            subTasks.put(subtask.getId(), subtask);
            epic.getSubtasks().add(subtask);


    }
    public static void createEpic(String name, String description){
        Epic epic = new Epic( name,description, Condition.NEW);
        epicTasks.put(epic.getId(), epic);
    }

    public static void removeAllTasks(){
        tasks.clear();
    }

    public static void removeAllEpics(){
        //TODO  про стастусы что то
        subTasks.clear();
        epicTasks.clear();
    }

    public static void removeAllSubTasks(){
        subTasks.clear();
        for (Integer i : epicTasks.keySet()) {
            epicTasks.get(i).setCondition(Condition.NEW);
        }
        //TODO  про стастусы что то
    }

    public static Task getTaskById(Integer id){
        if (tasks.containsKey(id)) {
            return new Task(tasks.get(id));
        }else if (epicTasks.containsKey(id)) {
            return new Epic(epicTasks.get(id));
        }else if (subTasks.containsKey(id)) {
            return new Subtask(subTasks.get(id));
        }else {
            System.out.println("Задачи с таким id не существует");
            return null;
        }
    }

    public static void changeTask(TaskDto taskDto){

        if (getTaskById(taskDto.getId()) == null || !tasks.containsKey(taskDto.getId())){
            System.out.println("Такой задачи не существует");
            return;
        }

        tasks.put(taskDto.getId(), new Task(taskDto.getId(), taskDto.getName(), taskDto.getDescription(),
                taskDto.getCondition()));
        System.out.println("Задача была изменена");
    }

    public static void changeSubTask(SubtaskDto subtaskDto){
        if (getTaskById(subtaskDto.getId()) == null || !subTasks.containsKey(subtaskDto.getId())){
            System.out.println("Такой подзадачи не существует");
            return;
        }
//        if (subtaskDto.getCondition() != subTasks.get(subtaskDto.getId()).getCondition()) {
//            if (subtaskDto.getCondition() != Condition.NEW) {
//                Epic epic = epicTasks.get(subtaskDto.getEpicId());
//                if (subtaskDto.getCondition() == Condition.IN_PROGRESS && epic.getCondition() != Condition.IN_PROGRESS) {
//                    epic.setCondition(Condition.IN_PROGRESS);
//                    epicTasks.put(epic.getId(), epic);
//                    System.out.println(epicTasks);
//                } else if (subtaskDto.getCondition() == Condition.DONE) {
//                    if (epic.getSubtasks().stream().allMatch(x -> x.getCondition() == Condition.DONE)) {
//                        epic.setCondition(Condition.DONE);
//                        epicTasks.put(epic.getId(), epic);
//                    } else if (epic.condition != Condition.IN_PROGRESS) {
//                        epic.setCondition(Condition.IN_PROGRESS);
//                        epicTasks.put(epic.getId(), epic);
//                    }
//                }
//            } else {
//                Epic epic = epicTasks.get(subtaskDto.getEpicId());
//                if (epic.getCondition() == Condition.DONE) {
//                    epic.setCondition(Condition.IN_PROGRESS);
//                    epicTasks.put(epic.getId(), epic);
//                } else if (epic.getSubtasks().stream().allMatch(x -> x.getCondition() == Condition.NEW)) {
//                    epic.setCondition(Condition.NEW);
//                    epicTasks.put(epic.getId(), epic);
//                }
//
//            }
//        }
        Subtask subtask = new Subtask(subtaskDto.getId(), subtaskDto.getName()
                , subtaskDto.getDescription(), subtaskDto.getCondition(), subtaskDto.getEpicId());
        subTasks.put(subtaskDto.getId(), subtask);
        Epic epic = epicTasks.get(subtask.getEpicId());
        ArrayList<Subtask> list = epic.getSubtasks();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId()== subtask.getId()){
                list.set(i, subtask);
            }
        }
        epicTasks.put(epic.getId(), epic);
        System.out.println("Подзадача была изменена");
    }


}
