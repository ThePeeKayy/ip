package chatot;

class Chatot {
    private chatot.Storage storage;
    private chatot.TaskList tasks;
    private chatot.Ui ui;
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

    public String run() {
        return ui.showWelcome();
    }

    public String run(String userInput) {

        chatot.Command command = Parser.parse(userInput);

        switch (command.getType()) {
                case BYE:
                    storage.save(tasks.getTasks());
                    return ui.showGoodbye();

                case LIST:
                    try {
                        if (tasks.getSize() == 0) {
                            throw new IllegalStateException("No tasks available");
                        }
                        return ui.showTaskList(tasks);
                    } catch (IllegalStateException e) {
                        return ui.showError(e);
                    }

                case MARK:
                    try {
                        if (tasks.getSize() == 0) {
                            throw new IllegalStateException("No tasks available to remove");
                        }
                        int index = Integer.parseInt(command.getArguments());
                        if (tasks.getSize() < index) {
                            throw new IllegalStateException("Index out of range");
                        }
                        tasks.markTask(index - 1);
                        return ui.showTaskMarked(tasks.get(index - 1));
                    } catch (IllegalStateException e) {
                        return ui.showError(e);
                    }

                case UNMARK:
                    try {
                        if (tasks.getSize() == 0) {
                            throw new IllegalStateException("No tasks available to remove");
                        }
                        int index = Integer.parseInt(command.getArguments());
                        if (tasks.getSize() < index) {
                            throw new IllegalStateException("Index out of range");
                        }
                        tasks.unmarkTask(index - 1);
                        return ui.showTaskUnmarked(tasks.get(index - 1));
                    } catch (IllegalStateException e) {
                        return ui.showError(e);
                    }

                case TODO:
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

                case DEADLINE:
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

                case EVENT:
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

                        String taskDesc = arguments.substring(0, detailIndex - 1);
                        String details = arguments.substring(detailIndex);

                        chatot.Event targetEvent = new chatot.Event(taskDesc, details);
                        tasks.addTask(targetEvent);
                        return ui.showTaskAdded(targetEvent, tasks.getSize());
                    } catch (StringIndexOutOfBoundsException e) {
                        return ui.showErrorMessage("Event cannot be empty!");
                    } catch (IllegalArgumentException e) {
                        return ui.showErrorMessage(e.getMessage());
                    }

                case DELETE:
                    try {
                        String arguments = command.getArguments();
                        if (arguments.isEmpty()) {
                            throw new IllegalArgumentException("No index selected");
                        }
                        int selectedIndex = Integer.parseInt(arguments);
                        if (selectedIndex > tasks.getSize()) {
                            throw new IllegalArgumentException("Selected index exceeds list length");
                        }

                        chatot.Task removedTask = tasks.deleteTask(selectedIndex - 1);
                        return ui.showTaskRemoved(removedTask, tasks.getSize());
                    } catch (Exception e) {
                        return ui.showError(e);
                    }

                case FIND:
                    try {
                        String arguments = command.getArguments();
                        if (arguments.isEmpty()) {
                            throw new IllegalArgumentException("No index selected");
                        }
                        chatot.TaskList filteredTasks = tasks.findTask(arguments);
                        if (tasks.getSize() == 0) {
                            throw new IllegalStateException("No tasks match your keyword");
                        }
                        return ui.showTaskList(filteredTasks);
                    } catch (IllegalStateException e) {
                        return ui.showError(e);
                    }

                case UNKNOWN:
                default:
                    return ui.showCommandNotRecognised();
        }
    }



}