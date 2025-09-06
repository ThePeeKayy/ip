package chatot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * JUnit tests for the Storage class.
 */
class StorageTest {

    @TempDir
    Path tempDir;

    private Storage storage;
    private String testFilePath;

    @BeforeEach
    void setUp() {
        testFilePath = tempDir.resolve("test_tasks.txt").toString();
        storage = new Storage(testFilePath);
    }

    @AfterEach
    void tearDown() {
        // Cleanup is handled by @TempDir automatically
    }

    @Test
    void testConstructor() {
        Storage testStorage = new Storage("test/path.txt");
        assertNotNull(testStorage);
    }

    @Test
    void testLoadEmptyFile() throws Exception {
        // Create empty file
        new File(testFilePath).createNewFile();

        ArrayList<Task> tasks = storage.load();

        assertNotNull(tasks);
        assertTrue(tasks.isEmpty());
    }

    @Test
    void testLoadTodoTasks() throws Exception {
        // Create test file with Todo tasks
        try (FileWriter writer = new FileWriter(testFilePath)) {
            writer.write("[T][ ] read book\n");
            writer.write("[T][X] finish homework\n");
        }

        ArrayList<Task> tasks = storage.load();

        assertEquals(2, tasks.size());
        assertTrue(tasks.get(0) instanceof Todo);
        assertTrue(tasks.get(1) instanceof Todo);
        assertFalse(tasks.get(0).getDone());
        assertTrue(tasks.get(1).getDone());
    }

    @Test
    void testLoadDeadlineTasks() throws Exception {
        // Create test file with Deadline tasks
        try (FileWriter writer = new FileWriter(testFilePath)) {
            writer.write("[D][ ] submit assignment (by: 2023-12-01)\n");
            writer.write("[D][X] pay bills (by: 2023-11-15)\n");
        }

        ArrayList<Task> tasks = storage.load();

        assertEquals(2, tasks.size());
        assertTrue(tasks.get(0) instanceof Deadline);
        assertTrue(tasks.get(1) instanceof Deadline);
        assertFalse(tasks.get(0).getDone());
        assertTrue(tasks.get(1).getDone());
    }

    @Test
    void testLoadEventTasks() throws Exception {
        // Create test file with Event tasks
        try (FileWriter writer = new FileWriter(testFilePath)) {
            writer.write("[E][ ] team meeting (from: 2023-12-01 to: 2023-12-01)\n");
            writer.write("[E][X] conference (from: 2023-11-20 to: 2023-11-22)\n");
        }

        ArrayList<Task> tasks = storage.load();

        assertEquals(2, tasks.size());
        assertTrue(tasks.get(0) instanceof Event);
        assertTrue(tasks.get(1) instanceof Event);
        assertFalse(tasks.get(0).getDone());
        assertTrue(tasks.get(1).getDone());
    }

    @Test
    void testLoadMixedTasks() throws Exception {
        // Create test file with mixed task types
        try (FileWriter writer = new FileWriter(testFilePath)) {
            writer.write("[T][ ] read book\n");
            writer.write("[D][X] submit assignment (by: 2023-12-01)\n");
            writer.write("[E][ ] team meeting (from: 2023-12-01 to: 2023-12-01)\n");
        }

        ArrayList<Task> tasks = storage.load();

        assertEquals(3, tasks.size());
        assertTrue(tasks.get(0) instanceof Todo);
        assertTrue(tasks.get(1) instanceof Deadline);
        assertTrue(tasks.get(2) instanceof Event);
    }

    @Test
    void testLoadInvalidTaskType() throws Exception {
        // Create test file with invalid task type
        try (FileWriter writer = new FileWriter(testFilePath)) {
            writer.write("[X][ ] invalid task\n");
            writer.write("[T][ ] valid task\n");
        }

        ArrayList<Task> tasks = storage.load();

        // Should only load the valid task, ignoring the invalid one
        assertEquals(1, tasks.size());
        assertTrue(tasks.get(0) instanceof Todo);
    }

    @Test
    void testLoadFileNotFound() {
        Storage nonExistentStorage = new Storage("non/existent/file.txt");

        assertThrows(Exception.class, () -> {
            nonExistentStorage.load();
        });
    }

    @Test
    void testSaveEmptyTaskList() {
        ArrayList<Task> emptyTasks = new ArrayList<>();

        assertDoesNotThrow(() -> {
            storage.save(emptyTasks);
        });

        // Verify file was created and is empty
        File file = new File(testFilePath);
        assertTrue(file.exists());
        assertEquals(0, file.length());
    }

    @Test
    void testSaveTasks() throws Exception {
        // Create mock tasks (you'll need to adjust based on your Task constructors)
        ArrayList<Task> tasks = new ArrayList<>();

        // Note: You'll need to adjust these constructors based on your actual Task classes
        Todo todo = new Todo("read book", false);
        Deadline deadline = new Deadline("submit assignment", "2023-12-01", true);
        Event event = new Event("team meeting", "2023-12-01 to: 2023-12-01", false);

        tasks.add(todo);
        tasks.add(deadline);
        tasks.add(event);

        storage.save(tasks);

        // Verify file was created
        File file = new File(testFilePath);
        assertTrue(file.exists());
        assertTrue(file.length() > 0);

        // Load and verify the saved tasks
        ArrayList<Task> loadedTasks = storage.load();
        assertEquals(3, loadedTasks.size());
    }

    @Test
    void testSaveCreatesDataDirectory() {
        // Test with a path that includes the data directory
        String dataFilePath = tempDir.resolve("data").resolve("tasks.txt").toString();
        Storage dataStorage = new Storage(dataFilePath);

        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Todo("test task", false));

        assertDoesNotThrow(() -> {
            dataStorage.save(tasks);
        });

        // Verify data directory was created
        File dataDir = tempDir.resolve("data").toFile();
        assertTrue(dataDir.exists());
        assertTrue(dataDir.isDirectory());
    }

    @Test
    void testSaveAndLoadRoundTrip() throws Exception {
        // Create tasks, save them, then load and verify they match
        ArrayList<Task> originalTasks = new ArrayList<>();
        originalTasks.add(new Todo("task 1", false));
        originalTasks.add(new Todo("task 2", true));
        originalTasks.add(new Deadline("deadline task", "2023-12-01", false));
        originalTasks.add(new Event("event task", "2023-12-01 to: 2023-12-01", true));

        storage.save(originalTasks);
        ArrayList<Task> loadedTasks = storage.load();

        assertEquals(originalTasks.size(), loadedTasks.size());

        for (int i = 0; i < originalTasks.size(); i++) {
            assertEquals(originalTasks.get(i).getDone(), loadedTasks.get(i).getDone());
            assertEquals(originalTasks.get(i).getClass(), loadedTasks.get(i).getClass());
        }
    }

    @Test
    void testLoadMalformedLine() throws Exception {
        // Create test file with malformed lines
        try (FileWriter writer = new FileWriter(testFilePath)) {
            writer.write("[T][ ] valid task\n");
            writer.write("invalid line format\n");
            writer.write("[T][X] another valid task\n");
        }

        // This test depends on how your code handles malformed lines
        // It might throw an exception or skip the line
        assertDoesNotThrow(() -> {
            ArrayList<Task> tasks = storage.load();
            // Should load the valid tasks and handle malformed lines gracefully
            assertEquals(2, tasks.size());
        });
    }
}