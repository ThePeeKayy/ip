package chatot;

/**
 * Represents a simple task. No time attributes.
 */
public class Todo extends Task {
    /**
     * Creates a new Todo object.
     * @param description the task description
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Creates a Todo task when also given completion.
     * @param description the task description
     * @param isDone whether the task is completed
     */
    public Todo(String description, boolean isDone) {
        super(description, isDone);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}