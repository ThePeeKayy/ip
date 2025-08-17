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
        public Task(String description, boolean isDoneState) {
            this.description = description;
            this.isDone = !isDoneState;
        }

        public String getDescription() {
            return description;
        }

        public boolean getDone() {
            return isDone;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<String> commandList = new ArrayList<>();
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
                printList(commandList);
            } else {
                commandList.add(currentCommand);
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

    public static void echo(String inputCommand) {
        System.out.println("added: " + inputCommand);
    }

    public static void printList(ArrayList<String> commandList) {
        for (int i = 0; i < commandList.size(); i++) {
            System.out.println((i+1) + ". " + commandList.get(i));
        }
    }

}
