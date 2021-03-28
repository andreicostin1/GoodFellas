import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controller {

    @FXML
    Button addLeft = new Button();
    @FXML
    Button addRight = new Button();
    @FXML
    Button clearPane = new Button();
    @FXML
    ChoiceBox leftCharacterMenu = new ChoiceBox();
    @FXML
    ChoiceBox rightCharacterMenu = new ChoiceBox();
    @FXML
    GridPane display = new GridPane();

    ArrayList<Character> poseList = new ArrayList<>();
    String sourceRootPath = "resources/characters/";

    Character left = null;
    Character right = null;

    EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            if (e.getSource().equals(addLeft)) {
                addPoseLeft();
            } else if (e.getSource().equals(addRight)) {
                addPoseRight();
            }
        }
    };

    public void initialize() {
        createPoseList();
        for (Character pose : poseList) {
            leftCharacterMenu.getItems().add(pose.getName());
        }
        for (Character pose : poseList) {
            rightCharacterMenu.getItems().add(pose.getName());
        }

        addLeft.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        addRight.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    }

    //Creates a list of Character objects for each image
    public void createPoseList() {
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

    public void addPoseLeft() {
        Character character = findCharacter(leftCharacterMenu.getValue().toString());

        character.getImage().setFitHeight(100);
        character.getImage().setFitWidth(100);

        left = character;
        display.add(character.getImage(), 0, 1);
    }

    public void addPoseRight() {
        Character character = findCharacter(rightCharacterMenu.getValue().toString());

        character.getImage().setFitHeight(100);
        character.getImage().setFitWidth(100);

        right = character;
        display.add(character.getImage(), 1, 1);
    }

    public void clearPane() {
        display.getChildren().clear();
        left = null;
        right = null;
    }

    public Character findCharacter(String name) {
        Character character = null;

        for (Character c : poseList) {
            if (name.equals(c.getName())) {
                character = c;
            }
        }

        return character;
    }

    public void flipLeft() {
        display.getChildren().remove(left.getImage());
        if (left != null) {
            if (left.getImage().getScaleX() == -1) {
                left.getImage().setScaleX(1);
            } else {
                left.getImage().setScaleX(-1);
            }
            display.add(left.getImage(), 0, 1);
        }
    }

    public void flipRight() {
        display.getChildren().remove(right.getImage());
        if (right != null) {
            if (right.getImage().getScaleX() == -1) {
                right.getImage().setScaleX(1);
            } else {
                right.getImage().setScaleX(-1);
            }
            display.add(right.getImage(), 1, 1);
        }
    }
}
