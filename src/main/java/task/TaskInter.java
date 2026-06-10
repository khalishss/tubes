package task;

// Interface buat TaskManager
public interface TaskInter {
    public void addTask(String taskName, String description, String dueDate, String taskType);
    public void editTask(int taskID, String newTaskName, String newDescription, String newDueDate);
    public void deleteTask(int taskID);
    public void saveTasks();
    public void loadTasks();
}
