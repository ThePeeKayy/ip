package chatot;

/**
 * Represents a basic task.
 */
public class Task {
    private String description;
    private boolean isDone;

    /**
     * Creates a new task with description. Defaults to not done based on requirements.
     * @param description the task description in string
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Creates a task when given description and completion status.
     * @param description the task description
     * @param isDone whether the task is completed
     */
    public Task(String description, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
    }

    /**
     * Toggles the completion status. Logic of scenarios to toggle handled externally.
     */
    public void switchDone() {
        this.isDone = !this.isDone;
    }

    public String getDescription() {
        return description;
    }

    public boolean getDone() {
        return isDone;
    }

    public String toString() {
        return "[" + (isDone ? "X" : " ") + "] " + description;
    }
}