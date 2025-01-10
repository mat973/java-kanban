package managers;

import dto.EpicDto;
import dto.SubtaskDto;
import dto.TaskDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileBackedTaskManagerTest {
    private TaskManager taskManager;
    private HistoryManager historyManager;
    private TaskDto taskDto;
    private EpicDto epicDto1;
    private EpicDto epicDto2;
    private SubtaskDto subtaskDto1;
    private SubtaskDto subtaskDto2;
    private SubtaskDto subtaskDto3;

    @BeforeEach
    public void init() {
        historyManager = Managers.getDefaultHistory();
        taskManager = Managers.getFileTaskManager(historyManager);
        taskDto = new TaskDto(0, "task", "Desc", Status.NEW);
        epicDto1 = new EpicDto(1, "Epic1", "Desck1", Status.NEW);
        epicDto2 = new EpicDto(2, "Epic2", "Desck3", Status.NEW);
        subtaskDto1 = new SubtaskDto(3, "Sub1", "Description1", Status.NEW, epicDto1);
        subtaskDto2 = new SubtaskDto(4, "Sub2", "Description2", Status.NEW, epicDto1);
        subtaskDto3 = new SubtaskDto(5, "Sub3", "Description3", Status.NEW, epicDto2);


        taskManager.createTask(taskDto);
        taskManager.createEpic(epicDto1);
        taskManager.createEpic(epicDto2);
        taskManager.createSubTusk(subtaskDto1);
        taskManager.createSubTusk(subtaskDto2);
        taskManager.createSubTusk(subtaskDto3);
    }

    @AfterEach
    public void deleteFile() {
        FileBackedTaskManager.deleteFile();
    }

    @Test
    public void taskShouldBeEqualsWithEqualsId() {
        Task task1 = taskManager.getTaskById(0).get();
        Task task2 = taskManager.getTaskById(0).get();
        assertEquals(task1, task2);
    }

    @Test
    public void epicShouldBeEqualsWithEqualsId() {
        Epic task1 = taskManager.getEpicById(1).get();
        Epic task2 = taskManager.getEpicById(1).get();
        assertEquals(task1, task2);
    }

    @Test
    public void subtaskShouldBeEqualsWithEqualsId() {
        Subtask task1 = taskManager.getSubtaskById(3).get();
        Subtask task2 = taskManager.getSubtaskById(3).get();
        assertEquals(task1, task2);
    }

    @Test
    public void testHistorySizeAndUpdate() {
        assertTrue(historyManager.getHistory().isEmpty());
        taskManager.getTaskById(0);
        assertEquals(1, historyManager.getHistory().size());
        taskManager.getEpicById(1);
        assertEquals(2, historyManager.getHistory().size());
        taskManager.getEpicById(2);
        assertEquals(3, historyManager.getHistory().size());
        taskManager.getSubtaskById(3);
        assertEquals(4, historyManager.getHistory().size());
        taskManager.getSubtaskById(4);
        assertEquals(5, historyManager.getHistory().size());
        taskManager.getSubtaskById(5);
        assertEquals(6, historyManager.getHistory().size());
        assertEquals(taskDto.getId(), historyManager.getHistory().get(0).getId());
        assertEquals(epicDto1.getId(), historyManager.getHistory().get(1).getId());
        assertEquals(epicDto2.getId(), historyManager.getHistory().get(2).getId());
        assertEquals(subtaskDto1.getId(), historyManager.getHistory().get(3).getId());
        assertEquals(subtaskDto2.getId(), historyManager.getHistory().get(4).getId());
        assertEquals(subtaskDto3.getId(), historyManager.getHistory().get(5).getId());
        taskManager.getTaskById(0);
        taskManager.getEpicById(1);
        taskManager.getEpicById(2);
        taskManager.getSubtaskById(3);
        taskManager.getSubtaskById(4);
        taskManager.getSubtaskById(5);
        assertEquals(6, historyManager.getHistory().size());
        taskManager.getSubtaskById(5);
        taskManager.getSubtaskById(4);
        taskManager.getSubtaskById(3);

        taskManager.getEpicById(2);
        taskManager.getEpicById(1);
        taskManager.getTaskById(0);

        assertEquals(taskDto.getId(), historyManager.getHistory().get(5).getId());
        assertEquals(epicDto1.getId(), historyManager.getHistory().get(4).getId());
        assertEquals(epicDto2.getId(), historyManager.getHistory().get(3).getId());
        assertEquals(subtaskDto1.getId(), historyManager.getHistory().get(2).getId());
        assertEquals(subtaskDto2.getId(), historyManager.getHistory().get(1).getId());
        assertEquals(subtaskDto3.getId(), historyManager.getHistory().get(0).getId());

    }

    @Test
    public void checkEpicCondition() {
        subtaskDto1.setCondition(Status.IN_PROGRESS);
        subtaskDto2.setCondition(Status.DONE);
        subtaskDto3.setCondition(Status.DONE);
        taskManager.changeSubTask(subtaskDto3);
        taskManager.changeSubTask(subtaskDto1);
        taskManager.changeSubTask(subtaskDto2);
        assertEquals(Status.IN_PROGRESS, taskManager.getEpicById(epicDto1.getId()).get().getStatus());
        assertEquals(Status.DONE, taskManager.getEpicById(epicDto2.getId()).get().getStatus());
        taskManager.removeSubtaskById(subtaskDto3.getId());
        assertEquals(Status.NEW, taskManager.getEpicById(epicDto2.getId()).get().getStatus());
    }

    @Test
    public void checkAmountOfTasksInNewManager() {
        TaskManager taskManager1 = Managers.getFileTaskManager(historyManager);
        int amount = taskManager1.getTasks().size() + taskManager1.getSubTasks().size() +
                taskManager1.getEpicTasks().size();
        assertEquals(6, amount);
        taskDto = new TaskDto(0, "task", "Desc", Status.NEW);
        epicDto1 = new EpicDto(1, "Epic1", "Desck1", Status.NEW);
        epicDto2 = new EpicDto(2, "Epic2", "Desck3", Status.NEW);
        subtaskDto1 = new SubtaskDto(3, "Sub1", "Description1", Status.NEW, epicDto1);
        subtaskDto2 = new SubtaskDto(4, "Sub2", "Description2", Status.NEW, epicDto1);
        subtaskDto3 = new SubtaskDto(5, "Sub3", "Description3", Status.NEW, epicDto2);
        taskManager1.createTask(taskDto);
        taskManager1.createEpic(epicDto1);
        taskManager1.createEpic(epicDto2);
        taskManager1.createSubTusk(subtaskDto1);
        taskManager1.createSubTusk(subtaskDto2);
        taskManager1.createSubTusk(subtaskDto3);
        TaskManager taskManager2 = Managers.getFileTaskManager(historyManager);
        int amount1 = taskManager1.getTasks().size() + taskManager1.getSubTasks().size() +
                taskManager1.getEpicTasks().size();
        assertEquals(12, amount1);
    }

}