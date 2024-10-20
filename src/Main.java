
import Dto.EpicDto;
import Dto.SubtaskDto;
import Dto.TaskDto;
import task.*;


public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskDto taskDto = new TaskDto(0,"task", "Desc", Condition.NEW );
        EpicDto epicDto1 = new EpicDto(1, "Epic1", "Desck1", Condition.NEW);
        EpicDto epicDto2 = new EpicDto(2, "Epic2", "Desck3", Condition.NEW);
        SubtaskDto subtaskDto1 = new SubtaskDto(3,"Sub1", "Description1", Condition.NEW,epicDto1);
        SubtaskDto subtaskDto2 = new SubtaskDto(4,"Sub2", "Description2", Condition.NEW,epicDto1);
        SubtaskDto subtaskDto3 = new SubtaskDto(5,"Sub3", "Description3", Condition.NEW,epicDto2);

        TaskManager.createTask(taskDto);
        TaskManager.createEpic(epicDto1);
        TaskManager.createEpic(epicDto2);
        TaskManager.createSubTusk(subtaskDto1);
        TaskManager.createSubTusk(subtaskDto2);
        TaskManager.createSubTusk(subtaskDto3);

        System.out.println(TaskManager.getTasks());
        System.out.println(TaskManager.getEpicTasks());
        System.out.println(TaskManager.getSubTasks());

        subtaskDto1.setCondition(Condition.IN_PROGRESS);
        subtaskDto2.setCondition(Condition.DONE);
        subtaskDto3.setCondition(Condition.DONE);
        TaskManager.changeSubTask(subtaskDto3);
        TaskManager.changeSubTask(subtaskDto1);
        TaskManager.changeSubTask(subtaskDto2);

        System.out.println(TaskManager.getTasks());
        System.out.println(TaskManager.getEpicTasks());
        System.out.println(TaskManager.getSubTasks());

        subtaskDto1.setCondition(Condition.DONE);
        TaskManager.changeSubTask(subtaskDto1);
        TaskManager.removeById(subtaskDto3.getId());

        System.out.println(TaskManager.getTasks());
        System.out.println(TaskManager.getEpicTasks());
        System.out.println(TaskManager.getSubTasks());

        subtaskDto2.setCondition(Condition.IN_PROGRESS);
        TaskManager.changeSubTask(subtaskDto2);
        TaskManager.removeById(epicDto2.getId());
        taskDto.setCondition(Condition.DONE);
        TaskManager.changeTask(taskDto);

        System.out.println(TaskManager.getTasks());
        System.out.println(TaskManager.getEpicTasks());
        System.out.println(TaskManager.getSubTasks());
    }
}
