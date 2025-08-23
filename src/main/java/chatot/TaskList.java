package chatot;

import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public Task deleteTask(int index) {
        return tasks.remove(index);
    }

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