package task;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link TaskManager}.
 *
 * TaskManager builds its data file as "<username>.dat". By passing a full
 * path inside a JUnit {@link TempDir} as the "username", every .dat file is
 * written into a fresh temp directory that JUnit deletes automatically, so
 * tests are fully isolated from each other and from the repo.
 */
class TaskManagerTest {

    @TempDir
    Path tempDir;

    private TaskManager manager;

    private static String futureDate(int daysAhead) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, daysAhead);
        return new SimpleDateFormat("dd-MM-yyyy").format(c.getTime());
    }

    private static String pastDate(int daysAgo) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -daysAgo);
        return new SimpleDateFormat("dd-MM-yyyy").format(c.getTime());
    }

    private String userKey(String name) {
        return tempDir.resolve(name).toString();
    }

    @BeforeEach
    void setUp() {
        manager = new TaskManager(userKey("tester"));
    }

    @Test
    @DisplayName("addTask stores a Personal task")
    void addPersonalTask() {
        manager.addTask("Belajar", "Belajar CI/CD", futureDate(5), "Personal");
        assertEquals(1, manager.getTasks().size());
        Task t = manager.getTasks().get(0);
        assertEquals("Belajar", t.getTaskName());
        assertEquals("Personal", t.getTaskType());
    }

    @Test
    @DisplayName("addTask stores a Work task")
    void addWorkTask() {
        manager.addTask("Laporan", "Bikin laporan", futureDate(3), "Work");
        assertEquals(1, manager.getTasks().size());
        assertEquals("Work", manager.getTasks().get(0).getTaskType());
    }

    @Test
    @DisplayName("addTask rejects an invalid task type")
    void rejectInvalidType() {
        manager.addTask("X", "desc", futureDate(2), "Unknown");
        assertTrue(manager.getTasks().isEmpty());
    }

    @Test
    @DisplayName("addTask rejects a past due date")
    void rejectPastDueDate() {
        manager.addTask("X", "desc", pastDate(2), "Personal");
        assertTrue(manager.getTasks().isEmpty());
    }

    @Test
    @DisplayName("addTask rejects an invalid date format")
    void rejectInvalidDateFormat() {
        manager.addTask("X", "desc", "2025/01/01", "Personal");
        assertTrue(manager.getTasks().isEmpty());
    }

    @Test
    @DisplayName("editTask updates name, description and due date")
    void editTask() {
        manager.addTask("Old", "old desc", futureDate(5), "Personal");
        manager.editTask(0, "New", "new desc", futureDate(10));
        Task t = manager.getTasks().get(0);
        assertEquals("New", t.getTaskName());
        assertEquals("new desc", t.getDescription());
    }

    @Test
    @DisplayName("editTask ignores an out-of-range id")
    void editInvalidId() {
        manager.addTask("Old", "old desc", futureDate(5), "Personal");
        manager.editTask(99, "New", "new desc", futureDate(10));
        assertEquals("Old", manager.getTasks().get(0).getTaskName());
    }

    @Test
    @DisplayName("deleteTask removes the task")
    void deleteTask() {
        manager.addTask("ToDelete", "desc", futureDate(5), "Personal");
        manager.deleteTask(0);
        assertTrue(manager.getTasks().isEmpty());
    }

    @Test
    @DisplayName("tasks survive being reloaded from disk")
    void tasksPersistAcrossReload() {
        String user = userKey("persist");
        TaskManager m1 = new TaskManager(user);
        m1.addTask("Persisted", "desc", futureDate(7), "Work");

        TaskManager m2 = new TaskManager(user);
        assertEquals(1, m2.getTasks().size());
        assertEquals("Persisted", m2.getTasks().get(0).getTaskName());
    }
}
