public class Chatot {
    private final String userCommand;

    public Chatot() {
        this.userCommand = "";
    }

    public Chatot(String inputCommand) {
        this.userCommand = inputCommand;
    }

    public static void main(String[] args) {
        String logo = "  ____ |  |__ _____ _/  |_  _____/  |_ \n"
                + "_/ ___\\|  |  \\\\__  \\\\   __\\/  _ \\   __\\\n"
                + "\\  \\___|   Y  \\/ __ \\|  | (  <_> )  |  \n"
                + " \\___  >___|  (____  /__|  \\____/|__|  \n"
                + "     \\/     \\/     \\/                  \n";
        System.out.println("Hello from\n" + logo);

        if (userCommand.isEmpty()) {
            greet();
        }

        exit();
    }

    public static void greet() {
        System.out.println("Hello I'm Chatot!");
        System.out.println("What can I do for you?");
    }

    public static void exit() {
        System.out.println("Bye. Hope to see you again soon!");
    }
//
//    public static void echo(String inputCommand) {
//        System.out.println(inputCommand);
//    }
}
