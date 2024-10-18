
import task.TaskManager;


public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
//        System.out.println(TaskManager.getId());
//        System.out.println(TaskManager.getId());
       // Epic epic = new Epic("что то","что то делаем", Condition.NEW);
       // Subtask subtask = new Subtask("чуть","немного", Condition.NEW,  epic);
        TaskManager.createTusk("Alone", "to be alone");
        TaskManager.createTusk("TODO", "Todosomething");
        TaskManager.createEpic("TODO", "Todosomething" );
        TaskManager.createSubTusk("TODO", "Todosomething",
                TaskManager.getEpicTasks().get(2));
        TaskManager.createSubTusk("TODO1", "Todosomething",
                TaskManager.getEpicTasks().get(2));
        TaskManager.createSubTusk("TODO2", "Todosomething",
                TaskManager.getEpicTasks().get(2));
        TaskManager.createSubTusk("TODO3", "Todosomething",
                TaskManager.getEpicTasks().get(2));

        //System.out.println(TaskManager.getEpicTasks());
     //   TaskManager.getEpicTasks().put(1, TaskManager.getEpicTasks().get(0));
        System.out.println(TaskManager.getSupTasks());
        System.out.println(TaskManager.getEpicTasks());
        System.out.println(TaskManager.getTasks());

        TaskManager.removeAllTasks();
  //      TaskManager.removeAllSubTasks();
        TaskManager.removeAllEpics();
        System.out.println(TaskManager.getSupTasks());
        System.out.println(TaskManager.getEpicTasks());
        System.out.println(TaskManager.getTasks());
    }
}
