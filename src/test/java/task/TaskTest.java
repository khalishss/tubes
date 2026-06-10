package task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void personalTaskType() {
        Task t = new PersonalTask("A", "desc", "01-01-2025", "02-01-2025");
        assertEquals("Personal", t.getTaskType());
    }

    @Test
    void workTaskType() {
        Task t = new WorkTask("A", "desc", "01-01-2025", "02-01-2025");
        assertEquals("Work", t.getTaskType());
    }

    @Test
    void gettersReturnConstructorValues() {
        Task t = new PersonalTask("Nama", "Deskripsi", "01-01-2025", "10-01-2025");
        assertEquals("Nama", t.getTaskName());
        assertEquals("Deskripsi", t.getDescription());
        assertEquals("01-01-2025", t.getCreatedDate());
        assertEquals("10-01-2025", t.getDueDate());
    }

    @Test
    void settersUpdateValues() {
        Task t = new WorkTask("Nama", "Deskripsi", "01-01-2025", "10-01-2025");
        t.setTaskName("Baru");
        t.setDescription("Deskripsi Baru");
        t.setDueDate("20-01-2025");
        assertEquals("Baru", t.getTaskName());
        assertEquals("Deskripsi Baru", t.getDescription());
        assertEquals("20-01-2025", t.getDueDate());
    }

    @Test
    void toStringContainsName() {
        Task t = new PersonalTask("Penting", "desc", "01-01-2025", "10-01-2025");
        assertTrue(t.toString().contains("Penting"));
    }
}
