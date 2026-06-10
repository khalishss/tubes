package user;

import java.io.*;
import java.util.*;

// Class buat mengatur data akun user
public class UserManager implements Auth{
    private static final String USER_FILE = "user.dat";
    private HashMap<String, String> users = new HashMap<>();
    
    // Getter buat USER_FILE
    public static String getUserFile(){
        return USER_FILE;
    }
    
    // Constructor
    public UserManager(){
        loadUsers();
    }
    
    // Method untuk login akun
    @Override
    public boolean login(String username, String password){
        return users.containsKey(username) && users.get(username).equals(password);
    }
    
    // Method untuk register akun
    @Override
    public boolean register(String username, String password){
        if (!users.containsKey(username)){
            users.put(username, password);
            saveUsers();
            System.out.println("Berhasil membuat akun.");
            return true;
        } else {
            System.out.println("Username sudah digunakan!");
            return false;
        }
    }
    
    // Method untuk load data akun user dari file
    @SuppressWarnings("unchecked")
    private void loadUsers(){
        File file = new File(USER_FILE);
        if (file.exists()){
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
                Object obj = ois.readObject();
                if (obj instanceof HashMap){
                    users = (HashMap<String,String>) obj;
                } else {
                    System.err.println("Format data tidak valid");
                }
            } catch (Exception e){
                System.err.println("Gagal memuat akun: " + e.getMessage());
            }
        }
    }
    
    // Method untuk save data akun user ke file
    private void saveUsers(){
        if (users.isEmpty()){
            new File(USER_FILE).delete();
            return;
        }
        File file = new File(USER_FILE);
        if (fileUsers()){
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))){
                oos.writeObject(users);
            } catch (IOException e){
                System.err.println("Gagal menyimpan akun: " + e.getMessage());
            }
        }
    }
    
    // Method membuat file data akun
    public boolean fileUsers(){
        File file = new File(USER_FILE);
        if (!file.exists()){
            try {
                return file.createNewFile();
            } catch (IOException e){
                System.err.println("Gagal membuat user file: " + e.getMessage());
            }
        }
        return true;
    }
    
    // Method untuk ubah password
    public void changePassword(String username, String oldPassword, String newPassword){
        if (users.containsKey(username)){
            if (login(username, oldPassword)){
                users.put(username, newPassword);
                saveUsers();
                System.out.println("Password berhasil diubah.");
            } else {
                System.out.println("Password salah");
            }
        } else {
            System.out.println("Username tidak ditemukan!");
        }
    }
    
    // Method buat delete Account
    public void deleteAccount(String username, String password){
        if (users.containsKey(username) && users.get(username).equals(password)){
            users.remove(username);
            saveUsers();
            File userFile = new File(username + ".dat");
            if (userFile.exists() && userFile.delete()){
                System.out.println("File tugas pengguna berhasil dihapus.");
            }
            System.out.println("Akun berhasil dihapus. Terima Kasih telah menggunakan program ini");
        } else {
            throw new IllegalArgumentException("Akun tidak ditemukan atau password salah.");
        }
    }
}
