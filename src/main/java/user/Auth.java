package user;

// Interface buat UserManager
public interface Auth {
    public boolean login(String username, String password);
    public boolean register(String username, String password);
}
