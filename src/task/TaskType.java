package task;

public enum TaskType {
    TASK,
    EPIC,
    SUBTASK;
    public static TaskType getTaskType(String type) {
        for (TaskType taskType : values()) {
            if (taskType.name().equals(type)) {
                return taskType;
            }
        }
        throw new IllegalArgumentException("Тип задачи не найден: " + type);
    }
}
