package chatot;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;

public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Chatot chatot;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/user.jpg"));
    private Image botImage = new Image(this.getClass().getResourceAsStream("/images/chatot.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        chatot = new Chatot();
        dialogContainer.getChildren().addAll(
                DialogBox.getChatotDialog(chatot.run(), botImage)
        );
    }

    public void setChatot(Chatot c) {
        chatot = c;
    }

    @FXML
    private void handleUserInput() {
        String userText = userInput.getText();
        String chatotText = chatot.run(userInput.getText());
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(userText, userImage),
                DialogBox.getChatotDialog(chatotText, botImage)
        );
        userInput.clear();
    }

}
