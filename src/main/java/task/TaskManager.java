package task;

import java.io.*;
import java.text.*;
import java.util.*;

// Class buat mengatur data Tugas
public class TaskManager implements TaskInter{
    private final String taskFile;
    private List<Task> tasks = new ArrayList<>();
    
    // Constructor
    @SuppressWarnings("this-escape")
    public TaskManager(String username){
        this.taskFile = username + ".dat";
        loadTasks();
    }
    
    // Buat tambah tugas
    @Override
    public void addTask(String taskName, String description, String dueDate, String taskType){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setLenient(false);
        try {
            Date due = sdf.parse(dueDate);
            Date today = new Date();
            if (due.before(today)){
                System.out.println("Error: Tenggat waktu tidak boleh kemarin.");
                return;
            }
            String createdDate = sdf.format(new Date());
            Task newTask;
            if (taskType.equalsIgnoreCase("Personal")) {
                newTask = new PersonalTask(taskName, description, createdDate, dueDate);
            } else if (taskType.equalsIgnoreCase("Work")) {
                newTask = new WorkTask(taskName, description, createdDate, dueDate);
            } else {
                System.out.println("Error: Jenis tugas hanya bisa 'Personal' atau 'Work'.");
                return;
            }
            tasks.add(newTask);
            saveTasks();
            System.out.println("Tugas berhasil ditambahkan.");
        } catch (Exception e){
            System.out.println("Error: Format tanggal tidak valid. Gunakan format (dd-MM-yyyy).");
        }
    }
    
    // Buat edit task
    @Override
    public void editTask(int taskID, String newTaskName, String newDescription, String newDueDate){
        if (taskID >= 0 && taskID < tasks.size()){
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            sdf.setLenient(false);
            try {
                Date due = sdf.parse(newDueDate);
                Date today = new Date();
                if (due.before(today)){
                    System.out.println("Error: Tenggat waktu tidak boleh kemarin.");
                    return;
                }
                Task existingTask = tasks.get(taskID);
                existingTask.setTaskName(newTaskName);
                existingTask.setDescription(newDescription);
                existingTask.setDueDate(newDueDate);
                saveTasks();
                System.out.println("Tugas berhasil diubah.");
            } catch (Exception e){
                System.out.println("Error: Format tanggal tidak valid. Gunakan format (dd-MM-yyyy).");
            }
            
        } else {
            System.out.println("Tugas ID tidak valid!");
        }
    }
    
    // Buat hapus task
    @Override
    public void deleteTask(int taskID){
        if (taskID >= 0 && taskID < tasks.size()){
            tasks.remove(taskID);
            saveTasks();
        } else {
            System.out.println("Tugas ID tidak valid!");
        }
    }
    
    // Buat save task ke file .dat
    @Override
    public void saveTasks(){
        if (tasks.isEmpty()){
            new File(taskFile).delete();
            return;
        }
        File file = new File(taskFile);
        if (taskUsers()){
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))){
                oos.writeObject(tasks);
            } catch (IOException e){
                System.err.println("Gagal menyimpan tugas: " + e.getMessage());
            }
        }
    }
    
    // Buat load task dari file .dat
    @Override
    @SuppressWarnings("unchecked")
    public void loadTasks(){
        File file = new File(taskFile);
        if (file.exists()){
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
                tasks = (List<Task>) ois.readObject();
            } catch (Exception e){
                System.err.println("Gagal memuat tugas: " + e.getMessage());
            }
        }
    }
    
    // Method membuat file data akun
    public boolean taskUsers(){
        File file = new File(taskFile);
        if (!file.exists()){
            try {
                return file.createNewFile();
            } catch (IOException e){
                System.err.println("Gagal membuat file tugas: " + e.getMessage());
            }
        }
        return true;
    }
    
    // Method untuk menampilkan tugas
    public void displayTasks(){
        if (tasks.isEmpty()){
            System.out.println("Belum ada tugas.");
            return;
        }
        System.out.println("\n========== Daftar Tugas ==========");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date today = new Date();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            System.out.println("Tugas " + (i+1));
            System.out.println("Nama Tugas\t: " + task.getTaskName());
            System.out.println("Deskripsi\t: " + task.getDescription());
            System.out.println("Dibuat Tanggal\t: " + task.getCreatedDate());
            System.out.println("Tenggat Waktu\t: " + task.getDueDate());
            System.out.println("Jenis Tugas\t: " + task.getTaskType());
            try {
                Date dueDate = sdf.parse(task.getDueDate());
                long diffInMills = dueDate.getTime() - today.getTime();
                long daysRemaining = diffInMills / (1000 * 60 * 60 * 24);
                if (daysRemaining >= 0){
                    System.out.println("Sisa Waktu\t: " + daysRemaining + " hari lagi.");
                } else {
                    System.out.println("Status\t: Sudah melewati tenggat waktu.");
                }
            } catch (Exception e){
                System.out.println("Status\t: Error membaca tanggal tenggat.");
            }
            System.out.println();
        }
    }
    
    // Getter untuk Tugas
    public List<Task> getTasks(){
        return tasks;
    }
}
