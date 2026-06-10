package user;

import org.junit.jupiter.api.*;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link UserManager}.
 *
 * UserManager persists to a fixed "user.dat" in the JVM working directory.
 * The JVM working directory cannot be reliably changed at runtime, so instead
 * of relocating it we simply delete the data files before and after each test
 * to guarantee isolation regardless of test execution order.
 */
class UserManagerTest {

    private UserManager userManager;

    private void cleanDataFiles() {
        new File(UserManager.getUserFile()).delete();
        // any per-user task file these tests might create
        new File("budi.dat").delete();
    }

    @BeforeEach
    void setUp() {
        cleanDataFiles();
        userManager = new UserManager();
    }

    @AfterEach
    void tearDown() {
        cleanDataFiles();
    }

    @Test
    @DisplayName("register then login succeeds")
    void registerAndLogin() {
        assertTrue(userManager.register("budi", "secret"));
        assertTrue(userManager.login("budi", "secret"));
    }

    @Test
    @DisplayName("login fails with the wrong password")
    void loginWrongPassword() {
        userManager.register("budi", "secret");
        assertFalse(userManager.login("budi", "wrong"));
    }

    @Test
    @DisplayName("login fails for an unknown user")
    void loginUnknownUser() {
        assertFalse(userManager.login("ghost", "x"));
    }

    @Test
    @DisplayName("registering a duplicate username fails")
    void duplicateRegisterFails() {
        assertTrue(userManager.register("budi", "secret"));
        assertFalse(userManager.register("budi", "other"));
    }

    @Test
    @DisplayName("changePassword updates credentials")
    void changePassword() {
        userManager.register("budi", "old");
        userManager.changePassword("budi", "old", "new");
        assertFalse(userManager.login("budi", "old"));
        assertTrue(userManager.login("budi", "new"));
    }

    @Test
    @DisplayName("changePassword keeps old credentials when old password is wrong")
    void changePasswordWrongOld() {
        userManager.register("budi", "old");
        userManager.changePassword("budi", "incorrect", "new");
        assertTrue(userManager.login("budi", "old"));
    }

    @Test
    @DisplayName("deleteAccount removes the user")
    void deleteAccount() {
        userManager.register("budi", "secret");
        userManager.deleteAccount("budi", "secret");
        assertFalse(userManager.login("budi", "secret"));
    }

    @Test
    @DisplayName("deleteAccount with bad credentials throws")
    void deleteAccountBadCredentials() {
        userManager.register("budi", "secret");
        assertThrows(IllegalArgumentException.class,
                () -> userManager.deleteAccount("budi", "wrong"));
    }

    @Test
    @DisplayName("getUserFile returns the data file name")
    void userFileName() {
        assertEquals("user.dat", UserManager.getUserFile());
    }
}
