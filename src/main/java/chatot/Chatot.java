package chatot;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;


public class Chatot extends Application {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;
    private static final String DEFAULT_FILE_PATH = "chatot/example.txt";

    private ScrollPane scrollPane;
    private VBox dialogContainer;
    private TextField userInput;
    private Button sendButton;
    private Scene scene;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/user.png"));
    private Image botImage = new Image(this.getClass().getResourceAsStream("/images/chatot.png"));

    public Chatot() {
        this(DEFAULT_FILE_PATH);
    }

    @Override
    public void start(Stage stage) {
        scrollPane = new ScrollPane();
        dialogContainer = new VBox();
        scrollPane.setContent(dialogContainer);

        userInput = new TextField();
        sendButton = new Button("Send");

        DialogBox dialogBox = new DialogBox("Hello!", userImage);
        dialogContainer.getChildren().addAll(dialogBox);

        AnchorPane mainLayout = new AnchorPane();
        mainLayout.getChildren().addAll(scrollPane, userInput, sendButton);

        scene = new Scene(mainLayout);

        stage.setScene(scene);
        stage.show();
        stage.setTitle("Duke");
        stage.setResizable(false);
        stage.setMinHeight(600.0);
        stage.setMinWidth(400.0);

        mainLayout.setPrefSize(400.0, 600.0);

        scrollPane.setPrefSize(385, 535);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        scrollPane.setVvalue(1.0);
        scrollPane.setFitToWidth(true);

        dialogContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);

        userInput.setPrefWidth(325.0);

        sendButton.setPrefWidth(55.0);

        AnchorPane.setTopAnchor(scrollPane, 1.0);

        AnchorPane.setBottomAnchor(sendButton, 1.0);
        AnchorPane.setRightAnchor(sendButton, 1.0);

        AnchorPane.setLeftAnchor(userInput, 1.0);
        AnchorPane.setBottomAnchor(userInput, 1.0);
    }

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
                        if (tasks.getSize() == 0) {
                            throw new IllegalStateException("No tasks available");
                        }
                        ui.showTaskList(tasks);
                    } catch (IllegalStateException e) {
                        ui.showError(e);
                    }
                    break;

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
                        ui.showTaskMarked(tasks.get(index - 1));
                    } catch (IllegalStateException e) {
                        ui.showError(e);
                    }
                    break;

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
                        ui.showTaskAdded(targetTodo, tasks.getSize());
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
                        ui.showTaskAdded(targetDeadline, tasks.getSize());
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
                        ui.showTaskAdded(targetEvent, tasks.getSize());
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
                        if (selectedIndex > tasks.getSize()) {
                            throw new IllegalArgumentException("Selected index exceeds list length");
                        }

                        Task removedTask = tasks.deleteTask(selectedIndex - 1);
                        ui.showTaskRemoved(removedTask, tasks.getSize());
                    } catch (Exception e) {
                        ui.showError(e);
                    }
                    break;

                case FIND:
                    try {
                        String arguments = command.getArguments();
                        if (arguments.isEmpty()) {
                            throw new IllegalArgumentException("No index selected");
                        }
                        TaskList filteredTasks = tasks.findTask(arguments);
                        if (tasks.getSize() == 0) {
                            throw new IllegalStateException("No tasks match your keyword");
                        }
                        ui.showTaskList(filteredTasks);
                    } catch (IllegalStateException e) {
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