public class Chatot {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    public Chatot(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (Exception e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    public void run() {
        ui.showWelcome();

        while (true) {
            String currentCommand = ui.readCommand();
            Command command = Parser.parse(currentCommand);

            switch (command.getType()) {
                case BYE:
                    storage.save(tasks.getTasks());
                    ui.showGoodbye();
                    return;

                case LIST:
                    try {
                        if (tasks.size() == 0) {
                            throw new IllegalStateException("No tasks available to remove");
                        }
                        ui.showTaskList(tasks);
                    } catch (IllegalStateException e) {
                        ui.showError(e);
                    }
                    break;

                case MARK:
                    try {
                        if (tasks.size() == 0) {
                            throw new IllegalStateException("No tasks available to remove");
                        }
                        int index = Integer.parseInt(command.getArguments());
                        if (tasks.size() < index) {
                            throw new IllegalStateException("Index out of range");
                        }
                        tasks.markTask(index - 1);
                        ui.showTaskMarked(tasks.get(index - 1));
                    } catch (IllegalStateException e) {
                        ui.showError(e);
                    }
                    break;

                case UNMARK:
                    try {
                        if (tasks.size() == 0) {
                            throw new IllegalStateException("No tasks available to remove");
                        }
                        int index = Integer.parseInt(command.getArguments());
                        if (tasks.size() < index) {
                            throw new IllegalStateException("Index out of range");
                        }
                        tasks.unmarkTask(index - 1);
                        ui.showTaskUnmarked(tasks.get(index - 1));
                    } catch (IllegalStateException e) {
                        ui.showError(e);
                    }
                    break;

                case TODO:
                    try {
                        String arguments = command.getArguments();
                        if (arguments.isEmpty()) {
                            throw new StringIndexOutOfBoundsException("Task too short");
                        }
                        Todo targetTodo = new Todo(arguments);
                        tasks.addTask(targetTodo);
                        ui.showTaskAdded(targetTodo, tasks.size());
                    } catch (StringIndexOutOfBoundsException e) {
                        ui.showError(e);
                    }
                    break;

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

                        Deadline targetDeadline = new Deadline(taskDesc, details);
                        tasks.addTask(targetDeadline);
                        ui.showTaskAdded(targetDeadline, tasks.size());
                    } catch (IllegalArgumentException e) {
                        ui.showErrorMessage(e.getMessage());
                    }
                    break;

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

                        Event targetEvent = new Event(taskDesc, details);
                        tasks.addTask(targetEvent);
                        ui.showTaskAdded(targetEvent, tasks.size());
                    } catch (StringIndexOutOfBoundsException e) {
                        ui.showErrorMessage("Event cannot be empty!");
                    } catch (IllegalArgumentException e) {
                        ui.showErrorMessage(e.getMessage());
                    }
                    break;

                case DELETE:
                    try {
                        String arguments = command.getArguments();
                        if (arguments.isEmpty()) {
                            throw new IllegalArgumentException("No index selected");
                        }
                        int selectedIndex = Integer.parseInt(arguments);
                        if (selectedIndex > tasks.size()) {
                            throw new IllegalArgumentException("Selected index exceeds list length");
                        }

                        Task removedTask = tasks.deleteTask(selectedIndex - 1);
                        ui.showTaskRemoved(removedTask, tasks.size());
                    } catch (Exception e) {
                        ui.showError(e);
                    }
                    break;

                case UNKNOWN:
                default:
                    ui.showCommandNotRecognised();
                    break;
            }
        }
    }

    public static void main(String[] args) {
        new Chatot("./data/taskHistory.txt").run();
    }
}