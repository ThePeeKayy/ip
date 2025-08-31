package chatot;

import java.util.Scanner;

/**
 * Handles user interface interactions.
 */
class Ui {
    private Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public String showWelcome() {
        return "Hello I'm Chatot! \n" + "What can I do for you?";
    }

    public String showGoodbye() {
        return "Bye. Hope to see you again soon!";
    }

    public String showError(Exception e) {
        return e.getMessage();
    }

    public String showErrorMessage(String message) {
        return "Error: " + message;
    }

    public String showTaskAdded(Task task, int totalTasks) {
        return "Got it. I've added this task: \n" +
        task + "\n" +
        "Now you have " + totalTasks + " tasks in the list.";
    }

    public String showTaskRemoved(Task task, int totalTasks) {
        return "Noted. I've removed this task:\n" + task + "\n" + "Now you have " + totalTasks + " tasks in the list.\n";
    }

    public String showTaskMarked(Task task) {
        return "Nice! I've marked this task as done:\n" + task + "\n";
    }

    public String showTaskUnmarked(Task task) {
        return "OK, I've marked this task as not done yet:\n" + task + "\n";
    }

    public String showTaskUpdated(Task task) {
        return "Amazing, I've edited this task:\n" + task + "\n";
    }

    public String showTaskList(TaskList taskList) {
        String output = "";
        for (int i = 0; i < taskList.getSize(); i++) {
            output += (i+1) + "." + taskList.get(i)  + "\n";;
        }
        if (output.isEmpty()) {
            return "No tasks found. Tell Chatot some commands";
        }
        return "Here are your tasks: \n" + output;

    }

    public String showCommandNotRecognised() {
        return "Command not recognised! Check out our user guide!";
    }

    public String showLoadingError() {
        return "No previous data retrieved";
    }
}