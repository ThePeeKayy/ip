package chatot;

import java.util.ArrayList;

/**
 * List object storing tasks.
 */
class TaskList {
    private ArrayList<chatot.Task> tasks;

    /**
     * Initialises an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list given existing tasks.
     * @param tasks the given list of tasks
     */
    public TaskList(ArrayList<chatot.Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a new Task object.
     * @param task the Task Object to add.
     */
    public void addTask(chatot.Task task) {
        assert task != null : "Task should not be null";
        tasks.add(task);
    }

    /**
     * Returns TaskList based on keyword.
     * @param taskName the string of task to find
     * @return the new TaskList filtered object
     */
    public TaskList findTask(String taskName) {
        ArrayList<Task> filtered = new ArrayList<>();
        this.tasks.stream()
                .filter(task -> task.getDescription().contains(taskName))
                .forEach(task -> filtered.add(task));
        return new TaskList(filtered);
    }

    /**
     * Deletes a task.
     * @param index which is the index of the task to delete
     * @return the deleted task
     */
    public Task deleteTask(int index) {
        assert index >= 0 && index < tasks.size() : "Delete should not lie outside size";
        return tasks.remove(index);
    }

    /**
     * Gets the specific Task Index.
     * @param index the index of the task
     * @return the task at the index
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Marks a task.
     * @param index which is the index of the task to mark
     */
    public void markTask(int index) {
        assert index >= 0 && index < tasks.size() : "Mark task should not lie outside size";
        Task selectedTask = tasks.get(index);
        if (!selectedTask.getDone()) {
            selectedTask.switchDone();
        }
        tasks.set(index, selectedTask);
    }

    /**
     * Unmarks a task.
     * @param index which is the index of the task to unmark
     */
    public void unmarkTask(int index) {
        assert index >= 0 && index < tasks.size() : "Unmark should not lie outside size";
        Task selectedTask = tasks.get(index);
        if (selectedTask.getDone()) {
            selectedTask.switchDone();
        }
        tasks.set(index, selectedTask);
    }

    /**
     * Updates a task.
     * @param index which is the index of the task to update
     * @param updatedTask which is the new updated task
     */
    public void updateTask(int index, Task updatedTask) {
        assert index >= 0 && index < tasks.size() : "Edit index should not lie outside size";
        tasks.set(index, updatedTask);
    }

    public int getSize() {
        return tasks.size();
    }

    /**
     * Gets all tasks.
     * @return ArrayList of tasks
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }
}