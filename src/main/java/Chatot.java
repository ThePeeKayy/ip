import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.File;

public class Chatot {

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
        taskList = getStoredTasks();
        while (true) {
            String currentCommand = sc.nextLine();
            if (currentCommand.equals("bye")) {

                exit(taskList);
                break;
            } else if (currentCommand.equals("list")) {
                try {
                    if (taskList.size() == 0) {
                        throw new IllegalStateException("No tasks available to remove");
                    }
                    printList(taskList);
                } catch (IllegalStateException e) {
                    System.out.println(e);
                }
            } else if (currentCommand.startsWith("mark")) {
                try {
                    if (taskList.size() == 0) {
                        throw new IllegalStateException("No tasks available to remove");
                    }

                    int index = Integer.parseInt(currentCommand.split(" ")[1]);
                    if (taskList.size() < index) {
                        throw new IllegalStateException("Index out of range");
                    }
                    mark(index-1, taskList);
                } catch (IllegalStateException e){
                    System.out.println(e);
                }
            } else if (currentCommand.startsWith("unmark")) {
                try {
                    if (taskList.size() == 0) {
                        throw new IllegalStateException("No tasks available to remove");
                    }
                    int index = Integer.parseInt(currentCommand.split(" ")[1]);
                    if (taskList.size() < index) {
                        throw new IllegalStateException("Index out of range");
                    }
                    unmark(index-1, taskList);
                } catch (IllegalStateException e){
                    System.out.println(e);
                }
            } else if (currentCommand.startsWith("todo ")) {
                try {
                    if (currentCommand.length() <= 5) {
                        throw new StringIndexOutOfBoundsException("Task too short");
                    }
                    String taskDesc = currentCommand.substring(5);
                    System.out.println("Got it. I've added this task:");
                    Todo targetTodo = new Todo(taskDesc);
                    taskList.add(targetTodo);
                    System.out.println("Now you have " + taskList.size() +  " tasks in the list.");
                } catch (StringIndexOutOfBoundsException e) {
                    System.out.println(e);
                    continue;
                }

            } else if (currentCommand.startsWith("deadline ")) {
                try {
                    if (currentCommand.length() <= 9) {
                        throw new IllegalArgumentException("Deadline cannot be empty!");
                    }
                    String withoutCmd = currentCommand.substring(9);
                    int detailIndex = withoutCmd.indexOf("/by ");

                    if (detailIndex == -1) {
                        throw new IllegalArgumentException("Missing '/by' in command");
                    }

                    if (detailIndex == 0) {
                        throw new IllegalArgumentException("Missing actual task");
                    }

                    String taskDesc = withoutCmd.substring(0, detailIndex).trim();
                    String details = withoutCmd.substring(detailIndex).trim();

                    if (taskDesc.isEmpty()) {
                        throw new IllegalArgumentException("Task description cannot be empty");
                    }
                    if (details.isEmpty()) {
                        throw new IllegalArgumentException("Deadline details cannot be empty");
                    }
                    System.out.println("Got it. I've added this task:");
                    Deadline targetDeadline = new Deadline(taskDesc, details);
                    taskList.add(targetDeadline);
                    System.out.println(targetDeadline);
                    System.out.println("Now you have " + taskList.size() + " tasks in the list.");
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                    continue;
                }
            } else if (currentCommand.startsWith("event ")) {
                try {
                    if (currentCommand.length() <= 6) {
                        throw new StringIndexOutOfBoundsException("Task too short");
                    }
                    String withoutCmd = currentCommand.substring(6);
                    int detailIndex = withoutCmd.indexOf("/from ");
                    if (detailIndex == -1) {
                        throw new IllegalArgumentException("Event needs /from");
                    }
                    if (detailIndex == 6) {
                        throw new IllegalArgumentException("Actual task missing");
                    }
                    if (withoutCmd.indexOf("/to ") == -1) {
                        throw new IllegalArgumentException("Event needs /to");
                    }
                    String taskDesc = withoutCmd.substring(0, detailIndex - 1);
                    String details = withoutCmd.substring(detailIndex);
                    System.out.println("Got it. I've added this task:");
                    Event targetEvent= new Event(taskDesc, details);
                    taskList.add(targetEvent);
                    System.out.println(targetEvent);
                    System.out.println("Now you have " + taskList.size() +  " tasks in the list.");
                } catch (StringIndexOutOfBoundsException e) {
                    System.out.println("Error: Event cannot be empty!");
                    continue;
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                    continue;
                }

            } else if (currentCommand.startsWith("delete ")) {
                try {
                    if (currentCommand.length() == 7) {
                        throw new IllegalArgumentException("No index selected");
                    }
                    int selectedIndex = Integer.parseInt(currentCommand.substring(7));
                    if (selectedIndex > taskList.size()) {
                        throw new IllegalArgumentException("Selected index exceeds list length");
                    }

                    Task removedTask = taskList.remove(selectedIndex-1);
                    System.out.println("Noted. I've removed this task:\n" + removedTask + "\n" + "Now you have " + taskList.size() + " tasks in the list.\n");
                } catch (Exception e) {
                    System.out.println(e);
                    continue;
                }
            } else {
                System.out.println("Command not recognised! Check out our user guide!");
            }

        }


    }

    public static void greet() {
        System.out.println("Hello I'm Chatot!");
        System.out.println("What can I do for you?");
    }

    public static void exit(ArrayList<Task> currentTasks) {
        System.out.println("Bye. Hope to see you again soon!");
        File dataDir = new File("./data"); // for folder creation

        if (!dataDir.exists()) {
            dataDir.mkdirs(); // handle missing folder as required
        }

        try (FileWriter writer = new FileWriter("./data/taskHistory.txt")) {
            for (Task task : currentTasks) {
                writer.write(task.toString() + "\n");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
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

    public static ArrayList<Task> getStoredTasks() {
        try {
            File file = new File("./data/taskHistory.txt");
            Scanner scanner = new Scanner(file);
            ArrayList<Task> taskList = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                boolean isDone = (line.charAt(4) == 'X');
                switch (line.charAt(1)) {
                    case 'E':
                        int startIndex = line.indexOf("from: ");
                        taskList.add(new Event(line.substring(7, startIndex - 2), line.substring(startIndex - 1), isDone));
                        break;
                    case 'D':
                        int timeIndex = line.indexOf("by: ");
                        taskList.add(new Deadline(line.substring(7, timeIndex - 2), line.substring(timeIndex + 1, line.length() - 1), isDone));
                        break;
                    case 'T':
                        taskList.add(new Todo(line.substring(7), isDone));
                        break;
                }
            }
            scanner.close();

            return taskList;

        } catch (Exception e) {
            System.out.println("No previous data retrieved");
            return new ArrayList<>();
        }
    }

}
