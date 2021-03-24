import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;


public class Main extends Application
{
    @Override
    public void start(Stage primaryStage)
    {
        try {

            URL url = getClass().getResource("GUI.fxml");

            Pane root = FXMLLoader.load(url);
            Scene scene = new Scene(root, 1600, 900);

            primaryStage.setScene(scene);
            primaryStage.setTitle("My App");
            primaryStage.show();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}