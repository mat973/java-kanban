package managers;

import dto.EpicDto;
import dto.SubtaskDto;
import dto.TaskDto;
import exeptions.*;
import task.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.time.Duration;
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
    public Task createTask(TaskDto taskDto) throws TaskIntersectionException {
        Task task;
        try {
             task = super.createTask(taskDto);
            save();
        } catch (ManagerSaveException | TaskIntersectionException e) {
            System.out.println(e.getMessage());
            throw e;
        }
        return task;
    }

    @Override
    public Subtask createSubTusk(SubtaskDto subtaskDto) throws EpicNotExistException, TaskIntersectionException {
        Subtask subtask;
        try {
            subtask = super.createSubTusk(subtaskDto);
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        return subtask;
    }

    @Override
    public Epic createEpic(EpicDto epicDto) throws ManagerSaveException {
        Epic epic;
        try {
            epic = super.createEpic(epicDto);
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
            throw e;
        }
        return epic;
    }

    @Override
    public void removeAllTasks() throws ManagerSaveException {
        try {
            super.removeAllTasks();
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @Override
    public void removeAllEpics() throws ManagerSaveException {
        try {
            super.removeAllEpics();
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @Override
    public void removeAllSubTasks() throws ManagerSaveException {
        try {
            super.removeAllSubTasks();
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
            throw e;
        }

    }

    @Override
    public Task changeTask(TaskDto taskDto) throws ManagerSaveException, TaskNotFoundException, TaskIntersectionException {
        Task task;
        try {
            task = super.changeTask(taskDto);
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
            throw e;
        }
        return task;
    }

    @Override
    public Subtask changeSubTask(SubtaskDto subtaskDto) throws ManagerSaveException, SubtaskNotFoundException, TaskIntersectionException, EpicNotExistException {
        Subtask subtask;
        try {
            subtask = super.changeSubTask(subtaskDto);
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
            throw e;
        }
        return subtask;
    }

    @Override
    public Epic changeEpic(EpicDto epicDto) throws ManagerSaveException, EpicNotExistException {
        Epic epic;
        try {
            epic = super.changeEpic(epicDto);
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
            throw e;
        }
        return epic;
    }

    @Override
    public void removeTaskById(int id) throws ManagerSaveException, TaskNotFoundException {
        try {
            super.removeTaskById(id);
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @Override
    public void removeEpicById(int id) throws EpicNotExistException, ManagerSaveException {
        try {
            super.removeEpicById(id);
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @Override
    public void removeSubtaskById(int id) throws SubtaskNotFoundException, ManagerSaveException {
        try {
            super.removeSubtaskById(id);
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }


    private void save() throws ManagerSaveException {
        try (FileWriter writer = new FileWriter(FILE_NAME);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            bufferedWriter.write("MaxId:" + (getCurrentId() - 1) + ",id,type,name,status,description,epic\n");
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
                + task.getDescription() + "," + task.getStatus());
        if (task.getStartTime() != null && task.getDuration() != null) {
            Duration duration = task.getDuration();
            long hours = duration.toHours();
            long minutes = duration.toMinutesPart();
            String formattedDuration = String.format("%02d:%02d", hours, minutes);
            stringBuilder.append(",").append(formattedDuration).append(",")
                    .append(task.getStartTime().format(Task.inputFormatter));
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    private String epicToString(Epic epic) {
        StringBuilder stringBuilder = new StringBuilder(epic.getId() + "," + TaskType.EPIC + "," + epic.getName() + ","
                + epic.getDescription() + "," + epic.getStatus());
        if (epic.getStartTime() != null && epic.getDuration() != null) {
            Duration duration = epic.getDuration();
            long hours = duration.toHours();
            long minutes = duration.toMinutesPart();
            String formattedDuration = String.format("%02d:%02d", hours, minutes);
            stringBuilder.append(",").append(formattedDuration).append(",")
                    .append(epic.getStartTime().format(Task.inputFormatter));
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    private String subtaskToString(Subtask subtask) {
        StringBuilder stringBuilder = new StringBuilder(subtask.getId() + "," + TaskType.SUBTASK + ","
                + subtask.getName() + "," + subtask.getDescription() + "," + subtask.getStatus()
                + "," + subtask.getEpicId());
        if (subtask.getStartTime() != null && subtask.getDuration() != null) {
            Duration duration = subtask.getDuration();
            long hours = duration.toHours();
            long minutes = duration.toMinutesPart();
            String formattedDuration = String.format("%02d:%02d", hours, minutes);
            stringBuilder.append(",").append(formattedDuration).append(",")
                    .append(subtask.getStartTime().format(Task.inputFormatter));
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    private Task stringToTask(String[] sLine) {
        if (sLine.length == 5) {
            return new Task(Integer.parseInt(sLine[0]), sLine[2], sLine[3], Status.getStatus(sLine[4]));
        } else {
            Task task = new Task(Integer.parseInt(sLine[0]), sLine[2], sLine[3], Status.getStatus(sLine[4]),
                    localParser(sLine[5]), sLine[6]);
            super.updateTimeMap(task);
            return task;
        }
    }

    private Epic stringToEpic(String[] sLine) {
        if (sLine.length == 5) {
            return new Epic(Integer.parseInt(sLine[0]), sLine[2], sLine[3], Status.getStatus(sLine[4]));
        } else {
            Epic epic = new Epic(Integer.parseInt(sLine[0]), sLine[2], sLine[3], Status.getStatus(sLine[4]),
                    localParser(sLine[5]), sLine[6]);
            super.updateTimeMap(epic);
            return epic;
        }
    }

    private Subtask stringToSubtask(String[] sLine) {
        if (sLine.length == 6) {

            return new Subtask(Integer.parseInt(sLine[0]), sLine[2], sLine[3], Status.getStatus(sLine[4]),
                    Integer.parseInt(sLine[5]));
        } else {
            Subtask subtask = new Subtask(Integer.parseInt(sLine[0]), sLine[2], sLine[3], Status.getStatus(sLine[4]),
                    Integer.parseInt(sLine[5]), localParser(sLine[6]), sLine[7]);
            super.updateTimeMap(subtask);
            return subtask;
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

    private Long localParser(String s) {
        String[] strs = s.split(":");
        return Long.parseLong(strs[0]) * 60 + Long.parseLong(strs[1]);
    }
}
