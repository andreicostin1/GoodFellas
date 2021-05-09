package main.java;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import main.java.gif.GifSequenceWriter.GifSequenceWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

public class Controller {

    // Menu Items
    @FXML
    MenuItem saveXML = new MenuItem();
    @FXML
    MenuItem loadXML = new MenuItem();
    @FXML
    MenuItem saveHTML = new MenuItem();
    @FXML
    MenuItem saveGIF = new MenuItem();
    @FXML
    MenuItem close = new MenuItem();

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
    Button Delete = new Button();
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
    HBox speechBubbleLeft = new HBox();
    @FXML
    HBox speechBubbleRight = new HBox();
    @FXML
    ColorPicker hairColorPicker = new ColorPicker();
    @FXML
    ColorPicker skinColorPicker = new ColorPicker();
    @FXML
    TextField usertxt;
    @FXML
    TextField usertxt2;
    @FXML
    TextField aboveNarrativeText;
    @FXML
    TextField belowNarrativeText;
    @FXML
    TextField textLeft = new TextField();
    @FXML
    TextField textRight = new TextField();
    @FXML
    SplitMenuButton bubbleSelector = new SplitMenuButton();

    ArrayList<Character> poseList = new ArrayList<>();
    ArrayList<Bubble> rightBubbleList = new ArrayList<>();
    ArrayList<Bubble> leftBubbleList = new ArrayList<>();

    Label upperNarrative = new Label();
    Label lowerNarrative = new Label();

    public enum Direction {
        LEFT,
        RIGHT
    }

    MemoryOperations memoryOperations = new MemoryOperations();
    Character left;
    Character right;
    Bubble leftBubble;
    Bubble rightBubble;

    HBox currentlySelected = null;

    String out = "";
    Label leftLabel = new Label();
    Label rightLabel = new Label();

    EventHandler<MouseEvent> eventHandler =
            e -> {
                if (e.getSource().equals(addCharacter)) {
                    try {
                        addPose(currentlySelected);
                    } catch (Exception f) {
                        throwAlertMessage("Error adding character", f);
                    }
                } else if (e.getSource().equals(flipCharacter)) {
                    try {
                        flip();
                    } catch (Exception f) {
                        throwAlertMessage("Error flipping character", f);
                    }
                } else if (e.getSource().equals(leftGender)) {
                    try {
                        changeGender(currentlySelected);
                    } catch (Exception f) {
                        throwAlertMessage("Error changing gender", f);
                    }
                } else if (e.getSource().equals(Delete)) {
                    try {
                        if(listView.getSelectionModel().getSelectedIndex() != 0) {
                            memoryOperations.delete(listView.getSelectionModel().getSelectedIndex()-1, listView);
                            disableSaveToFile(memoryOperations.isEmpty());
                            clearPane();
                            listView.getSelectionModel().selectFirst();
                        }
                    } catch (Exception f) {
                        throwAlertMessage("Error deleting frame", f);
                    }
                } else if (e.getSource().equals(narrative)) {
                    narrativeText();
                } else if (e.getSource().equals(save)) {
                    try {
                        if(listView.getSelectionModel().getSelectedIndex() > 0) {
                            memoryOperations.update(left, right, upperNarrative.getText(), lowerNarrative.getText(), listView, listView.getSelectionModel().getSelectedIndex()-1);
                        } else {
                            memoryOperations.save(
                                    left, right, listView, upperNarrative.getText(), lowerNarrative.getText());
                            disableSaveToFile(memoryOperations.isEmpty());
                        }
                        clearPane();
                        listView.getSelectionModel().selectFirst();
                    } catch (Exception f) {
                        throwAlertMessage("Error saving Frame", f);
                    }
                } else if (e.getSource().equals(listView)) {
                    try {
                        if(listView.getSelectionModel().getSelectedIndex() == 0) {
                            clearPane();
                        } else {
                            loadPane();
                        }
                    } catch (Exception f) {
                        throwAlertMessage("Error loading Frame", f);
                    }
                }
            };

