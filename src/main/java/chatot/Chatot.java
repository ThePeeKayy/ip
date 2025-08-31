package chatot;

class Chatot {
    private chatot.Storage storage;
    private chatot.TaskList tasks;
    private chatot.Ui ui;
    private int updateTargetIndex = -1;
    private static final int INDEX_OFFSET = 1;
    private static final String DEFAULT_FILE_PATH = "./data/taskHistory.txt";


    public Chatot() {
        ui = new chatot.Ui();
        storage = new chatot.Storage(DEFAULT_FILE_PATH);
        try {
            tasks = new chatot.TaskList(storage.load());
        } catch (Exception e) {
            System.out.println("Creating new chat history...");
            tasks = new chatot.TaskList();
        }
    }

    private String handleByeCommand() {
        updateTargetIndex = -1;
        storage.save(tasks.getTasks());
        return ui.showGoodbye();
    }

    private String handleListCommand() {
        try {
            if (tasks.getSize() == 0) {
                throw new IllegalStateException("No tasks available");
            }
            return ui.showTaskList(tasks);
        } catch (IllegalStateException e) {
            return ui.showError(e);
        }
    }

    private String handleMarkCommand(Command command) {
        try {
            if (tasks.getSize() == 0) {
                throw new IllegalStateException("No tasks available to remove");
            }
            int index = Integer.parseInt(command.getArguments());
            if (tasks.getSize() < index) {
                throw new IllegalStateException("Index out of range");
            }
            tasks.markTask(index - INDEX_OFFSET);
            return ui.showTaskMarked(tasks.get(index - INDEX_OFFSET));
        } catch (IllegalStateException e) {
            return ui.showError(e);
        }
    }

    private String handleUnmarkCommand(Command command) {
        try {
            if (tasks.getSize() == 0) {
                throw new IllegalStateException("No tasks available to remove");
            }
            int index = Integer.parseInt(command.getArguments());
            if (tasks.getSize() < index) {
                throw new IllegalStateException("Index out of range");
            }
            tasks.unmarkTask(index - INDEX_OFFSET);
            return ui.showTaskUnmarked(tasks.get(index - INDEX_OFFSET));
        } catch (IllegalStateException e) {
            return ui.showError(e);
        }
    }

    private String handleTodoCommand(Command command, boolean isUpdating) {
        try {
            String arguments = command.getArguments();
            if (arguments.isEmpty()) {
                throw new StringIndexOutOfBoundsException("Task too short");
            }
            chatot.Todo targetTodo = new chatot.Todo(arguments);
            if (isUpdating) {
                tasks.updateTask(updateTargetIndex, targetTodo);
                updateTargetIndex = -1;
                return ui.showTaskUpdated(targetTodo);
            }
            tasks.addTask(targetTodo);
            return ui.showTaskAdded(targetTodo, tasks.getSize());
        } catch (StringIndexOutOfBoundsException e) {
            return ui.showError(e);
        }
    }

    private String handleDeadlineCommand(Command command, boolean isUpdating) {
        try {
            String arguments = command.getArguments();
            if (arguments.isEmpty()) {
                throw new IllegalArgumentException("Deadline cannot be empty!");
            }

            int detailIndex = arguments.indexOf("/by ");

            if (detailIndex == -1) {
                throw new IllegalArgumentException("Missing '/by' in command");
            }

            if (detailIndex == 0) {
                throw new IllegalArgumentException("Missing actual task");
            }

            String taskDesc = arguments.substring(0, detailIndex).trim();
            String details = arguments.substring(detailIndex).trim();

            if (taskDesc.isEmpty()) {
                throw new IllegalArgumentException("Task description cannot be empty");
            }
            if (details.isEmpty()) {
                throw new IllegalArgumentException("Deadline details cannot be empty");
            }

            chatot.Deadline targetDeadline = new chatot.Deadline(taskDesc, details);
            if (isUpdating) {
                tasks.updateTask(updateTargetIndex, targetDeadline);
                updateTargetIndex = -1;
                return ui.showTaskUpdated(targetDeadline);
            }
            tasks.addTask(targetDeadline);
            return ui.showTaskAdded(targetDeadline, tasks.getSize());
        } catch (IllegalArgumentException e) {
            return ui.showErrorMessage(e.getMessage());
        }
    }

