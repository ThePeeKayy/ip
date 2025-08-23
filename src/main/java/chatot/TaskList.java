package chatot;

import java.util.ArrayList;

/**
 * List object storing tasks.
 */
public class TaskList {
    private ArrayList<Task> tasks;

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
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a new Task object.
     * @param task the Task Object to add.
     */
    public void addTask(Task task) {
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

    public Task deleteTask(int index) {
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

    public void markTask(int index) {
        Task selectedTask = tasks.get(index);
        if (!selectedTask.getDone()) {
            selectedTask.switchDone();
        }
        tasks.set(index, selectedTask);
    }

    public void unmarkTask(int index) {
        Task selectedTask = tasks.get(index);
        if (selectedTask.getDone()) {
            selectedTask.switchDone();
        }
        tasks.set(index, selectedTask);
    }

    public int getSize() {
        return tasks.size();
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }
}