    EventHandler<ActionEvent> actionEventHandler =
            e -> {
                if (e.getSource().equals(hairColorPicker)) {
                    try {
                        changeHairColor();
                    } catch (Exception f) {
                        throwAlertMessage("Error changing hair color", f);
                    }
                } else if (e.getSource().equals(skinColorPicker)) {
                    try {
                        changeSkinColor();
                    } catch (Exception f) {
                        throwAlertMessage("Error changing skin color", f);
                    }
                } else if (e.getSource().equals(bubbleSelector)) {
                    try {
                        bubble();
                    } catch (Exception f) {
                        throwAlertMessage("Error loading Bubble", f);
                    }
                }
            };

    public void initialize() {
        // Menu Bar
        saveXML.setOnAction(event -> saveAsXML());
        loadXML.setOnAction(event -> loadXML());
        saveHTML.setOnAction(event -> saveAsHTML());
        saveGIF.setOnAction(event -> saveAsGIF());

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

        // insert empty narrative text
        upperNarrative.setFont(new Font("Arial", 15));
        upperNarrative.setText(" ");
        display.add(upperNarrative, 0, 0, 2, 1);
        lowerNarrative.setFont(new Font("Arial", 15));
        lowerNarrative.setText(" ");
        display.add(lowerNarrative, 0, 4, 2, 1);

        leftLabel.setText("");
        display.add(leftLabel, 0, 1);
        rightLabel.setText("");
        display.add(rightLabel, 1, 1);

        addCharacter.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        flipCharacter.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        leftGender.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        rightGender.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        narrative.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        hairColorPicker.setOnAction(actionEventHandler);
        skinColorPicker.setOnAction(actionEventHandler);
        save.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        listView.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        Delete.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        bubbleSelector.setOnAction(actionEventHandler);

        rightDisplayBox.setOnMouseClicked((MouseEvent e) -> characterSelected(rightDisplayBox));
        leftDisplayBox.setOnMouseClicked((MouseEvent e) -> characterSelected(leftDisplayBox));

        GridPane emptyPane = new GridPane();
        emptyPane.setPrefSize(225, 125);
        emptyPane.add(new Label("New Panel"), 0, 0);
        listView.getItems().add(emptyPane);
        listView.setOrientation(Orientation.HORIZONTAL);
        listView.getSelectionModel().selectFirst();
    }

    public void saveAsXML() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(Main.primaryStage);

