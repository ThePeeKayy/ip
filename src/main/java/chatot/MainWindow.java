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

    private chatot.Chatot chatotInstance;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/user.jpg"));
    private Image botImage = new Image(this.getClass().getResourceAsStream("/images/chatot.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        chatotInstance = new chatot.Chatot();
        dialogContainer.getChildren().addAll(
                DialogBox.getChatotDialog(chatotInstance.run(), botImage)
        );
    }

    public void setChatot(chatot.Chatot c) {
        chatotInstance = c;
    }

    @FXML
    private void handleUserInput() {
        String userText = userInput.getText();
        String chatotText = chatotInstance.run(userInput.getText());
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(userText, userImage),
                DialogBox.getChatotDialog(chatotText, botImage)
        );
        userInput.clear();
    }

}
