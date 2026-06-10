package user;

import org.junit.jupiter.api.*;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private UserManager userManager;

    private void cleanDataFiles() {
        new File(UserManager.getUserFile()).delete();
        new File("budi.dat").delete();
    }

    @BeforeEach
    void setUp() {
        cleanDataFiles();
        userManager = new UserManager();
        userManager.register("budi", "secret");
    }

    @AfterEach
    void tearDown() {
        cleanDataFiles();
    }

    @Test
    @DisplayName("Account is created for valid credentials")
    void createValidAccount() {
        Account account = new Account("budi", "secret", userManager);
        assertEquals("budi", account.getUsername());
        assertEquals("secret", account.getPassword());
    }

    @Test
    @DisplayName("Account creation throws for invalid credentials")
    void createInvalidAccount() {
        assertThrows(IllegalArgumentException.class,
                () -> new Account("budi", "wrong", userManager));
    }

    @Test
    @DisplayName("setPassword updates the password")
    void setPassword() {
        Account account = new Account("budi", "secret", userManager);
        account.setPassword("secret", "newpass", userManager);
        assertEquals("newpass", account.getPassword());
        assertTrue(userManager.login("budi", "newpass"));
    }

    @Test
    @DisplayName("setPassword rejects an empty new password")
    void setEmptyPassword() {
        Account account = new Account("budi", "secret", userManager);
        assertThrows(IllegalArgumentException.class,
                () -> account.setPassword("secret", "", userManager));
    }
}
