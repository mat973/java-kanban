
import dto.EpicDto;
import dto.SubtaskDto;
import dto.TaskDto;
import task.*;


public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        System.out.println("Поехали!");
        TaskDto taskDto = new TaskDto(0, "task", "Desc", Status.NEW);
        EpicDto epicDto1 = new EpicDto(1, "Epic1", "Desck1", Status.NEW);
        EpicDto epicDto2 = new EpicDto(2, "Epic2", "Desck3", Status.NEW);
        SubtaskDto subtaskDto1 = new SubtaskDto(3, "Sub1", "Description1", Status.NEW, epicDto1);
        SubtaskDto subtaskDto2 = new SubtaskDto(4, "Sub2", "Description2", Status.NEW, epicDto1);
        SubtaskDto subtaskDto3 = new SubtaskDto(5, "Sub3", "Description3", Status.NEW, epicDto2);

        taskManager.createTask(taskDto);
        taskManager.createEpic(epicDto1);
        taskManager.createEpic(epicDto2);
        taskManager.createSubTusk(subtaskDto1);
        taskManager.createSubTusk(subtaskDto2);
        taskManager.createSubTusk(subtaskDto3);

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpicTasks());
        System.out.println(taskManager.getSubTasks());

        subtaskDto1.setCondition(Status.IN_PROGRESS);
        subtaskDto2.setCondition(Status.DONE);
        subtaskDto3.setCondition(Status.DONE);
        taskManager.changeSubTask(subtaskDto3);
        taskManager.changeSubTask(subtaskDto1);
        taskManager.changeSubTask(subtaskDto2);

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpicTasks());
        System.out.println(taskManager.getSubTasks());

        subtaskDto1.setCondition(Status.DONE);
        taskManager.changeSubTask(subtaskDto1);
        taskManager.removeSubtaskById(subtaskDto3.getId());

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpicTasks());
        System.out.println(taskManager.getSubTasks());

        subtaskDto2.setCondition(Status.IN_PROGRESS);
        taskManager.changeSubTask(subtaskDto2);
        taskManager.removeEpicById((epicDto2.getId()));
        taskDto.setCondition(Status.DONE);
        taskManager.changeTask(taskDto);

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpicTasks());
        System.out.println(taskManager.getSubTasks());

        taskManager.removeAllSubTasks();
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpicTasks());
        System.out.println(taskManager.getSubTasks());
    }
}
