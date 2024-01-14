import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class cliente extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file for the GUI
        FXMLLoader loader = new FXMLLoader(getClass().getResource("cliente.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Cliente");

        // Set the width and height for your desired window size
        primaryStage.setWidth(800);  // Set the width as needed
        primaryStage.setHeight(600); // Set the height as needed

        // Center the window on the screen
        primaryStage.centerOnScreen();

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}