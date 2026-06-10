package task;

public class WorkTask extends Task{
    private static final long serialVersionUID = 1L;

    public WorkTask(String taskName, String description, String createdDate, String dueDate) {
        super(taskName, description, createdDate, dueDate);
    }

    @Override
    public String getTaskType() {
        return "Work";
    }
}
