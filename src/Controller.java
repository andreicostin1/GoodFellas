import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class Controller {

    @FXML Button characterLeft = new Button();
    @FXML HBox display = new HBox();

    ImageView neutralImage = new ImageView("resources/characters/neutral.png");

    public class MyEventHandler implements EventHandler<Event> {

        public void handle(Event event){
            System.out.println("Event happened");
        }
    }



    public void imagePlacement(){
        display.getChildren().addAll(neutralImage);

        System.out.println("This worked");
    }
}
