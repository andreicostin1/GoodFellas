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

    @FXML Button addLeft = new Button();
    @FXML Button addRight = new Button();
    @FXML ChoiceBox leftCharacterMenu = new ChoiceBox();
    @FXML ChoiceBox rightCharacterMenu = new ChoiceBox();
    @FXML HBox display = new HBox();

    ArrayList<Character> poseList = new ArrayList<>();
    String sourceRootPath = "resources/characters/";


    public class MyEventHandler implements EventHandler<Event> {

        public void handle(Event event){
            System.out.println("Event happened");
        }
    }


    public void initialize(){
        createPoseList();

        for (Character pose : poseList) {
            leftCharacterMenu.getItems().add(pose.getName());
        }
        for (Character pose : poseList) {
            rightCharacterMenu.getItems().add(pose.getName());
        }


        System.out.println("INITIALISED");
    }


    //Creates a list of Character objects for each image
    public void createPoseList(){
        File poseFile = new File("src/resources/characters");
        List<File> files = Arrays.asList(poseFile.listFiles());

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

    public void addPoseLeft(){
        System.out.println("Here");

        addLeft.setOnAction(actionEvent -> {
            System.out.println("You chose: " + leftCharacterMenu.getValue());

            Character character = findCharacter(leftCharacterMenu.getValue().toString());

            character.getImage().setFitHeight(100);
            character.getImage().setFitWidth(100);
            character.getImage().setTranslateY(98);

            display.getChildren().add(character.getImage());
        });
    }

    public void addPoseRight(){
        System.out.println("Here");

        addLeft.setOnAction(actionEvent -> {
            System.out.println("You chose: " + rightCharacterMenu.getValue());

            Character character = findCharacter(rightCharacterMenu.getValue().toString());

            character.getImage().setFitHeight(100);
            character.getImage().setFitWidth(100);
            character.getImage().setTranslateY(98);

            display.getChildren().add(character.getImage());
        });
    }


    public Character findCharacter(String name){

        Character character = null;

        for (Character c : poseList) {
            if(name.equals(c.getName())) {
                character = c;
            }
        }

        return character;
    }
}
