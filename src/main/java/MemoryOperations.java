package main.java;

import javafx.geometry.Orientation;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class MemoryOperations {

  GridPane savedSlide;
  private ArrayList<SavedSlide> slideArrayList = new ArrayList<>();
  private int id = 0;

  public void save(
      Character left,
      Character right,
      ListView<GridPane> listView,
      TextField aboveNarrativeText,
      TextField belowNarrativeText) {
    if (left == null || right == null) {
      throw new IllegalArgumentException("Needs two characters in frame");
    }
    savedSlide = generateSlide(left, right, aboveNarrativeText, belowNarrativeText);
    savedSlide.setPrefSize(225, 225);
    savedSlide.setGridLinesVisible(true);
    listView.getItems().add(savedSlide);
    listView.setOrientation(Orientation.HORIZONTAL);
    id++;
  }

  public void load(
      HBox leftDisplay,
      HBox rightDisplay,
      int index,
      HBox speechBubbleLeft,
      HBox speechBubbleRight,
      TextField textLeft,
      TextField textRight) {
    if (slideArrayList.size() == 0) {
      throw new IllegalArgumentException("Please add a slide before trying to load it");
    }

    leftDisplay.getChildren().clear();
    rightDisplay.getChildren().clear();

    SavedSlide slideToLoad = slideArrayList.get(index);

    ImageView left = new ImageView(slideToLoad.getCharacterLeft().getImage().getImage());
    ImageView right = new ImageView(slideToLoad.getCharacterRight().getImage().getImage());

    left.setFitHeight(150);
    left.setFitWidth(150);
    leftDisplay.getChildren().add(left);

    if (slideToLoad.getCharacterLeft().getBubble() != null) {
      speechBubbleLeft.getChildren().add(slideToLoad.getCharacterLeft().getBubble().getImage());
    }
    if (slideToLoad.getCharacterLeft().getText() != null) {
      textLeft.setText(slideToLoad.getCharacterLeft().getText());
    }

    right.setFitHeight(150);
    right.setFitWidth(150);
    rightDisplay.getChildren().add(right);

    if (slideToLoad.getCharacterRight().getBubble() != null) {
      speechBubbleRight.getChildren().add(slideToLoad.getCharacterRight().getBubble().getImage());
    }
    if (slideToLoad.getCharacterRight().getText() != null) {
      textRight.setText(slideToLoad.getCharacterRight().getText());
    }
  }

  public void delete(int index, ListView<GridPane> listView) {
    if (listView.getItems().size() == 0) {
      throw new IllegalArgumentException("Please add a slide before deleting");
    }
    listView.getItems().remove(index);
    id--;
  }

  public GridPane generateSlide(Character left, Character right, TextField aboveNarrativeText, TextField belowNarrativeText) {
    GridPane generatedSlide = new GridPane();

    SavedSlide slide = new SavedSlide(id, left, right, aboveNarrativeText, belowNarrativeText);
    slideArrayList.add(slide);

    ImageView leftImage = slide.getCharacterLeft().getImage();
    ImageView rightImage = slide.getCharacterRight().getImage();

    leftImage.setFitHeight(110);
    leftImage.setFitWidth(110);
    leftImage.setScaleX(left.getScale());
    rightImage.setFitWidth(110);
    rightImage.setFitHeight(110);
    rightImage.setScaleX(right.getScale());

    generatedSlide.add(leftImage, 0, 0);
    generatedSlide.add(rightImage, 1, 0);

    return generatedSlide;
  }

  public ArrayList<String> toXML() {
    Color defaultSkin = new Color(255 / 255.0, 232 / 255.0, 216 / 255.0, 1);
    Color defaultHair = new Color(249 / 255.0, 255 / 255.0, 0 / 255.0, 1);
    Color defaultBraid = new Color(240 / 255.0, 255 / 255.0, 0 / 255.0, 1);

    Character character;

    ArrayList<String> strings = new ArrayList<>();
    strings.add("<?xml version = \"1.0\" encoding = \"UTF-8\"?>");
    strings.add("<comic>");
    strings.add("<panels>");

    for (SavedSlide slide : slideArrayList) {
      strings.add("<panel>");

      if (!slide.getAboveNarrativeText().getText().isEmpty()) {
        strings.add("<above>" + slide.getAboveNarrativeText().getText() + "</above>");
      }

      // Left Character
      strings.add("<left>");
      if (slide.getCharacterLeft() != null) {
        character = slide.getCharacterLeft();
        strings.add("<figure>");
        strings.add("<name>" + character.getName() + "</name>");
        strings.add("<appearance>" + character.getGender().toString() + "</appearance>");
        Color skin = character.getSkin();
        Color hair = character.getHairColor();
        Color braid = character.getBraidColor();

        // skin
        if (skin.equals(defaultSkin)) {
          strings.add("<skin>default</skin>");
        } else {
          strings.add("<skin>" + skin.toString() + "</skin>");
        }

        // hair
        if (hair.equals(defaultHair)) {
          strings.add("<hair>default</hair>");
        } else {
          strings.add("<hair>" + hair.toString() + "</hair>");
        }

        // braid
        if (braid.equals(defaultBraid)) {
          strings.add("<braid>default</braid>");
        } else {
          strings.add("<braid>" + braid.toString() + "</braid>");
        }

        strings.add("<facing>" + character.getFacing().toString() + "</facing>");
        strings.add("</figure>");

        // bubble
        if (character.getBubble() != null) {
          strings.add("<balloon status = \"" + character.getBubble().getName() + "\">");
          strings.add("<content>" + character.getText() + "<content>");
        }
      }
      strings.add("</left>");

      // Right Character
      strings.add("<right>");
      if (slide.getCharacterRight() != null) {
        character = slide.getCharacterRight();
        strings.add("<figure>");
        strings.add("<name>" + character.getName() + "</name>");
        strings.add("<appearance>" + character.getGender().toString() + "</appearance>");
        Color skin = character.getSkin();
        Color hair = character.getHairColor();
        Color braid = character.getBraidColor();

        // skin
        if (skin.equals(defaultSkin)) {
          strings.add("<skin>default</skin>");
        } else {
          strings.add("<skin>" + skin.toString() + "</skin>");
        }

        // hair
        if (hair.equals(defaultHair)) {
          strings.add("<hair>default</hair>");
        } else {
          strings.add("<hair>" + hair.toString() + "</hair>");
        }

        // braid
        if (braid.equals(defaultBraid)) {
          strings.add("<braid>default</braid>");
        } else {
          strings.add("<braid>" + braid.toString() + "</braid>");
        }

        strings.add("<facing>" + character.getFacing().toString() + "</facing>");
        strings.add("</figure>");

        // bubble
        if (character.getBubble() != null) {
          strings.add("<balloon status = \"" + character.getBubble().getName() + "\">");
          strings.add("<content>" + character.getText() + "<content>");
        }
      }
      strings.add("</right>");

      if (!slide.getBelowNarrativeText().getText().isEmpty()) {
        strings.add("<below>" + slide.getBelowNarrativeText().getText() + "</below>");
      }
      strings.add("</panel>");
    }

    strings.add("</panels>");
    strings.add("</comic>");

    return strings;
  }
}
