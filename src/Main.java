
import Dto.EpicDto;
import Dto.SubtaskDto;
import Dto.TaskDto;
import task.*;


public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        TaskManager.createTusk("Task", "to be alone");
        TaskManager.createEpic("Epic1", "Todosomething" );
        TaskManager.createEpic("Epic2", "Todosomething" );
        TaskManager.createSubTusk("Sub1", "Todosomething",
                TaskManager.getEpicTasks().get(1));
        TaskManager.createSubTusk("Sub2", "Todosomething",
                TaskManager.getEpicTasks().get(1));
        TaskManager.createSubTusk("Sub3", "Todosomething",
                TaskManager.getEpicTasks().get(1));
        TaskManager.createSubTusk("Sub4", "Todosomething",
                TaskManager.getEpicTasks().get(1));
        TaskManager.createSubTusk("Sub1", "Todosomething",
                TaskManager.getEpicTasks().get(2));


        System.out.println(TaskManager.getTasks());
        System.out.println(TaskManager.getEpicTasks());
        System.out.println(TaskManager.getSubTasks());
        TaskDto taskDto = new TaskDto(0,"Alone", " to be alone", Condition.DONE);
        TaskManager.changeTask(taskDto);
        System.out.println(TaskManager.getTasks());

        EpicDto epicDto = new EpicDto(1);
        SubtaskDto subtaskDto = new SubtaskDto(3, "Sub11", "Todosomething"
                , Condition.IN_PROGRESS, epicDto);
        TaskManager.changeSubTask(subtaskDto);
        System.out.println("-".repeat(30));
        System.out.println(TaskManager.getEpicTasks());


        System.out.println("-".repeat(30));
        SubtaskDto subtaskDto1 = new SubtaskDto(4, "Sub11", "Todosomething"
                , Condition.DONE, epicDto);
        SubtaskDto subtaskDto2 = new SubtaskDto(3, "Sub11", "Todosomething"
                , Condition.DONE, epicDto);
        SubtaskDto subtaskDto3 = new SubtaskDto(5, "Sub11", "Todosomething"
                , Condition.DONE, epicDto);
        SubtaskDto subtaskDto4 = new SubtaskDto(6, "Sub11", "Todosomething"
                , Condition.DONE, epicDto);
        TaskManager.changeSubTask(subtaskDto1);
        TaskManager.changeSubTask(subtaskDto2);
        TaskManager.changeSubTask(subtaskDto3);
        TaskManager.changeSubTask(subtaskDto4);
        subtaskDto4.setCondition(Condition.IN_PROGRESS);
        TaskManager.changeSubTask(subtaskDto4);
        System.out.println(TaskManager.getEpicTasks());

        System.out.println("-".repeat(30));

        EpicDto epicDto1  = new EpicDto(1, "Новове имя", "Новая дискрипция");
        TaskManager.changeEpic(epicDto1);
        System.out.println(TaskManager.getEpicTasks());
    }
}
