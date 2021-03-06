package main.java;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SplashScreenController implements Initializable {

  @FXML private Pane splashPane;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    new SplashScreen().start();
  }

  class SplashScreen extends Thread {

    @Override
    public void run() {
      try {
        Thread.sleep(2000);

        Platform.runLater(
            new Runnable() {
              @Override
              public void run() {
                Pane root = null;
                URL url = getClass().getResource("/main/resources/view/GUI.fxml");

                try {
                  root = FXMLLoader.load(url);
                } catch (IOException e) {

                }

                Scene scene = new Scene(root);
                Stage stage = new Stage();

                stage.setScene(scene);
                stage.setTitle("Excelsior Comic Maker");
                stage.setResizable(false);
                stage.show();

                splashPane.getScene().getWindow().hide();
              }
            });

      } catch (Exception e) {
      }
    }
  }
}