        if (file != null) {
            PrintWriter writer;
            try {
                writer = new PrintWriter(file);
                ArrayList<String> strings = memoryOperations.toXML();
                for (String string : strings) {
                    writer.println(string);
                }
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadXML() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showOpenDialog(Main.primaryStage);

        if (file != null) {
            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(file);
                doc.getDocumentElement().normalize();

                if (!doc.getDocumentElement().getNodeName().equals("comic")) {
                    // alert the user their file is not valid
                    throwAlertMessage("File invalid", new Exception());
                } else {
                    clearPane();
                    memoryOperations.clear(listView);

                    Node panelsNode = doc.getDocumentElement().getElementsByTagName("panels").item(0);
                    Element panelsElement = (Element) panelsNode;
                    NodeList panels = panelsElement.getElementsByTagName("panel");

                    // get each panel
                    for (int i = 0; i < panels.getLength(); i++) {
                        Element panel = (Element) panels.item(i);
                        Element above = (Element) panel.getElementsByTagName("above").item(0);
                        Element below = (Element) panel.getElementsByTagName("below").item(0);

                        Element leftE = (Element) panel.getElementsByTagName("left").item(0);
                        Element rightE = (Element) panel.getElementsByTagName("right").item(0);

                        Element leftBalloon = (Element) leftE.getElementsByTagName("balloon").item(0);
                        Element rightBalloon = (Element) rightE.getElementsByTagName("balloon").item(0);

                        Element leftFigure = (Element) leftE.getElementsByTagName("figure").item(0);
                        Element rightFigure = (Element) rightE.getElementsByTagName("figure").item(0);

                        Element leftFigureName = (Element) leftFigure.getElementsByTagName("name").item(0);
                        Element rightFigureName = (Element) rightFigure.getElementsByTagName("name").item(0);

                        Element leftFigureAppearance =
                                (Element) leftFigure.getElementsByTagName("appearance").item(0);
                        Element rightFigureAppearance =
                                (Element) rightFigure.getElementsByTagName("appearance").item(0);

                        Element leftFigureSkin = (Element) leftFigure.getElementsByTagName("skin").item(0);
                        Element rightFigureSkin = (Element) rightFigure.getElementsByTagName("skin").item(0);

                        Element leftFigureHair = (Element) leftFigure.getElementsByTagName("hair").item(0);
                        Element rightFigureHair = (Element) rightFigure.getElementsByTagName("hair").item(0);

                        Element leftFigureFacing = (Element) leftFigure.getElementsByTagName("facing").item(0);
                        Element rightFigureFacing =
                                (Element) rightFigure.getElementsByTagName("facing").item(0);

                        // Left Character
                        currentlySelected = leftDisplayBox;
                        left = findCharacter(leftFigureName.getTextContent());
                        left.getImage().setFitHeight(150);
                        left.getImage().setFitWidth(150);
                        if (leftFigureAppearance.getTextContent().equals("MALE")) {
                            changeGender(leftDisplayBox);
                        }
                        if (!leftFigureSkin.getTextContent().equals("default")) {
                            skinColorPicker.setValue(Color.web(leftFigureSkin.getTextContent()));
                            changeSkinColor();
                        }
                        if (!leftFigureHair.getTextContent().equals("default")) {
                            hairColorPicker.setValue(Color.web(leftFigureHair.getTextContent()));
                            changeHairColor();
                        }
                        if (leftFigureFacing.getTextContent().equals("RIGHT")) {
                            flip();
                        }

                        if (leftBalloon != null) {
                            Element leftBalloonContent = (Element) leftBalloon.getElementsByTagName("content").item(0);

                            for (MenuItem menuItem : bubbleSelector.getItems()) {
                                if (menuItem.getText().equals(leftBalloon.getAttribute("status"))) {
                                    bubbleSelector.setText(leftBalloon.getAttribute("status"));
                                    usertxt.setText(leftBalloonContent.getTextContent());
                                    bubble();
                                }
                            }
                        }

                        //Right Character
                        currentlySelected = rightDisplayBox;
                        right = findCharacter(rightFigureName.getTextContent());
                        right.getImage().setFitHeight(150);
                        right.getImage().setFitWidth(150);
                        if (rightFigureAppearance.getTextContent().equals("MALE")) {
                            changeGender(rightDisplayBox);
                        }
                        if (!rightFigureSkin.getTextContent().equals("default")) {
                            skinColorPicker.setValue(Color.web(rightFigureSkin.getTextContent()));
                            changeSkinColor();
                        }
                        if (!rightFigureHair.getTextContent().equals("default")) {
                            hairColorPicker.setValue(Color.web(rightFigureHair.getTextContent()));
                            changeHairColor();
                        }
                        if (rightFigureFacing.getTextContent().equals("RIGHT")) {
                            flip();
                        }

                        if (above != null) {
                            aboveNarrativeText.setText(above.getTextContent());
                        }

                        if (below != null) {
                            belowNarrativeText.setText(below.getTextContent());
                        }

                        if (rightBalloon != null) {
                            Element rightBalloonContent = (Element) rightBalloon.getElementsByTagName("content").item(0);

                            for (MenuItem menuItem : bubbleSelector.getItems()) {
                                if (menuItem.getText().equals(rightBalloon.getAttribute("status"))) {
                                    bubbleSelector.setText(rightBalloon.getAttribute("status"));
                                    usertxt2.setText(rightBalloonContent.getTextContent());
                                    bubble();
                                }
                            }
                        }

                        memoryOperations.save(
                                left, right, listView, aboveNarrativeText.getText(), belowNarrativeText.getText());
                        clearPane();
                    }
                    disableSaveToFile(memoryOperations.isEmpty());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveAsHTML() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(Main.primaryStage);

        if (file != null) {
            try {
                ArrayList<BufferedImage> images = memoryOperations.toImages();
                ArrayList<String> imageNames = new ArrayList<String>(); //  To save the name of the generated series of images

                for (BufferedImage image : images) {
                    File savedImage = new File(file.getPath() + "/" + images.indexOf(image) + ".png");
                    ImageIO.write(image, "png", savedImage);
                    imageNames.add(savedImage.getName()); //  Store the name of the current image
                }

                generateHTML(imageNames, file.getPath());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveAsGIF() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter for gif
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("GIF File", "*.gif");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(Main.primaryStage);

        if (file != null) {
            try {
                ArrayList<BufferedImage> imgList = memoryOperations.toImages();

                try (
                        final FileImageOutputStream outputStream = new FileImageOutputStream(file);
                        final GifSequenceWriter writer = new GifSequenceWriter(outputStream, imgList.get(0).getType(), 6000, false)
                ) {
                    for (BufferedImage img : imgList) {
                        writer.writeToSequence(img);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void generateHTML(ArrayList<String> imageNames, String exportPath) throws FileNotFoundException {
        int i = 0;
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<style>\n" +
                "table, th, td {\n" +
                "border: 10px solid white;\n" +
                "border-collapse: collapse;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "<center>\n" +
                "<body style=\"background-color:white\">\n" +
                "<h2>Your Comic</h2>\n" +
                "<table>");

        for (String name : imageNames) {
            if (i % 2 == 0) {
                html.append("<tr>\n");
            }
            html.append("<td><center><img src=\"").append(name).append("\" width=\"500\" height=\"500\"></center></td>\n");

            if (i % 2 == 1) {
                html.append("<tr />\n");
            }

            i++;
        }

        if (i % 2 == 1) {
            html.append("<tr />\n");
        }

        html.append("</body>\n" +
                "</html>\n" +
                "</center>");

        File htmlFile = new File(exportPath + "/index.html");
        PrintStream printStream = new PrintStream(new FileOutputStream(htmlFile));
        printStream.println(html.toString());//将字符串写入文件
    }

    public void throwAlertMessage(String error, Exception f) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(error);
        alert.setContentText(f.getMessage());
        alert.showAndWait();
    }

    // Creates a list of main.java.Character objects for each image
    public void createPoseList() throws URISyntaxException, IOException {
        URI uri = Objects.requireNonNull(getClass().getResource("/main/resources/characters/")).toURI();
        Path myPath;
        FileSystem fileSystem = null;
        if (uri.getScheme().equals("jar")) {
            fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
            myPath = fileSystem.getPath("/main/resources/characters/");
        } else {
            myPath = Paths.get(uri);
        }
        Stream<Path> walk = Files.walk(myPath, 1);

        for (Iterator<Path> it = walk.iterator(); it.hasNext(); ) {
            Path inLoop = it.next();
            Image poseImage = new Image(inLoop.toUri().toURL().toString());
            String name = inLoop.getFileName().toString();
            if (!name.equals("characters")) {
                // Removes extension from file name
                if (name.indexOf(".") > 0) {
                    name = name.substring(0, name.lastIndexOf("."));
                }
                poseList.add(new Character(name, new ImageView(poseImage)));
            }
        }
        // to make list alphabetical
        poseList.sort(Comparator.comparing(Character::getName));

        if (fileSystem != null) {
            fileSystem.close();
        }
    }

    public void createBubbleList() throws URISyntaxException, IOException {
        URI uri = Objects.requireNonNull(getClass().getResource("/main/resources/bubbles/")).toURI();
        Path myPath;
        FileSystem fileSystem = null;
        if (uri.getScheme().equals("jar")) {
            fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
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
                // Removes extension from file name
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

        for (Bubble bubble : rightBubbleList) {
            bubbleSelector.getItems().add(new MenuItem(bubble.getName()));
        }
        bubbleSelector.getItems().add(new MenuItem("none"));

        for (MenuItem bubbleItem : bubbleSelector.getItems()) {
            bubbleItem.setOnAction((event) -> {
                bubbleSelector.setText(bubbleItem.getText());
            });
        }

        if (fileSystem != null) {
            fileSystem.close();
        }
    }

    // method for adding poses
    public void addPose(HBox currentlySelected) {
        Character toStore;
        String menuValue;

        if (characterMenu.getSelectionModel().isEmpty()) {
            throw new IllegalArgumentException("Please select a character");
        }

        if (currentlySelected == null) {
            throw new IllegalArgumentException("Please select a frame");
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
        } else {
            right = toStore;
        }

        skinColorPicker.setValue(toStore.getSkin());
        hairColorPicker.setValue(toStore.getHairColor());
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

        aboveNarrativeText.clear();
        belowNarrativeText.clear();
        leftDisplayBox.getChildren().clear();
        rightDisplayBox.getChildren().clear();
        speechBubbleLeft.getChildren().clear();
        speechBubbleRight.getChildren().clear();
        leftLabel.setText(" ");
        rightLabel.setText(" ");
        upperNarrative.setText(" ");
        lowerNarrative.setText(" ");

        left = null;
        right = null;
    }

    // loads objects in saved frame
    public void loadPane() {
        SavedSlide slide = memoryOperations.load(leftDisplayBox, rightDisplayBox,
                listView.getSelectionModel().getSelectedIndex()-1, speechBubbleLeft, speechBubbleRight, textLeft,
                textRight, upperNarrative, lowerNarrative, leftLabel, rightLabel);
        left = slide.getCharacterLeft();
        right = slide.getCharacterRight();
        leftBubble = left.getBubble();
        rightBubble = right.getBubble();
    }

    // function to find character in list
    public Character findCharacter(String name) {
        Character character = new Character();
        for (Character c : poseList) {
            if (name.equals(c.getName())) {
                character.setName(c.getName());
                character.setImage(new ImageView(c.getImage().getImage()));
                character.setText("");
                character.setHairColor(new Color(249 / 255.0, 255 / 255.0, 0 / 255.0, 1));
                character.setSkin(new Color(255 / 255.0, 232 / 255.0, 216 / 255.0, 1));
                character.setBraidColor(new Color(240 / 255.0, 255 / 255.0, 0 / 255.0, 1));
            }
        }
        return character;
    }

    public void flip() {
        Character toFlip = new Character();

        if (currentlySelected == null) {
            throw new IllegalArgumentException("Please select a character to flip");
        }

        if (currentlySelected.equals(leftDisplayBox)) {
            toFlip = left;
            leftDisplayBox.getChildren().clear();
        } else if (currentlySelected.equals(rightDisplayBox)) {
            toFlip = right;
            rightDisplayBox.getChildren().clear();
        }

        toFlip.setScale();
        currentlySelected.getChildren().add(toFlip.getImage());

        if (currentlySelected.equals(leftDisplayBox)) {
            left = toFlip;
        } else {
            right = toFlip;
        }
    }

    public void changeGender(HBox currentlySelected) {

        Character changingCharacter;

        try {
            if (currentlySelected.equals(leftDisplayBox)) {
                changingCharacter = left;
            } else {
                changingCharacter = right;
            }

            currentlySelected.getChildren().clear();
            changingCharacter.setGender();

            if (currentlySelected.equals(leftDisplayBox)) {
                left = changingCharacter;
                leftDisplayBox.getChildren().add(left.getImage());
            } else {
                right = changingCharacter;
                rightDisplayBox.getChildren().add(right.getImage());
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Please select a character to change its gender");
        }
    }

    public void changeHairColor() {
        if (currentlySelected == null) {
            throw new IllegalArgumentException("Please select a character");
        }

        Character updatedCharacter;
        HBox panelSide;

        if (currentlySelected.equals(leftDisplayBox)) {
            updatedCharacter = left;
            panelSide = leftDisplayBox;
        } else {
            updatedCharacter = right;
            panelSide = rightDisplayBox;
        }

        if (updatedCharacter == null) {
            throw new IllegalArgumentException("Please add a character");
        }

        panelSide.getChildren().clear();
        updatedCharacter.setHairColor(hairColorPicker.getValue());
        panelSide.getChildren().add(updatedCharacter.getImage());

    }

    public void changeSkinColor() {
        if (currentlySelected == null) {
            throw new IllegalArgumentException("Please select a character");
        }

        Character updatedCharacter;
        HBox panelSide;

        if (currentlySelected.equals(leftDisplayBox)) {
            updatedCharacter = left;
            panelSide = leftDisplayBox;
        } else {
            updatedCharacter = right;
            panelSide = rightDisplayBox;
        }

        if (updatedCharacter == null) {
            throw new IllegalArgumentException("Please add a character");
        }

        panelSide.getChildren().clear();
        updatedCharacter.setSkin(skinColorPicker.getValue());
        panelSide.getChildren().add(updatedCharacter.getImage());
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

    public void bubble() {
        String bubbleName = bubbleSelector.getText();
        if (bubbleName.equals("Bubbles")) {
            return;
        }
        if (currentlySelected == null) {
            throw new IllegalArgumentException("Please select a character");
        }

        Character speakingCharacter;

        if (currentlySelected.equals(leftDisplayBox)) {
            speakingCharacter = left;
            out = usertxt.getText();
        } else {
            speakingCharacter = right;
            out = usertxt2.getText();
        }
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        FontMetrics fm = g2d.getFontMetrics();
        double textLength_1 = fm.stringWidth(out);
        g2d.dispose();
        double textLength_2 = fm.stringWidth(out);
        //System.out.print(textLength_1+"  ");
        if (textLength_1 > 170 || textLength_2 > 170) {
            throw new IllegalArgumentException("message to long enter again");
        }
        if (speakingCharacter == null) {
            throw new IllegalArgumentException("Please add character before adding text");
        }

        if (out.equals("") && !bubbleName.equals("none")) {
            throw new IllegalArgumentException("Please add text");
        }

        if (currentlySelected.equals(leftDisplayBox)) {

            if (leftBubble != null) {
                //display.getChildren().remove(leftLabel);
                leftLabel.setText("");
                speechBubbleLeft.getChildren().remove(leftBubble.getImage());
                leftBubble = null;
                left.setBubble(null);
                left.setText("");
            }

            if (!bubbleName.equals("none")) {
                leftBubble = findNextBubble(Direction.LEFT, bubbleName);
                leftBubble.getImage().setFitWidth(90);
                leftBubble.getImage().setFitHeight(25);
                leftLabel.setText("     " + out);
                //display.add(leftLabel, 0, 1);
                speechBubbleLeft.getChildren().add(leftBubble.getImage());
                left.setBubble(leftBubble);
                left.setText(usertxt.getText());
            }
            usertxt.clear();

        } else {

            if (rightBubble != null) {
                //display.getChildren().remove(rightLabel);
                rightLabel.setText("");
                speechBubbleRight.getChildren().remove(rightBubble.getImage());
                rightBubble = null;
                right.setBubble(null);
                right.setText("");
            }

            if (!bubbleName.equals("none")) {
                rightBubble = findNextBubble(Direction.RIGHT, bubbleName);
                rightBubble.getImage().setFitWidth(90);
                rightBubble.getImage().setFitHeight(25);
                rightLabel.setText("     " + out);
                //display.add(rightLabel, 1, 1);
                speechBubbleRight.getChildren().add(rightBubble.getImage());
                right.setBubble(rightBubble);
                right.setText(usertxt2.getText());
            }
            usertxt2.clear();
        }
    }

    public void narrativeText() {
        upperNarrative.setText(aboveNarrativeText.getText());
        lowerNarrative.setText(belowNarrativeText.getText());
        aboveNarrativeText.clear();
        belowNarrativeText.clear();
    }

    public void disableSaveToFile(Boolean bool) {
        saveXML.setDisable(bool);
        saveHTML.setDisable(bool);
        saveGIF.setDisable(bool);
    }
}
