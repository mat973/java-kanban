package managers;

import dto.EpicDto;
import dto.SubtaskDto;
import dto.TaskDto;
import exeptions.BadMemoryException;
import exeptions.ManagerSaveException;
import task.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public boolean createTask(TaskDto taskDto) {
        if (!super.createTask(taskDto)) {
            return false;
        }
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    @Override
    public boolean createSubTusk(SubtaskDto subtaskDto) {
        if (!super.createSubTusk(subtaskDto)) {
            return false;
        }
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean createEpic(EpicDto epicDto) {
        if (!super.createEpic(epicDto)) {
            return false;
        }
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean removeAllTasks() {
        if (!super.removeAllTasks()) {
            return false;
        }
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean removeAllEpics() {
        if (super.removeAllEpics()) {
            return false;
        }
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean removeAllSubTasks() {
        if (!super.removeAllSubTasks()) {
            return false;
        }
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;

    }

    @Override
    public boolean changeTask(TaskDto taskDto) {

        if (!super.changeTask(taskDto)) {
            return false;
        }

        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean changeSubTask(SubtaskDto subtaskDto) {

        if (!super.changeSubTask(subtaskDto)) {
            return false;
        }

        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean changeEpic(EpicDto epicDto) {
        if (super.changeEpic(epicDto)) {
            return false;
        }
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean removeTaskById(int id) {
        if (!super.removeTaskById(id)) {
            return false;
        }
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean removeEpicById(int id) {
        if (super.removeEpicById(id)) {
            return false;
        }
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean removeSubtaskById(int id) {
        if (!super.removeSubtaskById(id)) {
            return false;
        }
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
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
            return new Task(Integer.parseInt(sLine[0]), sLine[2], sLine[3], Status.getStatus(sLine[4]),
                    localParser(sLine[5]), sLine[6]);
        }
    }

    private Epic stringToEpic(String[] sLine) {
        if (sLine.length == 5) {
            return new Epic(Integer.parseInt(sLine[0]), sLine[2], sLine[3], Status.getStatus(sLine[4]));
        } else {
            return new Epic(Integer.parseInt(sLine[0]), sLine[2], sLine[3], Status.getStatus(sLine[4]),
                    localParser(sLine[5]), sLine[6]);
        }
    }

    private Subtask stringToSubtask(String[] sLine) {
        if (sLine.length == 6) {
            return new Subtask(Integer.parseInt(sLine[0]), sLine[2], sLine[3], Status.getStatus(sLine[4]),
                    Integer.parseInt(sLine[5]));
        } else {
            return new Subtask(Integer.parseInt(sLine[0]), sLine[2], sLine[3], Status.getStatus(sLine[4]),
                    Integer.parseInt(sLine[5]), localParser(sLine[6]), sLine[7]);
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
