package managers;

import dto.EpicDto;
import dto.SubtaskDto;
import dto.TaskDto;
import exeptions.EpicNotExistException;
import exeptions.ManagerSaveException;
import task.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class FileBackedTaskManager extends InMemoryTaskManager {
    private static final String FILE_NAME = "tasks.txt";
    private int currentId;

    public FileBackedTaskManager(HistoryManager historyManager) {
        super(historyManager);
        Path path = Paths.get(FILE_NAME);
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
                FileWriter writer = new FileWriter(FILE_NAME);
                writer.write("id,type,name,status,description,epic\n");
                writer.close();
                currentId = 0;
            } catch (IOException e) {
                throw new RuntimeException("Файл не создан");
            }
        } else {
            try (FileReader reader = new FileReader(FILE_NAME);
                 BufferedReader bufferedReader = new BufferedReader(reader)) {
                String line = "a";
                while (bufferedReader.ready()) {
                    line = bufferedReader.readLine();
                }
                String[] parts = line.split(",");
                try {
                    currentId = Integer.parseInt(parts[0]) + 1;
                } catch (NumberFormatException e) {
                    currentId = 0;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private int generateId() {
        return currentId++;
    }

    @Override
    public void createTask(TaskDto taskDto) {
        super.createTask(taskDto);
        String task = generateId() + "," + TaskType.TASK + "," + taskDto.getName() + "," + Status.NEW +
                "," + taskDto.getDescription() + "\n";
        try {
            save(task);
        } catch (ManagerSaveException e) {
            System.out.println("Ошибка добавления Task" + taskDto.getName());
        }
    }

    @Override
    public void createSubTusk(SubtaskDto subtaskDto) {
        try {
        super.createSubTusk(subtaskDto);
        } catch (EpicNotExistException e) {
            System.out.println("Epic с таким id не существует: " + subtaskDto.getEpicId());
            return;
        }
        String subTask = generateId() + "," + TaskType.SUBTASK + "," + subtaskDto.getName() + "," + Status.NEW +
                "," + subtaskDto.getDescription() + "," + subtaskDto.getEpicId() + "\n";
        try {
            save(subTask);
        } catch (ManagerSaveException e) {
            System.out.println("Не удалось сохранить" + subTask);
        }
    }

    @Override
    public void createEpic(EpicDto epicDto) {
        super.createEpic(epicDto);
        String epic = generateId() + "," + TaskType.EPIC + "," + epicDto.getName() + "," + Status.NEW +
                "," + epicDto.getDescription() + "\n";
        try {
            save(epic);
        } catch (ManagerSaveException e) {
            System.out.println("Ошибка добавления Epic" + epicDto.getName());
        }
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
    }

    @Override
    public void removeAllSubTasks() {
        super.removeAllSubTasks();
    }

    @Override
    public void changeTask(TaskDto taskDto) {
        super.changeTask(taskDto);
    }

    @Override
    public void changeSubTask(SubtaskDto subtaskDto) {
        super.changeSubTask(subtaskDto);
    }

    @Override
    public void changeEpic(EpicDto epicDto) {
        super.changeEpic(epicDto);
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
    }

    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
    }

    private void save(String str) throws ManagerSaveException {
        try (FileWriter writer = new FileWriter(FILE_NAME, true);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            bufferedWriter.write(str);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи");
        }
    }
    
    public static void deleteFile() {
        try {
            Files.delete(Paths.get(FILE_NAME));
        } catch (IOException e) {
            System.out.println("Файл не найде");
            throw new RuntimeException("Не повезло");
        }
    }
}
