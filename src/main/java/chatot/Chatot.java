package chatot;

class Chatot {
    private chatot.Storage storage;
    private chatot.TaskList tasks;
    private chatot.Ui ui;
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

    private String handleTodoCommand(Command command) {
        try {
            String arguments = command.getArguments();
            if (arguments.isEmpty()) {
                throw new StringIndexOutOfBoundsException("Task too short");
            }
            chatot.Todo targetTodo = new chatot.Todo(arguments);
            tasks.addTask(targetTodo);
            return ui.showTaskAdded(targetTodo, tasks.getSize());
        } catch (StringIndexOutOfBoundsException e) {
            return ui.showError(e);
        }
    }

    private String handleDeadlineCommand(Command command) {
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
            tasks.addTask(targetDeadline);
            return ui.showTaskAdded(targetDeadline, tasks.getSize());
        } catch (IllegalArgumentException e) {
            return ui.showErrorMessage(e.getMessage());
        }
    }

    private String handleEventCommand(Command command) {
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


    public String run() {
        return ui.showWelcome();
    }

    public String run(String userInput) {
        Command command = Parser.parse(userInput);
        return executeCommand(command);
    }

    private String executeCommand(Command command) {
        switch (command.getType()) {
            case BYE: return handleByeCommand();
            case LIST: return handleListCommand();
            case MARK: return handleMarkCommand(command);
            case UNMARK: return handleUnmarkCommand(command);
            case TODO: return handleTodoCommand(command);
            case DEADLINE: return handleDeadlineCommand(command);
            case EVENT: return handleEventCommand(command);
            case DELETE: return handleDeleteCommand(command);
            case FIND: return handleFindCommand(command);
            default: return ui.showCommandNotRecognised();
        }
    }


}



