package chatot;

public class Parser {

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

class Command {
    private CommandType type;
    private String arguments;

    public Command(CommandType type) {
        this.type = type;
        this.arguments = "";
    }

    public Command(CommandType type, String arguments) {
        this.type = type;
        this.arguments = arguments;
    }

    public CommandType getType() {
        return type;
    }

    public String getArguments() {
        return arguments;
    }
}