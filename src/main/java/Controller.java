package main.java;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;


public class Controller {

    @FXML
    Button addCharacter = new Button();
    @FXML
    Button clearPane = new Button();
    @FXML
    Button flipCharacter = new Button();
    @FXML
    Button leftGender = new Button();
    @FXML
    Button rightGender = new Button();
    @FXML
    Button LeftSpeechBubble = new Button();
    @FXML
    Button RightSpeechBubble = new Button();
    @FXML
    Button save = new Button();
    @FXML
    Button narrative = new Button();
    @FXML
    ComboBox characterMenu = new ComboBox();
    @FXML
    GridPane display = new GridPane();
    @FXML
    ListView<GridPane> listView = new ListView<>();
    @FXML
    HBox leftDisplayBox = new HBox();
    @FXML
    HBox rightDisplayBox = new HBox();
    @FXML
    ColorPicker leftHairColorPicker = new ColorPicker();
    @FXML
    ColorPicker rightHairColorPicker = new ColorPicker();
    @FXML
    ColorPicker leftSkinColorPicker = new ColorPicker();
    @FXML
    ColorPicker rightSkinColorPicker = new ColorPicker();
    @FXML
    TextField usertxt;
    @FXML
    TextField usertxt2;
    @FXML
    TextField narrativeText;

    ArrayList<Character> poseList = new ArrayList<>();
    ArrayList<Bubble> rightBubbleList = new ArrayList<>();
    ArrayList<Bubble> leftBubbleList = new ArrayList<>();

    Color color_1 = Color.rgb(200, 200, 200);
    Color color_2 = Color.rgb(100, 100, 100);

    Label upperNarrative = new Label();
    Label lowerNarrative = new Label();

    int pointer = 0;
    Map<Integer, Character> current_left = new HashMap<>();
    Map<Integer, Character> current_right = new HashMap<>();
    Map<Integer, Label> leftText = new HashMap<>();
    Map<Integer, Label> rightText = new HashMap<>();
    Map<Integer, Bubble> SpeechBubble_left = new HashMap<>();
    Map<Integer, Bubble> SpeechBubble_right = new HashMap<>();

    public enum Direction {
        LEFT, RIGHT, UP, DOWN, NONE
    }

    //where the narrative text currently is
    Direction narrativeDirection = Direction.NONE;

    MemoryOperations memoryOperations = new MemoryOperations();
    Character left = null;
    Character right = null;
    Bubble leftBubble = null;
    Bubble rightBubble = null;

    HBox currentlySelected = null;
    Bubble arrow = null;

    EventHandler<MouseEvent> eventHandler = e -> {
        if (e.getSource().equals(addCharacter)) {
            addPose(currentlySelected);
        } else if (e.getSource().equals(flipCharacter)) {
            flip(currentlySelected);
        } else if (e.getSource().equals(leftGender)) {
            changeGender(currentlySelected);
        } else if (e.getSource().equals(LeftSpeechBubble)) {
            LeftSpeechBubble();
        } else if (e.getSource().equals(RightSpeechBubble)) {
            RightSpeechBubble();
        }
//        else if (e.getSource().equals(narrative)) {
//            narrativeText();
//        }
        else if (e.getSource().equals(save)) {
            memoryOperations.save(left, right, display, listView);
        } else if (e.getSource().equals(listView)) {
            int index = listView.getSelectionModel().getSelectedIndex();
            memoryOperations.load(leftDisplayBox, rightDisplayBox, index, listView);
        }
    };

    EventHandler<ActionEvent> actionEventHandler = e -> {
        if (e.getSource().equals(leftHairColorPicker)) {
            changeLeftHairColor();
        } else if (e.getSource().equals(rightHairColorPicker)) {
            changeRightHairColor();
        } else if (e.getSource().equals(leftSkinColorPicker)) {
            changeLeftSkinColor();
        } else if (e.getSource().equals(rightSkinColorPicker)) {
            changeRightSkinColor();
        }
    };


    public void initialize() {
        try {
            createPoseList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            createBubbleList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Character pose : poseList) {
            characterMenu.getItems().add(pose.getName());
        }

        //insert empty narrative text
        upperNarrative.setFont(new Font("Arial", 15));
        upperNarrative.setText(" ");
        display.add(upperNarrative, 0, 0, 2, 1);
        lowerNarrative.setFont(new Font("Arial", 15));
        lowerNarrative.setText(" ");
        display.add(lowerNarrative, 0, 4, 2, 1);

        addCharacter.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        flipCharacter.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        leftGender.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        rightGender.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        LeftSpeechBubble.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        RightSpeechBubble.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        narrative.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        leftHairColorPicker.setOnAction(actionEventHandler);
        rightHairColorPicker.setOnAction(actionEventHandler);
        leftSkinColorPicker.setOnAction(actionEventHandler);
        rightSkinColorPicker.setOnAction(actionEventHandler);
        save.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        listView.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);

