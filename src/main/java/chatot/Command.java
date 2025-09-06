package chatot;

/**
 * Enumeration of all supported command types in the Chatot application.
 *
 * This enum defines the complete set of commands that users can execute
 * to interact with the task management system. Each command type corresponds
 * to a specific action that can be performed on tasks or the application state.
 *
 * Command categories:
 * - Task creation: TODO, DEADLINE, EVENT
 * - Task management: MARK, UNMARK, DELETE, UPDATE
 * - Task querying: LIST, FIND
 * - Application control: BYE
 * - Update operations: CANCELUPDATE
 * - Error handling: UNKNOWN
 */
enum CommandType {
    BYE,
    LIST,
    MARK,
    UNMARK,
    TODO,
    DEADLINE,
    EVENT,
    DELETE,
    UNKNOWN,
    FIND,
    UPDATE,
    CANCEL_UPDATE
}

/**
 * Represents a parsed user command with its type and associated arguments.
 *
 * This class encapsulates the result of parsing user input, containing both
 * the command type (what action to perform) and any additional arguments
 * needed to execute that command. It provides an immutable representation
 * of user intent that can be safely passed around the application.
 *
 * Examples:
 * - "list" → Command(LIST, "")
 * - "todo read book" → Command(TODO, "read book")
 */
class Command {
    private final CommandType type;
    private final String arguments;

    /**
     * Creates a command with the specified type and no arguments.
     *
     * @param type the command type to execute
     */
    public Command(CommandType type) {
        this.type = type;
        this.arguments = "";
    }

    /**
     * Creates a command with the specified type and arguments.
     *
     * @param type the command type to execute
     * @param arguments additional parameters needed for command execution
     */
    public Command(CommandType type, String arguments) {
        this.type = type;
        this.arguments = arguments;
    }

    /**
     * Gets the type of this command.
     *
     * @return the command type indicating what action should be performed
     */
    public CommandType getType() {
        return type;
    }

    /**
     * Gets the arguments associated with this command.
     *
     * @return the command arguments, or empty string if no arguments were provided
     */
    public String getArguments() {
        return arguments;
    }
}