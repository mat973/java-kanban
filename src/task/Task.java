package task;



public class Task {
    protected int id;
    protected String description;
    protected Condition condition;
    protected String name;


    protected Task(int id,  String name, String description, Condition condition) {
        this.id = id;
        this.description = description;
        this.condition = condition;
        this.name = name;
    }

    protected Task(String name , String description, Condition condition ) {
        this.id = TaskManager.getId();
        this.description = description;
        this.condition = condition;
        this.name = name;
    }


    protected Task(Task task) {
        this.id = task.id;
        this.description = task.description;
        this.condition = task.condition;
        this.name = task.name;
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

    protected void setDescription(String description) {
        this.description = description;
    }

    protected void setCondition(Condition condition) {
        this.condition = condition;
    }

    protected void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "[Название:" + getName() + ", описание:" + getDescription() + ", состояние:"
                + getCondition().name()+ "]";
    }


}
