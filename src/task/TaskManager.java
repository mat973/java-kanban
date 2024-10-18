package task;

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

    public static HashMap<Integer, Subtask> getSupTasks() {
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
        Subtask subtask = new Subtask(name,description, Condition.NEW,  epic);
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


}
