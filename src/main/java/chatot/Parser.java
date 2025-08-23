package chatot;

/**
 * Handles parsing of user inputs.
 */
public class Parser {

    /**
     * Comprehensive switch that handles all possible commands.
     * @param fullCommand the complete user input string
     * @return Command object with appropriate type and arguments
     */
    public static Command parse(String fullCommand) {
        String[] commandParts = fullCommand.split(" ", 2);
        String commandWord = commandParts[0];
        String arguments = commandParts.length > 1 ? commandParts[1] : "";

        switch (commandWord) {
            case "bye":
                return new Command(CommandType.BYE);
            case "list":
                return new Command(CommandType.LIST);
            case "mark":
                return new Command(CommandType.MARK, arguments);
            case "unmark":
                return new Command(CommandType.UNMARK, arguments);
            case "todo":
                return new Command(CommandType.TODO, arguments);
            case "deadline":
                return new Command(CommandType.DEADLINE, arguments);
            case "event":
                return new Command(CommandType.EVENT, arguments);
            case "delete":
                return new Command(CommandType.DELETE, arguments);
            default:
                return new Command(CommandType.UNKNOWN);
        }
    }
}

enum CommandType {
    BYE, LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, UNKNOWN
}

/**
 * Represents a singular user command.
 */
class Command {
    private CommandType type;
    private String arguments;
    /**
     * Creates a command with only type and no arguments.
     * @param type the command type
     */
    public Command(CommandType type) {
        this.type = type;
        this.arguments = "";
    }

    public Command(CommandType type, String arguments) {
        this.type = type;
        this.arguments = arguments;
    }
    /**
     * Simple getter for the command type.
     * @return the command type
     */
    public CommandType getType() {
        return type;
    }

    /**
     * Simple getter for the command arguments.
     * @return the command arguments
     */
    public String getArguments() {
        return arguments;
    }
}