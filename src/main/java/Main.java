package main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;



public class Main extends Application
{
    @Override
    public void start(Stage primaryStage)
    {
        try {
            URL url = getClass().getResource("/main/resources/view/SplashScreen.fxml");
            Pane root = FXMLLoader.load(url);
            Scene scene = new Scene(root, 700, 475);

            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Comic Builder");
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