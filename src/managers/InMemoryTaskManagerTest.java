package managers;

import dto.EpicDto;
import dto.SubtaskDto;
import dto.TaskDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.*;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
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
        taskManager = Managers.getDefault(historyManager);
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

    @Test
    public void taskShouldBeEqualsWithEqualsId() {
        Task task1 = taskManager.getTaskById(0);
        Task task2 = taskManager.getTaskById(0);
        assertEquals(task1, task2);
    }

    @Test
    public void epicShouldBeEqualsWithEqualsId() {
        Epic task1 = taskManager.getEpicById(1);
        Epic task2 = taskManager.getEpicById(1);
        assertEquals(task1, task2);
    }

    @Test
    public void subtaskShouldBeEqualsWithEqualsId() {
        Subtask task1 = taskManager.getSubtaskById(3);
        Subtask task2 = taskManager.getSubtaskById(3);
        assertEquals(task1, task2);
    }

    @Test
    public void testHistorySizeAndUpdate() {
        taskManager.getTaskById(0);
        taskManager.getEpicById(1);
        taskManager.getSubtaskById(4);
        taskManager.getTaskById(0);
        taskManager.getTaskById(0);
        taskManager.getEpicById(1);

        taskManager.getEpicById(1);
        taskManager.getEpicById(1);

        taskManager.getSubtaskById(4);
        taskManager.getSubtaskById(4);

        assertEquals(10, historyManager.getHistory().size());
        assertEquals(0, historyManager.getHistory().getFirst().getId());
        taskManager.getSubtaskById(4);
        assertEquals(10, historyManager.getHistory().size());
        assertEquals(1, historyManager.getHistory().getFirst().getId());
        taskManager.getSubtaskById(4);
        assertEquals(10, historyManager.getHistory().size());
        assertEquals(4, historyManager.getHistory().getFirst().getId());

    }

    @Test
    public void checkEpicCondition() {
        subtaskDto1.setCondition(Status.IN_PROGRESS);
        subtaskDto2.setCondition(Status.DONE);
        subtaskDto3.setCondition(Status.DONE);
        taskManager.changeSubTask(subtaskDto3);
        taskManager.changeSubTask(subtaskDto1);
        taskManager.changeSubTask(subtaskDto2);
        assertEquals(Status.IN_PROGRESS, taskManager.getEpicById(epicDto1.getId()).getStatus());
        assertEquals(Status.DONE, taskManager.getEpicById(epicDto2.getId()).getStatus());
        taskManager.removeSubtaskById(subtaskDto3.getId());
        assertEquals(Status.NEW, taskManager.getEpicById(epicDto2.getId()).getStatus());
    }


}