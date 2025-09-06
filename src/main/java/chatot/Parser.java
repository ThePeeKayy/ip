package chatot;

/**
 * Handles parsing of user inputs.
 */
class Parser {

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
            case "find":
                return new Command(CommandType.FIND, arguments);
            case "update":
                return new Command(CommandType.UPDATE, arguments);
            case "cancel":
                return new Command(CommandType.CANCELUPDATE, arguments);
            default:
                return new Command(CommandType.UNKNOWN);
        }
    }
}