    private String handleEventCommand(Command command, boolean isUpdating) {
        try {
            String arguments = command.getArguments();
            if (arguments.isEmpty()) {
                throw new StringIndexOutOfBoundsException("Task too short");
            }

            int detailIndex = arguments.indexOf("/from ");
            if (detailIndex == -1) {
                throw new IllegalArgumentException("Event needs /from");
            }
            if (detailIndex == 0) {
                throw new IllegalArgumentException("Actual task missing");
            }
            if (arguments.indexOf("/to ") == -1) {
                throw new IllegalArgumentException("Event needs /to");
            }

            String taskDesc = arguments.substring(0, detailIndex - INDEX_OFFSET);
            String details = arguments.substring(detailIndex);

            chatot.Event targetEvent = new chatot.Event(taskDesc, details);
            if (isUpdating) {
                tasks.updateTask(updateTargetIndex, targetEvent);
                updateTargetIndex = -1;
                return ui.showTaskUpdated(targetEvent);
            }
            tasks.addTask(targetEvent);
            return ui.showTaskAdded(targetEvent, tasks.getSize());
        } catch (StringIndexOutOfBoundsException e) {
            return ui.showErrorMessage("Event cannot be empty!");
        } catch (IllegalArgumentException e) {
            return ui.showErrorMessage(e.getMessage());
        }
    }

    private String handleDeleteCommand(Command command) {
        try {
            String arguments = command.getArguments();
            if (arguments.isEmpty()) {
                throw new IllegalArgumentException("No index selected");
            }
            int selectedIndex = Integer.parseInt(arguments);
            if (selectedIndex > tasks.getSize()) {
                throw new IllegalArgumentException("Selected index exceeds list length");
            }

            chatot.Task removedTask = tasks.deleteTask(selectedIndex - INDEX_OFFSET);
            return ui.showTaskRemoved(removedTask, tasks.getSize());
        } catch (Exception e) {
            return ui.showError(e);
        }
    }

    private String handleFindCommand(Command command) {
        try {
            String arguments = command.getArguments();
            if (arguments.isEmpty()) {
                throw new IllegalArgumentException("No index selected");
            }
            chatot.TaskList filteredTasks = tasks.findTask(arguments);
            if (tasks.getSize() == 0) {
                throw new IllegalStateException("No tasks match the keyword");
            }
            return ui.showTaskList(filteredTasks);
        } catch (IllegalStateException e) {
            return ui.showError(e);
        }
    }

    private String handleUpdateCommand(Command command) {
        try {
            String arguments = command.getArguments();
            if (arguments.isEmpty()) {
                throw new IllegalArgumentException("No index selected");
            }
            int targetIndex = Integer.parseInt(arguments);

            if (targetIndex < 1 || targetIndex > tasks.getSize()) {
                throw new IllegalArgumentException("Index out of range");
            }

            int inputIndex= targetIndex - INDEX_OFFSET;
            updateTargetIndex = Math.max(inputIndex, 0);
            return  "Please enter the new task (todo/deadline/event):\n" + "Please enter cancel to exit updating";

        } catch (Exception e) {
            return ui.showError(e);
        }
    }

    private String handleCancelUpdateCommand() {
        try {
            updateTargetIndex = -1;
            return  "Updating cancelled. Please enter a new command.";
        } catch (Exception e) {
            return ui.showError(e);
        }
    }

    private String handleUpdateReplacement(String userInput) {
        try {
            Command command = Parser.parse(userInput);
            boolean isUpdating = true;
            switch (command.getType()) {
                case TODO: return handleTodoCommand(command, isUpdating);
                case DEADLINE: return handleDeadlineCommand(command, isUpdating);
                case EVENT: return handleEventCommand(command, isUpdating);
                case CANCELUPDATE: return handleCancelUpdateCommand();
                case BYE: return handleByeCommand();
                default:
                    return "Please enter only todo, deadline, or event commands\n" + "Type cancel to cancel updating";
            }

        } catch (Exception e) {
            updateTargetIndex = -1;
            return ui.showError(e);
        }
    }

    public String run() {
        return ui.showWelcome();
    }

    public String run(String userInput) {
        assert userInput != null : "User input should not be null";

        if (updateTargetIndex != -1) {
            return handleUpdateReplacement(userInput);
        }

        Command command = Parser.parse(userInput);
        return executeCommand(command);
    }

    private String executeCommand(Command command) {
        boolean isUpdating = false;
        switch (command.getType()) {
            case BYE: return handleByeCommand();
            case LIST: return handleListCommand();
            case MARK: return handleMarkCommand(command);
            case UNMARK: return handleUnmarkCommand(command);
            case TODO: return handleTodoCommand(command, isUpdating);
            case DEADLINE: return handleDeadlineCommand(command, isUpdating);
            case EVENT: return handleEventCommand(command, isUpdating);
            case DELETE: return handleDeleteCommand(command);
            case FIND: return handleFindCommand(command);
            case UPDATE: return handleUpdateCommand(command);
            default: return ui.showCommandNotRecognised();
        }
    }
}