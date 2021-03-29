package main.java;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

public class Controller {

    @FXML
    Button addLeft = new Button();
    @FXML
    Button addRight = new Button();
    @FXML
    Button clearPane = new Button();
    @FXML
    ComboBox leftCharacterMenu = new ComboBox();
    @FXML
    ComboBox rightCharacterMenu = new ComboBox();
    @FXML
    GridPane display = new GridPane();

    ArrayList<Character> poseList = new ArrayList<>();

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
        try {
            createPoseList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Character pose : poseList) {
            leftCharacterMenu.getItems().add(pose.getName());
        }
        for (Character pose : poseList) {
            rightCharacterMenu.getItems().add(pose.getName());
        }

        addLeft.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        addRight.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    }

    //Creates a list of main.java.Character objects for each image
    public void createPoseList() throws URISyntaxException, IOException {
        URI uri = getClass().getResource("/main/resources/characters/").toURI();
        Path myPath;
        if (uri.getScheme().equals("jar")) {
            FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
            myPath = fileSystem.getPath("/main/resources/characters/");
        } else {
            myPath = Paths.get(uri);
        }
        Stream<Path> walk = Files.walk(myPath, 1);

        for (Iterator<Path> it = walk.iterator(); it.hasNext(); ) {
            Path inLoop = it.next();
            ImageView poseImage = new ImageView(inLoop.toUri().toURL().toString());
            String name = inLoop.getFileName().toString();
            if (!name.equals("characters")) {
                //Removes extension from file name
                if (name.indexOf(".") > 0) {
                    name = name.substring(0, name.lastIndexOf("."));
                }
                poseList.add(new Character(name, poseImage));
            }
        }
        Collections.sort(poseList, new Comparator<Character>() {
            public int compare(Character o1, Character o2) {
                return o1.name.compareTo(o2.name);
            }
        });
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
        if (left != null) {
            left.getImage().setScaleX(1);
        }
        if (right != null) {
            right.getImage().setScaleX(1);
        }
        left = null;
        right = null;
    }

    public Character findCharacter(String name) {
        Character character = new Character();
        for (Character c : poseList) {
            if (name.equals(c.getName())) {
                character.setName(c.getName());
                character.setImage(c.getImage());
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
