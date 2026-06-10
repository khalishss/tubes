package tasklist;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import task.TaskManager;
import user.Account;
import user.UserManager;

import java.io.File;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

/**
 * End-to-end integration test exercising the full flow:
 * register a user -> authenticate via Account -> create and persist tasks.
 *
 * Runs in the integration-test phase via the Failsafe plugin (*IT suffix).
 *
 * UserManager uses a fixed "user.dat" in the working directory, so that file
 * is cleaned around the test. Task files are placed inside a JUnit TempDir by
 * passing a full path as the task "username".
 */
class TaskListFlowIT {

    @TempDir
    Path tempDir;

    private void cleanUserFile() {
        new File(UserManager.getUserFile()).delete();
    }

    private static String futureDate(int daysAhead) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, daysAhead);
        return new SimpleDateFormat("dd-MM-yyyy").format(c.getTime());
    }

    @BeforeEach
    void setUp() {
        cleanUserFile();
    }

    @AfterEach
    void tearDown() {
        cleanUserFile();
    }

    @Test
    @DisplayName("full register -> login -> add tasks -> reload flow")
    void fullUserAndTaskFlow() {
        // 1. Register and authenticate
        UserManager userManager = new UserManager();
        assertTrue(userManager.register("integ", "pw123"));

        Account account = new Account("integ", "pw123", userManager);
        assertEquals("integ", account.getUsername());

        // 2. Create tasks for that user (stored inside the temp dir)
        String taskKey = tempDir.resolve(account.getUsername()).toString();
        TaskManager taskManager = new TaskManager(taskKey);
        taskManager.addTask("Tugas Pribadi", "deskripsi", futureDate(4), "Personal");
        taskManager.addTask("Tugas Kerja", "deskripsi", futureDate(8), "Work");
        assertEquals(2, taskManager.getTasks().size());

        // 3. Reload from disk -> data must persist
        TaskManager reloaded = new TaskManager(taskKey);
        assertEquals(2, reloaded.getTasks().size());
        assertEquals("Personal", reloaded.getTasks().get(0).getTaskType());
        assertEquals("Work", reloaded.getTasks().get(1).getTaskType());

        // 4. Change password and confirm login reflects it
        account.setPassword("pw123", "newpw", userManager);
        assertTrue(userManager.login("integ", "newpw"));
        assertFalse(userManager.login("integ", "pw123"));
    }
}
