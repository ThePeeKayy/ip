package chatot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class TaskListTest {
    private TaskList taskList;
    private Todo todo1;
    private Todo todo2;

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
        todo1 = new Todo("Task 1");
        todo2 = new Todo("Task 2");

        taskList.addTask(todo1);
        taskList.addTask(todo2);
    }

    @Test
    public void testMarkTaskWhenNotDone() {
        assertFalse(taskList.get(0).getDone());
        taskList.markTask(0);
        assertTrue(taskList.get(0).getDone());
    }

    @Test
    public void testMarkTaskWhenAlreadyDone() {
        taskList.markTask(0);
        assertTrue(taskList.get(0).getDone());

        // Mark again - should remain done
        taskList.markTask(0);
        assertTrue(taskList.get(0).getDone());
    }

    @Test
    public void testDeleteTaskReturnsCorrectTask() {
        Task deleted = taskList.deleteTask(0);
        assertEquals("Task 1", deleted.getDescription());
        assertEquals(1, taskList.size());
        assertEquals("Task 2", taskList.get(0).getDescription());
    }

    @Test
    public void testUnmarkTaskWhenDone() {
        taskList.markTask(1);
        assertTrue(taskList.get(1).getDone());

        taskList.unmarkTask(1);
        assertFalse(taskList.get(1).getDone());
    }
}