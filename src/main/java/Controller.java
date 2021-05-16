package main.java;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

public class Controller {

  // Menu Items
  @FXML MenuItem saveXML = new MenuItem();
  @FXML MenuItem loadXML = new MenuItem();
  @FXML MenuItem saveHTML = new MenuItem();
  @FXML MenuItem saveGIF = new MenuItem();
  @FXML SplitPane splitPane = new SplitPane();
  @FXML Text title = new Text();
  @FXML Button addCharacter = new Button();
  @FXML Button clearPane = new Button();
  @FXML Button flipCharacter = new Button();
  @FXML Button leftGender = new Button();
  @FXML Button rightGender = new Button();
  @FXML Button Delete = new Button();
  @FXML Button save = new Button();
  @FXML Button topNarrative = new Button();
  @FXML Button bottomNarrative = new Button();
  @FXML ComboBox characterMenu = new ComboBox();
  @FXML GridPane display = new GridPane();
  @FXML ListView<Thumbnail> listView = new ListView<>();
  @FXML HBox leftDisplayBox = new HBox();
  @FXML HBox rightDisplayBox = new HBox();
  @FXML HBox speechBubbleLeft = new HBox();
  @FXML HBox speechBubbleRight = new HBox();
  @FXML ColorPicker hairColorPicker = new ColorPicker();
  @FXML ColorPicker skinColorPicker = new ColorPicker();
  @FXML TextField aboveNarrativeText;
  @FXML TextField belowNarrativeText;
  @FXML TextField textLeft = new TextField();
  @FXML TextField textRight = new TextField();
  @FXML TextField speech = new TextField();
  @FXML SplitMenuButton bubbleSelector = new SplitMenuButton();

