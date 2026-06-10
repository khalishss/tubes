package user;

// Class buat nyimpen data akun user saat program jalan
public class Account {
    private String username;
    private String password;
    
    // Constructor Account
    public Account(String username, String password, UserManager userManager){
        if (userManager.login(username, password)){
            this.username = username;
            this.password = password;
        } else {
            throw new IllegalArgumentException("Login data tidak valid");
        }
    }
    
    // Getter username
    public String getUsername(){
        return username;
    }
    
    // Getter password
    public String getPassword(){
        return password;
    }
    
    // Setter password
    public void setPassword(String oldPassword, String newPassword, UserManager userManager){
        if (newPassword == null || newPassword.isEmpty()){
            throw new IllegalArgumentException("Password baru tidak boleh kosong");
        }
        this.password = newPassword;
        userManager.changePassword(username, oldPassword,newPassword);
    }
}