        rightDisplayBox.setOnMouseClicked((MouseEvent e) -> {
            characterSelected(rightDisplayBox);
        });
        leftDisplayBox.setOnMouseClicked((MouseEvent e) -> {
            characterSelected(leftDisplayBox);
        });

    }

    //Creates a list of main.java.Character objects for each image
    public void createPoseList() throws URISyntaxException, IOException {
        URI uri = getClass().getResource("/main/resources/characters/").toURI();
        Path myPath;
        if (uri.getScheme().equals("jar")) {
            FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
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
        poseList.sort(Comparator.comparing(o -> o.name));
    }

    public void createBubbleList() throws URISyntaxException, IOException {
        URI uri = getClass().getResource("/main/resources/bubbles/").toURI();
        Path myPath;
        if (uri.getScheme().equals("jar")) {
            FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
            myPath = fileSystem.getPath("/main/resources/bubbles/");
        } else {
            myPath = Paths.get(uri);
        }
        Stream<Path> walk = Files.walk(myPath, 1);

        for (Iterator<Path> it = walk.iterator(); it.hasNext(); ) {
            Path inLoop = it.next();
            ImageView poseImageR = new ImageView(inLoop.toUri().toURL().toString());
            ImageView poseImageL = new ImageView(inLoop.toUri().toURL().toString());
            String name = inLoop.getFileName().toString();
            if (!name.equals("bubbles")) {
                //Removes extension from file name
                if (name.indexOf(".") > 0) {
                    name = name.substring(0, name.lastIndexOf("."));
                }
                rightBubbleList.add(new Bubble(name, poseImageR));
                leftBubbleList.add(new Bubble(name, poseImageL));
            }
        }
        // to make list alphabetical
        rightBubbleList.sort(Comparator.comparing(Bubble::getName));
        leftBubbleList.sort(Comparator.comparing(Bubble::getName));
    }

    // method for adding poses
    public void addPose(HBox currentlySelected) {
        Character toStore;
        String menuValue;

        if (characterMenu.getSelectionModel().isEmpty()) {
            return;
        }

        menuValue = characterMenu.getValue().toString();

        // if character is already defined, override
        if (currentlySelected.getChildren() != null) {
            currentlySelected.getChildren().clear();
        }

        // add new character
        toStore = findCharacter(menuValue);
        toStore.getImage().setFitHeight(150);
        toStore.getImage().setFitWidth(150);

        // add to display
        currentlySelected.getChildren().add(toStore.getImage());

        if (currentlySelected.equals(leftDisplayBox)) {
            left = toStore;
            leftSkinColorPicker.setValue(toStore.getSkin());
            leftHairColorPicker.setValue(toStore.getHairColor());
        } else {
            right = toStore;
            rightSkinColorPicker.setValue(toStore.getSkin());
            rightHairColorPicker.setValue(toStore.getHairColor());
        }
    }

    public void characterSelected(HBox side) {


        String border_style = "-fx-border-color: red;" + "-fx-border-width: 1;";

        if (side.equals(leftDisplayBox)) {
            rightDisplayBox.setStyle("-fx-border-width: 0;");
            leftDisplayBox.setStyle(border_style);
            currentlySelected = leftDisplayBox;
        } else if (side.equals(rightDisplayBox)) {
            leftDisplayBox.setStyle("-fx-border-width: 0;");
            rightDisplayBox.setStyle(border_style);
            currentlySelected = rightDisplayBox;
        }
    }

    // function to clear the display
    public void clearPane() {
        leftDisplayBox.getChildren().clear();
        rightDisplayBox.getChildren().clear();
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
                character.setBraidColor(new Color(240 / 255.0, 255 / 255.0, 0 / 255.0, 1));
            }
        }
        return character;
    }

    public void flip(HBox currentlySelected) {
        Character toFlip = null;

        if (currentlySelected.equals(leftDisplayBox)) {
            toFlip = left;
            leftDisplayBox.getChildren().clear();
        } else if (currentlySelected.equals(rightDisplayBox)) {
            toFlip = right;
            rightDisplayBox.getChildren().clear();
        } else if (toFlip == null) {
            return;
        }
        if (currentlySelected.getScaleX() == -1) {
            currentlySelected.setScaleX(1);
        } else {
            currentlySelected.setScaleX(-1);
        }
        currentlySelected.getChildren().add(toFlip.getImage());

        if (currentlySelected.equals(leftDisplayBox)) {
            left = toFlip;
        } else {
            right = toFlip;
        }
    }

    public void changeGender(HBox currentlySelected) {
        Character changingCharacter = null;

        try {
            if (currentlySelected.equals(leftDisplayBox)) {
                changingCharacter = left;
            } else {
                changingCharacter = right;
            }

            currentlySelected.getChildren().clear();

            Color skinColor = changingCharacter.getSkin();
            Image input = changingCharacter.getImage().getImage();
            int width = (int) input.getWidth();
            int height = (int) input.getHeight();

            WritableImage outputImage = new WritableImage(width, height);
            PixelReader reader = input.getPixelReader();
            PixelWriter writer = outputImage.getPixelWriter();


            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    Color currColor = reader.getColor(j, i);
                    if (currColor.equals(Color.RED)) {
                        double newGreen = (skinColor.getGreen() * 255) + 1;
                        Color hideLipColor = new Color(255 / 255.0, newGreen / 255.0, 216 / 255.0, 1);
                        writer.setColor(j, i, hideLipColor);
                    } else if (currColor.equals(Color.rgb(240, 255, 0))) {
                        double newRed = (skinColor.getRed() * 255) - 1;
                        Color hideLipColor = new Color(newRed / 255.0, 255 / 255.0, 255 / 255.0, 1);
                        writer.setColor(j, i, hideLipColor);
                    } else if (currColor.equals(Color.rgb(236, 180, 181))) {
                        double newBlue = (skinColor.getBlue() * 255) - 1;
                        Color hideLipColor = new Color(255 / 255.0, 255 / 255.0, newBlue / 255.0, 1);
                        writer.setColor(j, i, hideLipColor);
                    } else if (currColor.equals(new Color(255 / 255.0, ((skinColor.getGreen() * 255) + 1) / 255.0, 216 / 255.0, 1))) {
                        Color hideLipColor = new Color(255 / 255.0, 0 / 255.0, 0 / 255.0, 1);
                        writer.setColor(j, i, hideLipColor);
                    } else if (currColor.equals(new Color(((skinColor.getRed() * 255) - 1) / 255.0, 255 / 255.0, 255 / 255.0, 1)) && color_1.equals(Color.rgb(200, 200, 200))) {
                        Color hideLipColor = new Color(240 / 255.0, 255 / 255.0, 0 / 255.0, 1);
                        writer.setColor(j, i, hideLipColor);
                    } else if (currColor.equals(new Color(255 / 255.0, 255 / 255.0, ((skinColor.getBlue() * 255) - 1) / 255.0, 1))) {
                        Color hideLipColor = new Color(236 / 255.0, 180 / 255.0, 181 / 255.0, 1);
                        writer.setColor(j, i, hideLipColor);
                    } else if (currColor.equals(color_1)) {
                        double newRed = (skinColor.getRed() * 255) - 1;
                        Color hideLipColor = new Color(newRed / 255.0, 255 / 255.0, 255 / 255.0, 1);
                        writer.setColor(j, i, hideLipColor);
                    } else if (currColor.equals(new Color(((skinColor.getRed() * 255) - 1) / 255.0, 255 / 255.0, 255 / 255.0, 1))) {
                        writer.setColor(j, i, color_1);
                    } else {
                        writer.setColor(j, i, currColor);
                    }
                }
            }

            ImageView output = new ImageView(outputImage);
            output.setFitHeight(150);
            output.setFitWidth(150);

            if (changingCharacter.getImage().getScaleX() == -1) {
                output.setScaleX(-1);
            } else {
                output.setScaleX(1);
            }

            if (currentlySelected.equals(leftDisplayBox)) {
                left = changingCharacter;
                left.getImage().setImage(outputImage);
                leftDisplayBox.getChildren().add(output);

            } else {
                right = changingCharacter;
                right.getImage().setImage(outputImage);
                rightDisplayBox.getChildren().add(output);
            }

        } catch (Exception e) {
            Alert warning = new Alert(Alert.AlertType.WARNING);
            warning.setContentText("Please select a character to change");
            warning.show();
        }
    }

    public void changeLeftHairColor() {
        if (left == null) {
            return;
        }

        //original hair and braid colors
        Color originalHair = left.getHairColor();
        Color originalBraid = left.getBraidColor();

        //sets new hair color, sets new braid color to be slightly lighter than hair color
        left.setHairColor(leftHairColorPicker.getValue());
        Color interpolateWithWhite = left.getHairColor().interpolate(Color.WHITE, 0.1);
        int r = (int) Math.round(interpolateWithWhite.getRed() * 255);
        int g = (int) Math.round(interpolateWithWhite.getGreen() * 255);
        int b = (int) Math.round(interpolateWithWhite.getBlue() * 255);
        left.setBraidColor(Color.rgb(r, g, b));
        color_1 = left.getBraidColor();
        Image input = left.getImage().getImage();
        int width = (int) input.getWidth();
        int height = (int) input.getHeight();
        display.getChildren().remove(left.getImage());
        WritableImage outputImage = new WritableImage(width, height);
        PixelReader reader = input.getPixelReader();
        PixelWriter writer = outputImage.getPixelWriter();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color currColor = reader.getColor(j, i);
                if (currColor.equals(originalHair)) {
                    writer.setColor(j, i, left.getHairColor());
                } else if (currColor.equals(originalBraid)) {
                    writer.setColor(j, i, left.getBraidColor());
                } else {
                    writer.setColor(j, i, currColor);
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
        //left.getImage().setImage(outputImage);
        left.setImage(output);
        display.add(output, 0, 1);
    }

    public void changeRightHairColor() {
        if (right == null) {
            return;
        }

        //original hair colors
        Color originalHair = right.getHairColor();
        Color originalBraid = right.getBraidColor();

        //sets new hair color, sets new braid color to be slightly lighter than hair color
        right.setHairColor(rightHairColorPicker.getValue());
        Color interpolateWithWhite = right.getHairColor().interpolate(Color.WHITE, 0.1);
        int r = (int) Math.round(interpolateWithWhite.getRed() * 255);
        int g = (int) Math.round(interpolateWithWhite.getGreen() * 255);
        int b = (int) Math.round(interpolateWithWhite.getBlue() * 255);
        right.setBraidColor(Color.rgb(r, g, b));
        color_2 = right.getBraidColor();
        Image input = right.getImage().getImage();
        int width = (int) input.getWidth();
        int height = (int) input.getHeight();
        display.getChildren().remove(right.getImage());
        WritableImage outputImage = new WritableImage(width, height);
        PixelReader reader = input.getPixelReader();
        PixelWriter writer = outputImage.getPixelWriter();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color currColor = reader.getColor(j, i);
                if (currColor.equals(originalHair)) {
                    writer.setColor(j, i, right.getHairColor());
                } else if (currColor.equals(originalBraid)) {
                    writer.setColor(j, i, right.getBraidColor());
                } else {
                    writer.setColor(j, i, currColor);
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
        //right.getImage().setImage(outputImage);
        right.setImage(output);
        display.add(output, 1, 1);
    }

    public void changeLeftSkinColor() {
        if (left == null) {
            return;
        }

        Color originalSkin = left.getSkin();
        left.setSkin(leftSkinColorPicker.getValue());

        Image input = left.getImage().getImage();
        int width = (int) input.getWidth();
        int height = (int) input.getHeight();
        display.getChildren().remove(left.getImage());
        WritableImage outputImage = new WritableImage(width, height);
        PixelReader reader = input.getPixelReader();
        PixelWriter writer = outputImage.getPixelWriter();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color currColor = reader.getColor(j, i);
                if (currColor.equals(originalSkin)) {
                    writer.setColor(j, i, left.getSkin());
                } else {
                    writer.setColor(j, i, currColor);
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
        left.setImage(output);
        display.add(output, 0, 1);
    }

    public void changeRightSkinColor() {
        if (right == null) {
            return;
        }

        Color originalSkin = right.getSkin();
        right.setSkin(rightSkinColorPicker.getValue());

        Image input = right.getImage().getImage();
        int width = (int) input.getWidth();
        int height = (int) input.getHeight();
        display.getChildren().remove(right.getImage());
        WritableImage outputImage = new WritableImage(width, height);
        PixelReader reader = input.getPixelReader();
        PixelWriter writer = outputImage.getPixelWriter();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color currColor = reader.getColor(j, i);
                if (currColor.equals(originalSkin)) {
                    writer.setColor(j, i, right.getSkin());
                } else {
                    writer.setColor(j, i, currColor);
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
        right.setImage(output);
        display.add(output, 1, 1);
    }

    public Bubble findNextBubble(Direction d, String name) {
        Bubble bubble = new Bubble();
        List<Bubble> bubbleList;
        if (d == Direction.LEFT) {
            bubbleList = leftBubbleList;
        } else {
            bubbleList = rightBubbleList;
        }
        for (Bubble c : bubbleList) {
            if (name.equals(c.getName())) {
                bubble.setName(c.getName());
                bubble.setImage(c.getImage());
            }
        }
        return bubble;
    }

    String out = "";
    Label leftLabel = new Label();
    Label rightLabel = new Label();

    public void LeftSpeechBubble() {
        out = usertxt.getText();
        String newBubble = "arrow";
        if (leftBubble != null) {
            display.getChildren().remove(leftBubble.getImage());
            if (leftBubble.getName().equals("arrow")) {
                newBubble = "circles";
            } else {
                leftBubble = null;
                leftLabel.setText("   ");
                return;
            }
        }
        leftBubble = findNextBubble(Direction.LEFT, newBubble);
        leftBubble.getImage().setFitWidth(90);
        leftBubble.getImage().setFitHeight(25);
        if (!leftLabel.getText().equals("")) {
            leftLabel.setText("");
            leftLabel.setText("     " + out);
        } else {
            leftLabel.setText("     " + out);
            display.add(leftLabel, 0, 1);
        }
        display.add(leftBubble.getImage(), 0, 2);
    }

    public void RightSpeechBubble() {
        out = usertxt2.getText();
        String newBubble = "arrow";
        if (rightBubble != null) {
            display.getChildren().remove(rightBubble.getImage());
            if (rightBubble.getName().equals("arrow")) {
                newBubble = "circles";
            } else {
                rightBubble = null;
                rightLabel.setText("   ");
                return;
            }
        }
        rightBubble = findNextBubble(Direction.RIGHT, newBubble);
        rightBubble.getImage().setFitWidth(90);
        rightBubble.getImage().setFitHeight(25);
        if (!rightLabel.getText().equals("")) {
            rightLabel.setText("");
            rightLabel.setText("     " + out);
        } else {
            rightLabel.setText("     " + out);
            display.add(rightLabel, 1, 1);
        }
        display.add(rightBubble.getImage(), 1, 2);
    }

//    public void narrativeText() {
//        switch (narrativeDirection) {
//            case UP -> {
//                upperNarrative.setText(" ");
//                lowerNarrative.setText(narrativeText.getText());
//                narrativeDirection = Direction.DOWN;
//            }
//            case DOWN -> {
//                lowerNarrative.setText(" ");
//                narrativeDirection = Direction.NONE;
//            }
//            default -> {
//                upperNarrative.setText(narrativeText.getText());
//                narrativeDirection = Direction.UP;
//            }
//        }
//    }

    public void save() {
        GridPane view = new GridPane();
        view.setPrefSize(225, 225);
        view.setGridLinesVisible(true);
        current_left.put(pointer, left);
        current_right.put(pointer, right);
        leftText.put(pointer, leftLabel);
        rightText.put(pointer, rightLabel);
        SpeechBubble_left.put(pointer, leftBubble);
        SpeechBubble_right.put(pointer, rightBubble);
        try {
            if (current_left.get(pointer).getImage() != null && current_right.get(pointer).getImage() != null) {
                ImageView leftImage = current_left.get(pointer).getImage();
                ImageView rightImage = current_right.get(pointer).getImage();

                leftImage.setFitHeight(110);
                leftImage.setFitWidth(110);
                rightImage.setFitWidth(110);
                rightImage.setFitHeight(110);
                System.out.print(left.getImage().getScaleX() + "     ");

                if (left.getImage().getScaleX() == -1) {
                    leftImage.setScaleX(-1);
                } else {
                    leftImage.setScaleX(1);
                }

                if (right.getImage().getScaleX() == -1) {
                    rightImage.setScaleX(-1);
                } else {
                    rightImage.setScaleX(1);
                }

                view.add(leftImage, 0, 0);
                view.add(rightImage, 1, 0);
                listView.getItems().add(view);
                listView.setOrientation(Orientation.HORIZONTAL);
                //saveListView.setPrefSize(675,138);

                leftDisplayBox.getChildren().clear();
                rightDisplayBox.getChildren().clear();
                pointer++;
            }
        } catch (Exception e) {
            Alert warning = new Alert(Alert.AlertType.WARNING);
            warning.setContentText("Please put both character");
            warning.show();
        }
    }
}
