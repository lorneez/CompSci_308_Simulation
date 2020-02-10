package cellsociety;

import cellsociety.Control.GameEngine;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.application.Application.launch;

/**
 * Feel free to completely change this code or delete it entirely. 
 */
public class Main extends Application {
    /**
     * Start of the program.
     */

    public static void main (String[] args) {
        launch(args);
    }

    public void start(Stage stage) throws IOException {
        System.out.println("Hello world");
        new GameEngine();
        System.out.println("End");
    }
}
