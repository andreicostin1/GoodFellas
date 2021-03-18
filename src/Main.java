import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;


public class Main extends Application
{

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage stage)
    {
        MenuB menuBar = new MenuB();
        EditStage editStage=new EditStage();
        VBox vbox = new VBox();
        stage.setTitle("My App");

        vbox.getChildren().add(menuBar);
        editStage.setMaxWidth(500);
        vbox.getChildren().add(editStage);

        Scene scene = new Scene(vbox,1600,900);
        stage.setScene(scene);
        stage.show();
    }

}