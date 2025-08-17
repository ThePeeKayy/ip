import java.util.Scanner;
import java.util.ArrayList;

public class Chatot {

    static class Task {
        private String description;
        private boolean isDone;

        // For add
        public Task(String description) {
            this.description = description;
            this.isDone = false;
        }
        // For mark/unmark
        public Task(Task chosenTask) {
            this.description = chosenTask.description;
            this.isDone = !chosenTask.isDone;
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

    static class Todo extends Task {
        public Todo(String description) {
            super(description);
        }

        @Override
        public String toString() {
            return "[T]" + super.toString();
        }
    }

    static class Deadline extends Task {
        protected String by;

        public Deadline(String description, String by) {
            super(description);
            int index = by.indexOf("/by ");
            this.by = by.substring(index + 4);
        }

        @Override
        public String toString() {
            return "[D]" + super.toString() + " (by: " + by + ")";
        }
    }

    static class Event extends Task {
        protected String start;
        protected String end;

        public Event(String description, String details) {
            super(description);
            int startIndex = details.indexOf("/start ");
            int endIndex = details.indexOf("/end ");
            this.start = details.substring(startIndex + 7, endIndex);
            this.end = details.substring(endIndex + 5);
        }

        @Override
        public String toString() {
            return "[E]" + super.toString() + " (from: " + start + " to: " + end + ")";
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Task> taskList = new ArrayList<>();
        String logo = "  ____ |  |__ _____ _/  |_  _____/  |_ \n"
                + "_/ ___\\|  |  \\\\__  \\\\   __\\/  _ \\   __\\\n"
                + "\\  \\___|   Y  \\/ __ \\|  | (  <_> )  |  \n"
                + " \\___  >___|  (____  /__|  \\____/|__|  \n"
                + "     \\/     \\/     \\/                  \n";
        System.out.println("Hello from\n" + logo);

        greet();

        while (true) {
            String currentCommand = sc.nextLine();
            if (currentCommand.equals("bye")) {
                exit();
                break;
            } else if (currentCommand.equals("list")) {
                printList(taskList);
            } else if (currentCommand.startsWith("mark")) {
                int index = Integer.parseInt(currentCommand.split(" ")[1]);
                mark(index-1, taskList);
            } else if (currentCommand.startsWith("unmark")) {
                int index = Integer.parseInt(currentCommand.split(" ")[1]);
                unmark(index-1, taskList);
            } else {
                taskList.add(new Task(currentCommand));
                echo(currentCommand);
            }
        }


    }

    public static void greet() {
        System.out.println("Hello I'm Chatot!");
        System.out.println("What can I do for you?");
    }

    public static void exit() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    public static void echo(String inputTask) {
        System.out.println("added: " + inputTask);
    }

    public static void printList(ArrayList<Task> taskList) {
        for (int i = 0; i < taskList.size(); i++) {
            System.out.println((i+1) + "." + taskList.get(i));
        }
    }

    public static void mark(int index, ArrayList<Task> tasks) {
        Task selectedTask = tasks.get(index);
        if (!selectedTask.getDone()) {
            selectedTask = new Task(selectedTask);
        }
        tasks.set(index, selectedTask);
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(selectedTask);
    }

    public static void unmark(int index, ArrayList<Task> tasks) {
        Task selectedTask = tasks.get(index);
        if (selectedTask.getDone()) {
            selectedTask = new Task(selectedTask);
        }
        tasks.set(index, selectedTask);
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println(selectedTask);
    }

}
