package managers;

import dto.EpicDto;
import dto.SubtaskDto;
import dto.TaskDto;
import exeptions.BadMemoryException;
import exeptions.EpicNotExistException;
import exeptions.ManagerSaveException;
import exeptions.TaskIntersectionExeption;
import task.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


public class FileBackedTaskManager extends InMemoryTaskManager {
    private static final String FILE_NAME = "tasks.txt";

    public FileBackedTaskManager(HistoryManager historyManager) {
        super(historyManager);
        Path path = Paths.get(FILE_NAME);
        if (!Files.exists(path)) {
            try {
                setCurrentId(0);
                Files.createFile(path);
                FileWriter writer = new FileWriter(FILE_NAME);
                writer.write("MaxId:" + getCurrentId() + ",id,type,name,status,description,epic\n");
                writer.close();

            } catch (IOException e) {
                throw new RuntimeException("Файл не создан");
            }
        } else {
            try (FileReader reader = new FileReader(FILE_NAME);
                 BufferedReader bufferedReader = new BufferedReader(reader)) {
                String firstLine = bufferedReader.readLine();
                String[] splitLine = firstLine.split(",");
                setCurrentId(Integer.parseInt(splitLine[0].substring(6)));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try (FileReader fileReader = new FileReader(FILE_NAME);
                 BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                fillInMaps(bufferedReader);
            } catch (IOException e) {
                throw new ManagerSaveException("Ошибка чтения при заполнения Map");
            }
        }
    }

    private void fillInMaps(BufferedReader reader) throws IOException {
        reader.readLine(); // Skip firs line
        while (reader.ready()) {
            String line = reader.readLine();
            String[] sLine = line.split(",");
            switch (TaskType.getTaskType(sLine[1])) {
                case TASK:
                    Task task = stringToTask(sLine);
                    tasks.put(task.getId(), task);
                    break;
                case EPIC:
                    Epic epic = stringToEpic(sLine);
                    epicTasks.put(epic.getId(), epic);
                    break;
                case SUBTASK:
                    Subtask subtask = stringToSubtask(sLine);
                    subTasks.put(subtask.getId(), subtask);
                    break;
                default:
                    throw new BadMemoryException("Целостность фала с данными нарушена");
            }
        }
    }


    @Override
    public void createTask(TaskDto taskDto) {
        try {
            super.createTask(taskDto);
        } catch (TaskIntersectionExeption e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void createSubTusk(SubtaskDto subtaskDto) {
        try {
            super.createSubTusk(subtaskDto);
        } catch (EpicNotExistException e) {
            System.out.println("Epic с таким id не существует: " + subtaskDto.getEpicId());
            return;
        } catch (TaskIntersectionExeption e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void createEpic(EpicDto epicDto) {
        super.createEpic(epicDto);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeAllSubTasks() {
        super.removeAllSubTasks();
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void changeTask(TaskDto taskDto) {
        try {
            super.changeTask(taskDto);
        } catch (TaskIntersectionExeption e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void changeSubTask(SubtaskDto subtaskDto) {
        try {
            super.changeSubTask(subtaskDto);
        } catch (TaskIntersectionExeption e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void changeEpic(EpicDto epicDto) {
        super.changeEpic(epicDto);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }


    private void save() throws ManagerSaveException {
        try (FileWriter writer = new FileWriter(FILE_NAME);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            bufferedWriter.write("MaxId:" + (getCurrentId() - 1) + ",id,type,name,status,description,epic\n");
            ;
            writeTasks(getTasks(), bufferedWriter);
            writeEpic(getEpicTasks(), bufferedWriter);
            writeSubtask(getSubTasks(), bufferedWriter);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи");
        }
    }

    private void writeTasks(List<Task> taskList, BufferedWriter bufferedWriter) throws IOException {
        for (Task task : taskList) {
            bufferedWriter.write(taskToString(task));
        }
    }

    private void writeEpic(List<Epic> epicList, BufferedWriter bufferedWriter) throws IOException {
        for (Epic epic : epicList) {
            bufferedWriter.write(epicToString(epic));
        }
    }

    private void writeSubtask(List<Subtask> subtaskList, BufferedWriter bufferedWriter) throws IOException {
        for (Subtask subtask : subtaskList) {
            bufferedWriter.write(subtaskToString(subtask));
        }
    }

    private String taskToString(Task task) {
        StringBuilder stringBuilder = new StringBuilder(task.getId() + "," + TaskType.TASK + "," + task.getName() + ","
                + task.getDescription() + "," +  task.getStatus());
        if (task.getStartTime().isPresent() && task.getDuration().isPresent()){
            stringBuilder.append(task.getDuration()).append(",")
                    .append(task.getStartTime().get().format(Task.inputFormatter));
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    private String epicToString(Epic epic) {
        StringBuilder stringBuilder = new StringBuilder(epic.getId() + "," + TaskType.EPIC + "," + epic.getName() + ","
                + epic.getDescription() + "," + epic.getStatus());
        if (epic.getStartTime().isPresent() && epic.getDuration().isPresent()){
            stringBuilder.append(epic.getDuration()).append(",")
                    .append(epic.getStartTime().get().format(Task.inputFormatter));
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    private String subtaskToString(Subtask subtask) {
        StringBuilder stringBuilder = new StringBuilder(subtask.getId() + "," + TaskType.SUBTASK + ","
                + subtask.getName() + "," + subtask.getDescription() + "," + subtask.getStatus()
                + "," + subtask.getEpicId());
        if (subtask.getStartTime().isPresent() && subtask.getDuration().isPresent()){
            stringBuilder.append(subtask.getDuration()).append(",")
                    .append(subtask.getStartTime().get().format(Task.inputFormatter));
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    private Task stringToTask(String[] sLine) {
        if (sLine.length == 5) {
            return new Task(Integer.parseInt(sLine[0]), sLine[2], sLine[3], Status.getStatus(sLine[4]));
        }else {
            return new Task(Integer.parseInt(sLine[0]), sLine[2], sLine[3], Status.getStatus(sLine[4]),
                    Long.parseLong(sLine[5]), sLine[6]);
        }
    }

    private Epic stringToEpic(String[] sLine) {
        if (sLine.length == 5) {
            return new Epic(Integer.parseInt(sLine[0]), sLine[2], sLine[3], Status.getStatus(sLine[4]));
        }else {
        return new Epic(Integer.parseInt(sLine[0]), sLine[2], sLine[3], Status.getStatus(sLine[4]),
                Long.parseLong(sLine[5]), sLine[6]);
        }
    }

    private Subtask stringToSubtask(String[] sLine) {
        if (sLine.length == 6) {
            return new Subtask(Integer.parseInt(sLine[0]), sLine[2], sLine[3], Status.getStatus(sLine[4]),
                    Integer.parseInt(sLine[5]));
        }else {
            return new Subtask(Integer.parseInt(sLine[0]), sLine[2], sLine[3], Status.getStatus(sLine[4]),
                    Integer.parseInt(sLine[5]), Long.parseLong(sLine[6]), sLine[7]);
        }
    }

    public static void deleteFile() {
        try {
            if (Files.exists(Paths.get(FILE_NAME))) {
                Files.delete(Paths.get(FILE_NAME));
            }
        } catch (IOException e) {
            System.out.println("Файл не найде");
            throw new RuntimeException("Не повезло");
        }
    }
}
