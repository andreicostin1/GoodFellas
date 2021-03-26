import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controller {

    @FXML Button characterLeft = new Button();
    @FXML Button characterRight = new Button();
    @FXML ChoiceBox leftCharacterMenu = new ChoiceBox();
    @FXML HBox display = new HBox();

    String sourceRootPath = "resources/characters/";


    public class MyEventHandler implements EventHandler<Event> {

        public void handle(Event event){
            System.out.println("Event happened");
        }
    }

    /*
    public void init(){
        leftCharacterMenu.getItems().add("Test");
        //leftCharacterMenu.getItems().addAll(allCharacters.toString());
    }
    */

    //Creates a list of Character objects for each image
    public void createPoseList(){
        File poseFile = new File("src/resources/characters");
        List<File> files = Arrays.asList(poseFile.listFiles());
        ArrayList<Character> poseList = new ArrayList<>();


        for (int i = 0; i < poseFile.list().length; i++) {

            ImageView poseImage = new ImageView(sourceRootPath.concat(files.get(i).getName()));
            String name = files.get(i).getName();

            //Removes extension from file name
            if (name.indexOf(".") > 0) {
                name = name.substring(0, name.lastIndexOf("."));
            }

            poseList.add(new Character(name, poseImage));
        }

    }

    public void leftSideImagePlacement(){

        createPoseList();

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
