package chatot;

enum CommandType {
    BYE, LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, UNKNOWN, FIND
}

public class Command {
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
