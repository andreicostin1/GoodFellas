import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class Controller {

    @FXML Button characterLeft = new Button();
    @FXML Button characterRight = new Button();
    @FXML HBox display = new HBox();

    public class MyEventHandler implements EventHandler<Event> {

        public void handle(Event event){
            System.out.println("Event happened");
        }
    }



    public void leftSideImagePlacement(){
        ImageView neutralImage = new ImageView("resources/characters/neutral.png");
        neutralImage.setFitHeight(100);
        neutralImage.setFitWidth(100);
        neutralImage.setTranslateY(98);
        display.getChildren().addAll(neutralImage);
    }

    public void rightSideImagePlacement(){
        ImageView neutralImage = new ImageView("resources/characters/neutral.png");
        neutralImage.setFitHeight(100);
        neutralImage.setFitWidth(100);
        neutralImage.setTranslateY(98);
        display.getChildren().addAll(neutralImage);
    }
}
