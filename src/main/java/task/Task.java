package task;

import java.io.Serializable;

// Class buat nyimpen data Tugas saat program jalan
public abstract class Task implements Serializable{
    private static final long serialVersionUID = 1L;
    private String taskName;
    private String description;
    private String createdDate;
    private String dueDate;
    
    // Constructor
    public Task(String taskName, String description, String createdDate, String dueDate){
        this.taskName = taskName;
        this.description = description;
        this.createdDate = createdDate;
        this.dueDate = dueDate;
    }
    
    // Getter nama tugas
    public String getTaskName() {
        return taskName;
    }
    
    // Getter deskripsi tugas
    public String getDescription() {
        return description;
    }

    // Getter tanggal pembuatan tugas
    public String getCreatedDate() {
        return createdDate;
    }

    // Getter tanggal tenggat tugas
    public String getDueDate() {
        return dueDate;
    }
    
    // Setter nama tugas
    public void setTaskName(String taskName){
        this.taskName = taskName;
    }
    
    // Setter deskripsi tugas
    public void setDescription(String description){
        this.description = description;
    }
    
    // Setter tenggat tugas
    public void setDueDate(String dueDate){
        this.dueDate = dueDate;
    }
    
    public abstract String getTaskType();
    
    @Override
    public String toString(){
        return "Tugas: " + taskName + "\nDeskripsi: " + description + "\nDibuat pada: " + createdDate + "\nTenggat Waktu: " + dueDate;
    }
}