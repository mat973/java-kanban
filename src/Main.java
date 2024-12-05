import dto.EpicDto;
import dto.SubtaskDto;
import dto.TaskDto;
import managers.FileBackedTaskManager;
import managers.HistoryManager;
import managers.Managers;
import task.Status;


public class Main {

    public static void main(String[] args) {
 //     FileBackedTaskManager.deleteFile();
      HistoryManager historyManager = Managers.getDefaultHistory();
      FileBackedTaskManager taskManager = Managers.getFileTaskManager(historyManager);
//        TaskManager taskManager = Managers.getDefault(historyManager);
//
//        System.out.println("Поехали!");
        TaskDto taskDto = new TaskDto(0, "task", "Desc", Status.NEW);
        EpicDto epicDto1 = new EpicDto(1, "Epic1", "Desck1", Status.NEW);
        EpicDto epicDto2 = new EpicDto(2, "Epic2", "Desck3", Status.NEW);
        EpicDto epicDto3 = new EpicDto(150, "Epic2", "Desck3", Status.NEW);
        SubtaskDto subtaskDto1 = new SubtaskDto(3, "Sub1", "Description1", Status.NEW, epicDto1);
        SubtaskDto subtaskDto2 = new SubtaskDto(4, "Sub2", "Description2", Status.NEW, epicDto1);
        SubtaskDto subtaskDto3 = new SubtaskDto(5, "Sub3", "Description3", Status.NEW, epicDto2);
        SubtaskDto subtaskDto4 = new SubtaskDto(5, "Sub3", "Description3", Status.NEW, epicDto3);

        taskManager.createTask(taskDto);
        taskManager.createEpic(epicDto1);
        taskManager.createEpic(epicDto2);
        taskManager.createSubTusk(subtaskDto1);
        taskManager.createSubTusk(subtaskDto2);
        taskManager.createSubTusk(subtaskDto3);
        taskManager.createSubTusk(subtaskDto4);

      System.out.println(taskManager.getTaskById(0));
//
//        System.out.println(historyManager.getHistory());
//        taskManager.getTaskById(0);
//        System.out.println(taskManager.getEpicById(1));
//        taskManager.getEpicById(2);
//        taskManager.getSubtaskById(3);
//        taskManager.getSubtaskById(4);
//        taskManager.getSubtaskById(5);
//        System.out.println(historyManager.getHistory());
//
//        taskManager.removeTaskById(0);
//        System.out.println(historyManager.getHistory());
//        taskManager.removeEpicById(1);
//        System.out.println();
//        System.out.println(historyManager.getHistory());

    }
}

