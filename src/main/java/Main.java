package main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.Scanner;


public class Main extends Application
{
    @Override
    public void start(Stage primaryStage)
    {
        try {
            URL url = getClass().getResource("/main/resources/view/GUI.fxml");
            Pane root = FXMLLoader.load(url);
            Scene scene = new Scene(root, 700, 400);

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