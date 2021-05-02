package main.java;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

public class Controller {

  //Menu Items
  @FXML MenuItem saveXML = new MenuItem();
  @FXML MenuItem loadXML = new MenuItem();
  @FXML MenuItem close = new MenuItem();

  @FXML Button addCharacter = new Button();
  @FXML Button clearPane = new Button();
  @FXML Button flipCharacter = new Button();
  @FXML Button leftGender = new Button();
  @FXML Button rightGender = new Button();
  @FXML Button LeftSpeechBubble = new Button();
  @FXML Button RightSpeechBubble = new Button();
  @FXML Button Delete = new Button();
  @FXML Button save = new Button();
  @FXML Button narrative = new Button();
  @FXML ComboBox characterMenu = new ComboBox();
  @FXML GridPane display = new GridPane();
  @FXML ListView<GridPane> listView = new ListView<>();
  @FXML HBox leftDisplayBox = new HBox();
  @FXML HBox rightDisplayBox = new HBox();
  @FXML HBox speechBubbleLeft = new HBox();
  @FXML HBox speechBubbleRight = new HBox();
  @FXML ColorPicker hairColorPicker = new ColorPicker();
  @FXML ColorPicker skinColorPicker = new ColorPicker();
  @FXML TextField usertxt;
  @FXML TextField usertxt2;
  @FXML TextField aboveNarrativeText;
  @FXML TextField belowNarrativeText;
  @FXML TextField textLeft = new TextField();
  @FXML TextField textRight = new TextField();

  ArrayList<Character> poseList = new ArrayList<>();
  ArrayList<Bubble> rightBubbleList = new ArrayList<>();
  ArrayList<Bubble> leftBubbleList = new ArrayList<>();

