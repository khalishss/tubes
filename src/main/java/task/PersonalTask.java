package task;

public class PersonalTask extends Task {
    private static final long serialVersionUID = 1L;

    public PersonalTask(String taskName, String description, String createdDate, String dueDate) {
        super(taskName, description, createdDate, dueDate);
    }

    @Override
    public String getTaskType() {
        return "Personal";
    }
}
