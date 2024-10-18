package task;



public class Task {
    protected int id;
    protected String description;
    protected Condition condition;
    protected String name;

     Task(String name ,String description, Condition condition ) {
        this.id = TaskManager.getId();
        this.description = description;
        this.condition = condition;
        this.name = name;
    }


    protected int getId() {
        return id;
    }

    protected String getDescription() {
        return description;
    }

    protected Condition getCondition() {
        return condition;
    }

    protected String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "[Название:" + getName() + ", описание:" + getDescription() + ", состояние:"
                + getCondition().name()+ "]";
    }


}