  Color color_1 = Color.rgb(200, 200, 200);
  Color color_2 = Color.rgb(100, 100, 100);

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
        } else if (e.getSource().equals(LeftSpeechBubble)) {
          try {
            leftSpeechBubble();
          } catch (Exception f) {
            throwAlertMessage("Error adding bubble", f);
          }
        } else if (e.getSource().equals(RightSpeechBubble)) {
          try {
            rightSpeechBubble();
          } catch (Exception f) {
            throwAlertMessage("Error adding bubble", f);
          }
        } else if (e.getSource().equals(Delete)) {
          try {
            memoryOperations.delete(listView.getSelectionModel().getSelectedIndex(), listView);
            clearPane();
          } catch (Exception f) {
            throwAlertMessage("Error deleting character", f);
          }
        } else if (e.getSource().equals(narrative)) {
          narrativeText();
        } else if (e.getSource().equals(save)) {
          try {
            memoryOperations.save(left, right, listView, aboveNarrativeText, belowNarrativeText);
          } catch (Exception f) {
            throwAlertMessage("Error saving Frame", f);
          }
        } else if (e.getSource().equals(listView)) {
          try {
            memoryOperations.load(
                    leftDisplayBox,
                    rightDisplayBox,
                    listView.getSelectionModel().getSelectedIndex(),
                    speechBubbleLeft,
                    speechBubbleRight,
                    textLeft,
                    textRight);
          } catch (Exception f) {
            throwAlertMessage("Error loading Frame", f);
          }
        }
      };

  EventHandler<ActionEvent> actionEventHandler =
      e -> {
        if (e.getSource().equals(hairColorPicker)) {
          changeHairColor();
        } else if (e.getSource().equals(skinColorPicker)) {
          changeSkinColor();
        }
      };

  public void initialize() {
    //Menu Bar
    saveXML.setOnAction(event -> saveAsXML());
    loadXML.setOnAction(event -> loadXML());

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

    addCharacter.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    flipCharacter.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    leftGender.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    rightGender.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    LeftSpeechBubble.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    RightSpeechBubble.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    narrative.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    hairColorPicker.setOnAction(actionEventHandler);
    skinColorPicker.setOnAction(actionEventHandler);
    save.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    listView.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    Delete.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);

    rightDisplayBox.setOnMouseClicked(
        (MouseEvent e) -> characterSelected(rightDisplayBox));
    leftDisplayBox.setOnMouseClicked(
        (MouseEvent e) -> characterSelected(leftDisplayBox));
  }

  public void saveAsXML() {
    FileChooser fileChooser = new FileChooser();

    //Set extension filter for text files
    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML", "*.xml");
    fileChooser.getExtensionFilters().add(extFilter);

    //Show save file dialog
    File file = fileChooser.showSaveDialog(Main.primaryStage);

    if (file != null) {
      PrintWriter writer;
      try {
        writer = new PrintWriter(file);
        ArrayList<String> strings = memoryOperations.toXML();
        for(String string : strings) {
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

    //Set extension filter for text files
    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML", "*.xml");
    fileChooser.getExtensionFilters().add(extFilter);

    //Show save file dialog
    File file = fileChooser.showOpenDialog(Main.primaryStage);

    if (file != null) {
      try {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();


        if(!doc.getDocumentElement().getNodeName().equals("comic")) {
          //alert the user their file is not valid
        }
        else {
          clearPane();
          memoryOperations.clear(listView);

          Node panelsNode = doc.getDocumentElement().getElementsByTagName("panels").item(0);
          Element panelsElement = (Element) panelsNode;
          NodeList panels = panelsElement.getElementsByTagName("panel");

          //get each panel
          for(int i = 0; i < panels.getLength(); i++) {
            Element panel = (Element) panels.item(i);
            Element above = (Element) panel.getElementsByTagName("above").item(0);
            Element below = (Element) panel.getElementsByTagName("below").item(0);

            Element leftE = (Element) panel.getElementsByTagName("left").item(0);
            Element rightE = (Element) panel.getElementsByTagName("right").item(0);
            Element leftFigure = (Element) leftE.getElementsByTagName("figure").item(0);
            Element rightFigure = (Element) rightE.getElementsByTagName("figure").item(0);

            Element leftFigureName = (Element) leftFigure.getElementsByTagName("name").item(0);
            Element rightFigureName = (Element) rightFigure.getElementsByTagName("name").item(0);

            Element leftFigureAppearance = (Element) leftFigure.getElementsByTagName("appearance").item(0);
            Element rightFigureAppearance = (Element) rightFigure.getElementsByTagName("appearance").item(0);

            Element leftFigureSkin = (Element) leftFigure.getElementsByTagName("skin").item(0);
            Element rightFigureSkin = (Element) rightFigure.getElementsByTagName("skin").item(0);

            Element leftFigureHair = (Element) leftFigure.getElementsByTagName("hair").item(0);
            Element rightFigureHair = (Element) rightFigure.getElementsByTagName("hair").item(0);

            Element leftFigureFacing = (Element) leftFigure.getElementsByTagName("facing").item(0);
            Element rightFigureFacing = (Element) rightFigure.getElementsByTagName("facing").item(0);

            //Left Character
            currentlySelected = leftDisplayBox;
            left = findCharacter(leftFigureName.getTextContent());
            left.getImage().setFitHeight(150);
            left.getImage().setFitWidth(150);
            if(leftFigureAppearance.getTextContent().equals("MALE")) {
              changeGender(leftDisplayBox);
            }
            if(!leftFigureSkin.getTextContent().equals("default")) {
              skinColorPicker.setValue(Color.web(leftFigureSkin.getTextContent()));
              changeSkinColor();
            }
            if(!leftFigureHair.getTextContent().equals("default")) {
              hairColorPicker.setValue(Color.web(leftFigureHair.getTextContent()));
              changeHairColor();
            }
            if(leftFigureFacing.getTextContent().equals("RIGHT")) {
              flip();
            }

            //Right Character
            currentlySelected = rightDisplayBox;
            right = findCharacter(rightFigureName.getTextContent());
            right.getImage().setFitHeight(150);
            right.getImage().setFitWidth(150);
            if(rightFigureAppearance.getTextContent().equals("MALE")) {
              changeGender(rightDisplayBox);
            }
            if(!rightFigureSkin.getTextContent().equals("default")) {
              skinColorPicker.setValue(Color.web(rightFigureSkin.getTextContent()));
              changeSkinColor();
            }
            if(!rightFigureHair.getTextContent().equals("default")) {
              hairColorPicker.setValue(Color.web(rightFigureHair.getTextContent()));
              changeHairColor();
            }
            if(rightFigureFacing.getTextContent().equals("RIGHT")) {
              flip();
            }

            if(above != null) {
              aboveNarrativeText.setText(above.getTextContent());
            }

            if(below != null) {
              belowNarrativeText.setText(below.getTextContent());
            }

            memoryOperations.save(left, right, listView, aboveNarrativeText, belowNarrativeText);
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
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
    URI uri = getClass().getResource("/main/resources/characters/").toURI();
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
    URI uri = getClass().getResource("/main/resources/bubbles/").toURI();
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
    leftDisplayBox.getChildren().clear();
    rightDisplayBox.getChildren().clear();
    speechBubbleLeft.getChildren().clear();
    speechBubbleRight.getChildren().clear();
    leftLabel.setText(" ");
    rightLabel.setText(" ");
    aboveNarrativeText.setText(" ");
    belowNarrativeText.setText(" ");

    if (left != null) {
      left.getImage().setScaleX(1);
    }
    if (right != null) {
      right.getImage().setScaleX(1);
    }
    left = new Character();
    right = new Character();
  }

  // function to find character in list
  public Character findCharacter(String name) {
    Character character = new Character();
    for (Character c : poseList) {
      if (name.equals(c.getName())) {
        character.setName(c.getName());
        character.setImage(new ImageView(c.getImage().getImage()));
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
    currentlySelected.setScaleX(toFlip.getScale());
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
      changingCharacter.setGender();

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
          } else if (currColor.equals(
              new Color(255 / 255.0, ((skinColor.getGreen() * 255) + 1) / 255.0, 216 / 255.0, 1))) {
            Color hideLipColor = new Color(255 / 255.0, 0 / 255.0, 0 / 255.0, 1);
            writer.setColor(j, i, hideLipColor);
          } else if (currColor.equals(
                  new Color(((skinColor.getRed() * 255) - 1) / 255.0, 255 / 255.0, 255 / 255.0, 1))
              && color_1.equals(Color.rgb(200, 200, 200))) {
            Color hideLipColor = new Color(240 / 255.0, 255 / 255.0, 0 / 255.0, 1);
            writer.setColor(j, i, hideLipColor);
          } else if (currColor.equals(
              new Color(255 / 255.0, 255 / 255.0, ((skinColor.getBlue() * 255) - 1) / 255.0, 1))) {
            Color hideLipColor = new Color(236 / 255.0, 180 / 255.0, 181 / 255.0, 1);
            writer.setColor(j, i, hideLipColor);
          } else if (currColor.equals(color_1)) {
            double newRed = (skinColor.getRed() * 255) - 1;
            Color hideLipColor = new Color(newRed / 255.0, 255 / 255.0, 255 / 255.0, 1);
            writer.setColor(j, i, hideLipColor);
          } else if (currColor.equals(
              new Color(((skinColor.getRed() * 255) - 1) / 255.0, 255 / 255.0, 255 / 255.0, 1))) {
            writer.setColor(j, i, color_1);
          } else {
            writer.setColor(j, i, currColor);
          }
        }
      }

      ImageView output = new ImageView(outputImage);
      output.setFitHeight(150);
      output.setFitWidth(150);
      output.setScaleX(changingCharacter.getScale());

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
      throw new IllegalArgumentException("Please select a character to change its gender");
    }
  }

  public void changeHairColor() {
    if (currentlySelected == null) {
        throw new IllegalArgumentException("Please select a character");
    }

    Character updatedCharacter;
    HBox panelSide;

    if(currentlySelected.equals(leftDisplayBox)){
      updatedCharacter = left;
      panelSide = leftDisplayBox;
    } else {
      updatedCharacter = right;
      panelSide = rightDisplayBox;
    }

    // original hair and braid colors
    Color originalHair = updatedCharacter.getHairColor();
    Color originalBraid = updatedCharacter.getBraidColor();

    // sets new hair color, sets new braid color to be slightly lighter than hair color
    updatedCharacter.setHairColor(hairColorPicker.getValue());
    Color interpolateWithWhite = updatedCharacter.getHairColor().interpolate(Color.WHITE, 0.1);
    int r = (int) Math.round(interpolateWithWhite.getRed() * 255);
    int g = (int) Math.round(interpolateWithWhite.getGreen() * 255);
    int b = (int) Math.round(interpolateWithWhite.getBlue() * 255);
    updatedCharacter.setBraidColor(Color.rgb(r, g, b));
    color_1 = updatedCharacter.getBraidColor();
    Image input = updatedCharacter.getImage().getImage();
    int width = (int) input.getWidth();
    int height = (int) input.getHeight();
    panelSide.getChildren().remove(updatedCharacter.getImage());
    WritableImage outputImage = new WritableImage(width, height);
    PixelReader reader = input.getPixelReader();
    PixelWriter writer = outputImage.getPixelWriter();
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        Color currColor = reader.getColor(j, i);
        if (currColor.equals(originalHair)) {
          writer.setColor(j, i, updatedCharacter.getHairColor());
        } else if (currColor.equals(originalBraid)) {
          writer.setColor(j, i, updatedCharacter.getBraidColor());
        } else {
          writer.setColor(j, i, currColor);
        }
      }
    }

    ImageView output = new ImageView(outputImage);
    output.setFitHeight(150);
    output.setFitWidth(150);
    if (updatedCharacter.getImage().getScaleX() == -1) {
      output.setScaleX(-1);
    } else {
      output.setScaleX(1);
    }
    panelSide.getChildren().clear();
    // left.getImage().setImage(outputImage);
    updatedCharacter.setImage(output);
    panelSide.getChildren().add(output);
    // display.add(output, 0, 3);
  }

  public void changeSkinColor() {
    if (currentlySelected == null) {
      throw new IllegalArgumentException("Please select a character");
    }

    Character updatedCharacter;
    HBox panelSide;

    if(currentlySelected.equals(leftDisplayBox)){
      updatedCharacter = left;
      panelSide = leftDisplayBox;
    } else {
      updatedCharacter = right;
      panelSide = rightDisplayBox;
    }

    Color originalSkin = updatedCharacter.getSkin();
    updatedCharacter.setSkin(skinColorPicker.getValue());

    Image input = updatedCharacter.getImage().getImage();
    int width = (int) input.getWidth();
    int height = (int) input.getHeight();
    panelSide.getChildren().remove(updatedCharacter.getImage());
    WritableImage outputImage = new WritableImage(width, height);
    PixelReader reader = input.getPixelReader();
    PixelWriter writer = outputImage.getPixelWriter();

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        Color currColor = reader.getColor(j, i);
        if (currColor.equals(originalSkin)) {
          writer.setColor(j, i, updatedCharacter.getSkin());
        } else {
          writer.setColor(j, i, currColor);
        }
      }
    }

    ImageView output = new ImageView(outputImage);
    output.setFitHeight(150);
    output.setFitWidth(150);
    if (updatedCharacter.getImage().getScaleX() == -1) {
      output.setScaleX(-1);
    } else {
      output.setScaleX(1);
    }
    panelSide.getChildren().remove(updatedCharacter.getImage());
    updatedCharacter.setImage(output);
    panelSide.getChildren().add(output);
    // display.add(output, 0, 3);
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

  public void leftSpeechBubble() {
    if (left == null) {
      throw new IllegalArgumentException("Please add character before adding text");
    }
    out = usertxt.getText();

    if (out.equals("")) {
      throw new IllegalArgumentException("Please add text");
    }

    String newBubble = "arrow";
    if (leftBubble != null) {
      // display.getChildren().remove(leftBubble.getImage());
      if (leftBubble.getName().equals("arrow")) {
        newBubble = "circles";
      } else {
        leftBubble = null;
        leftLabel.setText("   ");
        speechBubbleLeft.getChildren().clear();
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
    speechBubbleLeft.getChildren().clear();
    speechBubbleLeft.getChildren().add(leftBubble.getImage());
    left.setBubble(leftBubble);
    left.setText(leftLabel.getText());
  }

  public void rightSpeechBubble() {
    if (right == null) {
      throw new IllegalArgumentException("Please add character before adding text");
    }

    out = usertxt2.getText();

    if (out.equals("")) {
      throw new IllegalArgumentException("Please add text");
    }

    String newBubble = "arrow";
    if (rightBubble != null) {
      // display.getChildren().remove(rightBubble.getImage());
      if (rightBubble.getName().equals("arrow")) {
        newBubble = "circles";
      } else {
        rightBubble = null;
        rightLabel.setText("   ");
        speechBubbleRight.getChildren().clear();
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
    speechBubbleRight.getChildren().clear();
    speechBubbleRight.getChildren().add(rightBubble.getImage());
    right.setBubble(rightBubble);
    right.setText(rightLabel.getText());
  }

  public void narrativeText() {
      upperNarrative.setText(aboveNarrativeText.getText());
      lowerNarrative.setText(belowNarrativeText.getText());
  }
}
