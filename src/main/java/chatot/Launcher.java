package chatot;

import javafx.application.Application;

/**
 * Entry point for the Chatot application.
 *
 * This class serves as the main launcher that initializes and starts the JavaFX application.
 * It delegates the actual application startup to the Main class while providing a clean
 * entry point that follows JavaFX best practices for application launching.
 *
 * The launcher pattern is used to avoid potential issues with module path configurations
 * and ensures proper JavaFX runtime initialization.
 *
 * @author Tan Peng Kiang
 */
public class Launcher {
    /**
     * Main method that launches the Chatot JavaFX application.
     *
     * @param args command line arguments passed to the application
     */
    public static void main(String[] args) {
        Application.launch(chatot.Main.class, args);
    }
}
