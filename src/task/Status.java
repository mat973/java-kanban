package task;

public enum Status {
    NEW,
    IN_PROGRESS,
    DONE;
    public static Status getStatus(String stat) {
        if (stat == null) {
            throw new IllegalArgumentException("Строка не может быть null");
        }
        for (Status status : values()) {
            if (status.name().equals(stat)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Статус не найден: " + stat);
    }
}


