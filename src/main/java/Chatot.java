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
            int startIndex = details.indexOf("/from ");
            int endIndex = details.indexOf("/to ");
            this.start = details.substring(startIndex + 6, endIndex);
            this.end = details.substring(endIndex + 4);
        }

        @Override
        public String toString() {
            return "[E]" + super.toString() + " (from: " + start + "to: " + end + ")";
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
                if (taskList.size() == 0) {
                    System.out.println("Your list is empty!");
                    continue;
                }
                printList(taskList);
            } else if (currentCommand.startsWith("mark")) {
                if (taskList.size() == 0) {
                    System.out.println("Your list is empty!");
                    continue;
                }
                int index = Integer.parseInt(currentCommand.split(" ")[1]);
                mark(index-1, taskList);
            } else if (currentCommand.startsWith("unmark")) {
                if (taskList.size() == 0) {
                    System.out.println("Your list is empty!");
                    continue;
                }
                int index = Integer.parseInt(currentCommand.split(" ")[1]);
                unmark(index-1, taskList);
            } else if (currentCommand.startsWith("todo ")) {
                try {
                    if (currentCommand.length() <= 5) {
                        throw new StringIndexOutOfBoundsException("Task too short");
                    }
                } catch (StringIndexOutOfBoundsException e) {
                    System.out.println("Error: Todo cannot be empty!");
                    continue;
                }
                String taskDesc = currentCommand.substring(5);
                System.out.println("Got it. I've added this task:");
                Todo targetTodo = new Todo(taskDesc);
                taskList.add(targetTodo);
                System.out.println("Now you have " + taskList.size() +  " tasks in the list.");
            } else if (currentCommand.startsWith("deadline ")) {
                try {
                    if (currentCommand.length() <= 9) {
                        throw new StringIndexOutOfBoundsException("Task too short");
                    }
                } catch (StringIndexOutOfBoundsException e) {
                    System.out.println("Error: Deadline cannot be empty!");
                    continue;
                }
                String withoutCmd = currentCommand.substring(9);
                int detailIndex = withoutCmd.indexOf("/by ");
                if (detailIndex == -1) {
                    System.out.println("Error: Missing '/by' in command");
                    continue;
                }
                if (detailIndex == 9) {
                    System.out.println("Error: Missing Actual task");
                    continue;
                }
                String taskDesc = withoutCmd.substring(0, detailIndex - 1);
                String details = withoutCmd.substring(detailIndex);
                System.out.println("Got it. I've added this task:");
                Deadline targetDeadline = new Deadline(taskDesc, details);
                taskList.add(targetDeadline);
                System.out.println(targetDeadline);
                System.out.println("Now you have " + taskList.size() +  " tasks in the list.");
            } else if (currentCommand.startsWith("event ")) {
                try {
                    if (currentCommand.length() <= 6) {
                        throw new StringIndexOutOfBoundsException("Task too short");
                    }
                } catch (StringIndexOutOfBoundsException e) {
                    System.out.println("Error: Event cannot be empty!");
                    continue;
                }
                String withoutCmd = currentCommand.substring(6);
                int detailIndex = withoutCmd.indexOf("/from ");
                if (detailIndex == -1) {
                    System.out.println("Error: Missing '/from' in command");
                    continue;
                }
                if (detailIndex == 6) {
                    System.out.println("Error: Missing Actual task");
                    continue;
                }
                if (withoutCmd.indexOf("/to ") == -1) {
                    System.out.println("Error: Missing '/to' in command");
                    continue;
                }
                String taskDesc = withoutCmd.substring(0, detailIndex - 1);
                String details = withoutCmd.substring(detailIndex);
                System.out.println("Got it. I've added this task:");
                Event targetEvent= new Event(taskDesc, details);
                taskList.add(targetEvent);
                System.out.println(targetEvent);
                System.out.println("Now you have " + taskList.size() +  " tasks in the list.");
            } else {
                System.out.println("Sorry! Your command is not recognised! ");
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
            selectedTask.switchDone();
        }
        tasks.set(index, selectedTask);
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(selectedTask);
    }

    public static void unmark(int index, ArrayList<Task> tasks) {
        Task selectedTask = tasks.get(index);
        if (selectedTask.getDone()) {
            selectedTask.switchDone();
        }
        tasks.set(index, selectedTask);
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println(selectedTask);
    }

}
