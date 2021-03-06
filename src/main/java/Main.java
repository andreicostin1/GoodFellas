package main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.Objects;

public class Main extends Application {
  public static Stage primaryStage;

  @Override
  public void start(Stage primaryStage) {
    Main.primaryStage = primaryStage;

    try {
      URL url = getClass().getResource("/main/resources/view/SplashScreen.fxml");
      Pane root = FXMLLoader.load(Objects.requireNonNull(url));
      Scene scene = new Scene(root, 700, 475);

      primaryStage.setResizable(false);
      primaryStage.initStyle(StageStyle.UNDECORATED);
      primaryStage.setScene(scene);
      primaryStage.setTitle("Splash Screen");
      primaryStage.show();
    } catch (Exception e) {
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
