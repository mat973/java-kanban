package task;


import java.util.Objects;

public class Task {

    protected int id;
    protected String description;
    protected Status status;
    protected String name;


    protected Task(int id, String name, String description, Status status) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.name = name;
    }


    protected Task(Task task) {
        this.id = task.id;
        this.description = task.description;
        this.status = task.status;
        this.name = task.name;
    }


    protected int getId() {
        return id;
    }

    protected String getDescription() {
        return description;
    }

    protected Status getCondition() {
        return status;
    }

    protected String getName() {
        return name;
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    protected void setCondition(Status status) {
        this.status = status;
    }

    protected void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ID:" + getId() + " [Название:" + getName() + ", описание:" + getDescription() + ", состояние:"
                + getCondition().name() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
