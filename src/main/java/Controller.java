package main.java;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.io.IOException;
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
    Button leftGender = new Button();
    @FXML
    Button rightGender = new Button();
    @FXML
    ComboBox leftCharacterMenu = new ComboBox();
    @FXML
    ComboBox rightCharacterMenu = new ComboBox();
    @FXML
    GridPane display = new GridPane();

    ArrayList<Character> poseList = new ArrayList<>();

    public enum direction {
        LEFT, RIGHT
    }

    Character left = null;
    Character right = null;

    EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            if (e.getSource().equals(addLeft)) {
                addPoseLeft();
            } else if (e.getSource().equals(addRight)) {
                addPoseRight();
            } else if (e.getSource().equals(leftGender)) {
                changeLeftGender();
            } else if (e.getSource().equals(rightGender)) {
                changeRightGender();
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
        leftGender.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        rightGender.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
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
        // to make list alphabetical
        Collections.sort(poseList, new Comparator<Character>() {
            public int compare(Character o1, Character o2) {
                return o1.name.compareTo(o2.name);
            }
        });
    }

    public void addPoseLeft() {
        addPose(direction.LEFT);
    }

    public void addPoseRight() {
        addPose(direction.RIGHT);
    }

    // method for adding poses
    //TODO fix bug if no selection is made in the dropdown
    public void addPose(direction d) {
        if (d == direction.LEFT) {
            String menuValue = leftCharacterMenu.getValue().toString();
            if (menuValue != null) {
                // if left is already defined, override
                if (left != null) {
                    display.getChildren().remove(0, 1);
                    left = null;
                }
                // add new character
                left = findCharacter(menuValue);
                left.getImage().setFitHeight(100);
                left.getImage().setFitWidth(100);
                // add to display
                display.add(left.getImage(), 0, 1);
            }
        } else {
            String menuValue = rightCharacterMenu.getValue().toString();
            if (menuValue != null) {
                // if left is already defined, override
                if (right != null) {
                    display.getChildren().remove(1, 1);
                    right = null;
                }
                // add new character
                right = findCharacter(menuValue);
                right.getImage().setFitHeight(100);
                right.getImage().setFitWidth(100);
                // add to display
                display.add(right.getImage(), 1, 1);
            }
        }
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

    // function to find character in list
    public Character findCharacter(String name) {
        Character character = new Character();
        for (Character c : poseList) {
            if (name.equals(c.getName())) {
                character.setName(c.getName());
                character.setImage(c.getImage());
                character.setHairColor(new Color(249 / 255.0, 255 / 255.0, 0 / 255.0, 1));
                character.setSkin(new Color(255 / 255.0, 232 / 255.0, 216 / 255.0, 1));
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

    public void changeLeftGender() {
        Character character = findCharacter(leftCharacterMenu.getValue().toString());
        Color hairColor = character.getHairColor();
        Color skinColor = character.getSkin();
        display.getChildren().remove(left.getImage());
        Image input = left.getImage().getImage();
        int W = (int) input.getWidth();
        int H = (int) input.getHeight();
        WritableImage outputImage = new WritableImage(W, H);
        PixelReader reader = input.getPixelReader();
        PixelWriter writer = outputImage.getPixelWriter();


        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                Color currColor = reader.getColor(x, y);
                if (currColor.equals(Color.RED)) {
                    double newGreen = (skinColor.getGreen() * 255) + 1;
                    Color hideLipColor = new Color(255 / 255.0, newGreen / 255.0, 216 / 255.0, 1);
                    writer.setColor(x, y, hideLipColor);
                } else if (currColor.equals(Color.rgb(240, 255, 0))) {
                    double newRed = (skinColor.getRed() * 255) - 1;
                    Color hideLipColor = new Color(newRed / 255.0, 255 / 255.0, 255 / 255.0, 1);
                    writer.setColor(x, y, hideLipColor);
                } else if (currColor.equals(Color.rgb(236, 180, 181))) {
                    double newBlue = (skinColor.getBlue() * 255) - 1;
                    Color hideLipColor = new Color(255 / 255.0, 255 / 255.0, newBlue / 255.0, 1);
                    writer.setColor(x, y, hideLipColor);
                } else if (currColor.equals(new Color(255 / 255.0, ((skinColor.getGreen() * 255) + 1) / 255.0, 216 / 255.0, 1))) {
                    Color hideLipColor = new Color(255 / 255.0, 0 / 255.0, 0 / 255.0, 1);
                    writer.setColor(x, y, hideLipColor);
                } else if (currColor.equals(new Color(((skinColor.getRed() * 255) - 1) / 255.0, 255 / 255.0, 255 / 255.0, 1))) {
                    Color hideLipColor = new Color(240 / 255.0, 255 / 255.0, 0 / 255.0, 1);
                    writer.setColor(x, y, hideLipColor);
                } else if (currColor.equals(new Color(255 / 255.0, 255 / 255.0, ((skinColor.getBlue() * 255) - 1) / 255.0, 1))) {
                    Color hideLipColor = new Color(236 / 255.0, 180 / 255.0, 181 / 255.0, 1);
                    writer.setColor(x, y, hideLipColor);
                } else {
                    writer.setColor(x, y, currColor);
                }
            }
        }
        ImageView output = new ImageView(outputImage);
        output.setFitHeight(100);
        output.setFitWidth(100);
        if (left.getImage().getScaleX() == -1) {
            output.setScaleX(-1);
        } else {
            output.setScaleX(1);
        }
        left.getImage().setImage(outputImage);
        display.add(output, 0, 1);
    }

    public void changeRightGender() {
        Character character = findCharacter(rightCharacterMenu.getValue().toString());
        Color hairColor = character.getHairColor();
        Color skinColor = character.getSkin();
        display.getChildren().remove(right.getImage());
        Image input = right.getImage().getImage();
        int W = (int) input.getWidth();
        int H = (int) input.getHeight();
        WritableImage outputImage = new WritableImage(W, H);
        PixelReader reader = input.getPixelReader();
        PixelWriter writer = outputImage.getPixelWriter();


        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                Color currColor = reader.getColor(x, y);
                if (currColor.equals(Color.RED)) {
                    double newGreen = (skinColor.getGreen() * 255) + 1;
                    Color hideLipColor = new Color(255 / 255.0, newGreen / 255.0, 216 / 255.0, 1);
                    writer.setColor(x, y, hideLipColor);
                } else if (currColor.equals(Color.rgb(240, 255, 0))) {
                    double newRed = (skinColor.getRed() * 255) - 1;
                    Color hideLipColor = new Color(newRed / 255.0, 255 / 255.0, 255 / 255.0, 1);
                    writer.setColor(x, y, hideLipColor);
                } else if (currColor.equals(Color.rgb(236, 180, 181))) {
                    double newBlue = (skinColor.getBlue() * 255) - 1;
                    Color hideLipColor = new Color(255 / 255.0, 255 / 255.0, newBlue / 255.0, 1);
                    writer.setColor(x, y, hideLipColor);
                } else if (currColor.equals(new Color(255 / 255.0, ((skinColor.getGreen() * 255) + 1) / 255.0, 216 / 255.0, 1))) {
                    Color hideLipColor = new Color(255 / 255.0, 0 / 255.0, 0 / 255.0, 1);
                    writer.setColor(x, y, hideLipColor);
                } else if (currColor.equals(new Color(((skinColor.getRed() * 255) - 1) / 255.0, 255 / 255.0, 255 / 255.0, 1))) {
                    Color hideLipColor = new Color(240 / 255.0, 255 / 255.0, 0 / 255.0, 1);
                    writer.setColor(x, y, hideLipColor);
                } else if (currColor.equals(new Color(255 / 255.0, 255 / 255.0, ((skinColor.getBlue() * 255) - 1) / 255.0, 1))) {
                    Color hideLipColor = new Color(236 / 255.0, 180 / 255.0, 181 / 255.0, 1);
                    writer.setColor(x, y, hideLipColor);
                } else {
                    writer.setColor(x, y, currColor);
                }
            }
        }
        ImageView output = new ImageView(outputImage);
        output.setFitHeight(100);
        output.setFitWidth(100);
        if (right.getImage().getScaleX() == -1) {
            output.setScaleX(-1);
        } else {
            output.setScaleX(1);
        }
        right.getImage().setImage(outputImage);
        display.add(output, 1, 1);
    }
}
