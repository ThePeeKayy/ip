package chatot;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import java.io.IOException;
import java.util.Collections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

/**
 * Custom JavaFX component representing a chat dialog box.
 * Displays messages in a chat interface with text content and profile images.
 * Supports different visual layouts for user messages and bot responses.
 */
public class DialogBox extends HBox {

    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image img) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
        displayPicture.setImage(img);
    }

    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
        dialog.getStyleClass().add("reply-label");
    }

    /**
     * Creates a dialog box representing a user message.
     * The dialog box displays with standard left-aligned formatting showing
     * the user's input text alongside their profile image.
     *
     * @param s the text content of the user's message
     * @param i the image to display as the user's profile picture
     * @return a new DialogBox configured for user messages
     */
    public static DialogBox getUserDialog(String s, Image i) {
        return new DialogBox(s, i);
    }

    /**
     * Creates a dialog box representing a Chatot response message.
     * The dialog box is automatically flipped to right-align the content,
     * applies appropriate styling, and displays Chatot's response text
     * alongside the bot's image.
     *
     * @param s the text content of Chatot's response
     * @param i the image to display as Chatot's profile picture
     * @return a new DialogBox configured and styled for Chatot messages
     */
    public static DialogBox getChatotDialog(String s, Image i) {
        var db = new DialogBox(s, i);
        db.flip();
        return db;
    }
}