  ArrayList<Character> poseList = new ArrayList<>();
  ArrayList<Bubble> bubbleList = new ArrayList<>();
  Label upperNarrative = new Label();
  Label lowerNarrative = new Label();
  MemoryOperations memoryOperations = new MemoryOperations();
  ExternalFileOperations externalFileOperations = new ExternalFileOperations();
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
            if (listView.getSelectionModel().getSelectedIndex() != 0) {
              memoryOperations.delete(listView.getSelectionModel().getSelectedIndex(), listView);
              disableSaveToFile(memoryOperations.isEmpty());
              clearPane();
              listView.getSelectionModel().selectFirst();
            }
          } catch (Exception f) {
            throwAlertMessage("Error deleting frame", f);
          }
        } else if (e.getSource().equals(topNarrative)) {
          addTopNarrative();
        } else if (e.getSource().equals(bottomNarrative)) {
          addBottomNarrative();
        } else if (e.getSource().equals(clearPane)) {
          clearPane();
        } else if (e.getSource().equals(save)) {
          try {
            if (listView.getSelectionModel().getSelectedIndex() > 0) {
              memoryOperations.update(
                  left,
                  right,
                  upperNarrative.getText(),
                  lowerNarrative.getText(),
                  listView,
                  listView.getSelectionModel().getSelectedIndex());
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
    saveXML.setOnAction(
        actionEvent -> externalFileOperations.saveAsXML(memoryOperations.getSavedSlides(listView)));
    loadXML.setOnAction(
        actionEvent -> {
          ArrayList<SavedSlide> savedSlides = externalFileOperations.loadXML(poseList, bubbleList);
          if (savedSlides != null) {
            clearPane();
            memoryOperations.setSavedSlides(savedSlides, listView);
          }
          disableSaveToFile(memoryOperations.isEmpty());
        });
    saveHTML.setOnAction(
        actionEvent ->
            externalFileOperations.saveAsHTML(memoryOperations.getSavedSlides(listView)));
    saveGIF.setOnAction(
        actionEvent -> externalFileOperations.saveAsGIF(memoryOperations.getSavedSlides(listView)));
    aboveNarrativeText.setFocusTraversable(false);
    belowNarrativeText.setFocusTraversable(false);
    try {
      createPoseList();
    } catch (Exception e) {
      throwAlertMessage("Error creating pose list", e);
    }

    try {
      createBubbleList();
    } catch (Exception e) {
      throwAlertMessage("Error creating bubble list", e);
    }
    for (Character pose : poseList) {
      characterMenu.getItems().add(pose.getName());
    }

    // insert empty narrative text
    upperNarrative.setFont(new Font("Arial", 15));
    upperNarrative.setText(" ");
    upperNarrative.setAlignment(Pos.CENTER);
    upperNarrative.setMaxWidth(Double.MAX_VALUE);
    display.add(upperNarrative, 0, 0, 2, 1);

    lowerNarrative.setFont(new Font("Arial", 15));
    lowerNarrative.setText(" ");
    lowerNarrative.setAlignment(Pos.CENTER);
    lowerNarrative.setMaxWidth(Double.MAX_VALUE);
    display.add(lowerNarrative, 0, 4, 2, 1);

    leftLabel.setText("");
    display.add(leftLabel, 0, 1);
    rightLabel.setText("");
    display.add(rightLabel, 1, 1);

    addCharacter.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    flipCharacter.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    leftGender.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    rightGender.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    topNarrative.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    bottomNarrative.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    hairColorPicker.setOnAction(actionEventHandler);
    skinColorPicker.setOnAction(actionEventHandler);
    save.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    listView.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    Delete.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    clearPane.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    bubbleSelector.setOnAction(actionEventHandler);

    rightDisplayBox.setOnMouseClicked((MouseEvent e) -> characterSelected(rightDisplayBox));
    leftDisplayBox.setOnMouseClicked((MouseEvent e) -> characterSelected(leftDisplayBox));
    characterSelected(leftDisplayBox);

    // new list view
    BufferedImage newPane = new BufferedImage(240, 120, 2);
    Graphics g = newPane.getGraphics();
    g.setColor(Color.BLACK);
    java.awt.Font font = new java.awt.Font("Arial", java.awt.Font.PLAIN, 15);
    g.setFont(font);
    g.drawString("New Panel", 80, 60);
    listView.setCellFactory(param -> new SlideCell());
    listView.setOrientation(Orientation.HORIZONTAL);
    listView.getItems().add(new Thumbnail(0, 240, 120, SwingFXUtils.toFXImage(newPane, null)));
    listView.getSelectionModel().selectFirst();
    listView
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (observableValue, thumbnail, t1) -> {
              try {
                if (t1.getID() == 0) {
                  clearPane();
                } else {
                  loadPane();
                }
              } catch (Exception f) {
                throwAlertMessage("Error loading Frame", f);
              }
            });
  }

  public void throwAlertMessage(String error, Exception f) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText(error);
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
      ImageView poseImage = new ImageView(inLoop.toUri().toURL().toString());
      String name = inLoop.getFileName().toString();
      if (!name.equals("bubbles")) {
        // Removes extension from file name
        if (name.indexOf(".") > 0) {
          name = name.substring(0, name.lastIndexOf("."));
        }
        bubbleList.add(new Bubble(name, poseImage));
      }
    }
    // to make list alphabetical
    bubbleList.sort(Comparator.comparing(Bubble::getName));

    for (Bubble bubble : bubbleList) {
      bubbleSelector.getItems().add(new MenuItem(bubble.getName()));
    }
    for (MenuItem bubbleItem : bubbleSelector.getItems()) {
      bubbleItem.setOnAction((event) -> bubbleSelector.setText(bubbleItem.getText()));
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
    toStore.getImage().setFitHeight(210);
    toStore.getImage().setFitWidth(210);

    // add to display
    currentlySelected.getChildren().add(toStore.getImage());

    if (currentlySelected.equals(leftDisplayBox)) {
      if (left != null) {
        toStore.setText(left.getText());
        toStore.setBubble(left.getBubble());
      }
      left = toStore;
    } else {
      if (right != null) {
        toStore.setText(right.getText());
        toStore.setBubble(right.getBubble());
      }
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
      title.setText("Left Character");
    } else {
      leftDisplayBox.setStyle("-fx-border-width: 0;");
      rightDisplayBox.setStyle(border_style);
      currentlySelected = rightDisplayBox;
      title.setText("Right Character");
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
    SavedSlide slide =
        memoryOperations.load(
            leftDisplayBox,
            rightDisplayBox,
            listView.getSelectionModel().getSelectedIndex(),
            listView,
            speechBubbleLeft,
            speechBubbleRight,
            textLeft,
            textRight,
            upperNarrative,
            lowerNarrative,
            leftLabel,
            rightLabel);
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
      }
    }
    return character;
  }

  public void flip() {
    Character toFlip;

    if (currentlySelected == null) {
      throw new IllegalArgumentException("Please select a character to flip");
    }

    if (currentlySelected.equals(leftDisplayBox)) {
      toFlip = left;
      leftDisplayBox.getChildren().clear();
    } else if (currentlySelected.equals(rightDisplayBox)) {
      toFlip = right;
      rightDisplayBox.getChildren().clear();
    } else {
      throw new IllegalArgumentException("Please select a character to flip");
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

  public enum COLORACTION {
    SKIN,
    HAIR
  }

  public void changeHairColor() {
    changeHairAndSkinColor(COLORACTION.HAIR);
  }

  public void changeSkinColor() {
    changeHairAndSkinColor(COLORACTION.SKIN);
  }

  // helper function
  public void changeHairAndSkinColor(COLORACTION x) {
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

    if (x == COLORACTION.HAIR) {
      updatedCharacter.setHairColor(hairColorPicker.getValue());
      panelSide.getChildren().add(updatedCharacter.getImage());
    } else if (x == COLORACTION.SKIN) {
      updatedCharacter.setSkin(skinColorPicker.getValue());
      panelSide.getChildren().add(updatedCharacter.getImage());
    }
  }

  public Bubble findNextBubble(String name) {
    Bubble bubble = new Bubble();
    for (Bubble c : bubbleList) {
      if (name.equals(c.getName())) {
        bubble.setName(c.getName());
        bubble.setImage(new ImageView(c.getImage().getImage()));
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
    } else {
      speakingCharacter = right;
    }
    out = speech.getText();
    BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = img.createGraphics();
    FontMetrics fm = g2d.getFontMetrics();
    double textLength_1 = fm.stringWidth(out);
    g2d.dispose();
    double textLength_2 = fm.stringWidth(out);
    // System.out.print(textLength_1+"  ");
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
        // display.getChildren().remove(leftLabel);
        leftLabel.setText("");
        speechBubbleLeft.getChildren().remove(leftBubble.getImage());
        leftBubble = null;
        left.setBubble(null);
        left.setText("");
      }

      if (!bubbleName.equals("none")) {
        leftBubble = findNextBubble(bubbleName);
        leftLabel.setText("     " + out);
        // display.add(leftLabel, 0, 1);
        speechBubbleLeft.getChildren().add(leftBubble.getImage());
        left.setBubble(leftBubble);
        left.setText(speech.getText());
      }
      speech.clear();

    } else {

      if (rightBubble != null) {
        // display.getChildren().remove(rightLabel);
        rightLabel.setText("");
        speechBubbleRight.getChildren().remove(rightBubble.getImage());
        rightBubble = null;
        right.setBubble(null);
        right.setText("");
      }

      if (!bubbleName.equals("none")) {
        rightBubble = findNextBubble(bubbleName);
        rightLabel.setText("     " + out);
        // display.add(rightLabel, 1, 1);
        speechBubbleRight.getChildren().add(rightBubble.getImage());
        right.setBubble(rightBubble);
        right.setText(speech.getText());
      }
      speech.clear();
    }
  }

  public enum narrativeLocation {
    TOP,
    BOTTOM
  }

  public void addTopNarrative() {
    addNarrativeText(narrativeLocation.TOP);
  }

  public void addBottomNarrative() {
    addNarrativeText(narrativeLocation.BOTTOM);
  }

  public void addNarrativeText(narrativeLocation l) {
    Exception f = new Exception();
    BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = img.createGraphics();
    FontMetrics fm = g2d.getFontMetrics();
    double textLength_1 = fm.stringWidth(aboveNarrativeText.getText());
    g2d.dispose();
    double textLength_2 = fm.stringWidth(belowNarrativeText.getText());
    // System.out.print(textLength_1+"  ");
    if (textLength_1 > 265 || textLength_2 > 265) {
      throwAlertMessage("message to long enter again", f);
      aboveNarrativeText.clear();
      belowNarrativeText.clear();
    } else {
      if (l == narrativeLocation.TOP) {
        if (!aboveNarrativeText.getText().equals("")) {
          upperNarrative.setText(aboveNarrativeText.getText());
          aboveNarrativeText.clear();
        }
      } else {
        if (!belowNarrativeText.getText().equals("")) {
          lowerNarrative.setText(belowNarrativeText.getText());
          belowNarrativeText.clear();
        }
      }
    }
  }

  public void disableSaveToFile(Boolean bool) {
    saveXML.setDisable(bool);
    saveHTML.setDisable(bool);
    saveGIF.setDisable(bool);
  }
}
