package main.java;

import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class MemoryOperations {

  GridPane savedSlide;
  private ArrayList<SavedSlide> slideArrayList = new ArrayList<>();
  private int id = 0;

  public void save(Character left, Character right, HBox leftDisplay, HBox rightDisplay, ListView<GridPane> listView, Label leftLabel, Label rightLabel, TextField narrativeText, Bubble leftBubble, Bubble rightBubble) {
    if (left == null || right == null) {
      throw new IllegalArgumentException("Needs two characters in frame");
    }
    savedSlide = generateSlide(left, right, leftDisplay, rightDisplay, leftLabel, rightLabel, narrativeText, leftBubble, rightBubble);

    savedSlide.setPrefSize(225, 225);
    savedSlide.setGridLinesVisible(true);

    listView.getItems().add(savedSlide);
    listView.setOrientation(Orientation.HORIZONTAL);

    id++;
  }

  public void load(GridPane display, HBox leftDisplay, HBox rightDisplay, int index, ListView<GridPane> listView) {
    leftDisplay.getChildren().clear();
    rightDisplay.getChildren().clear();

    SavedSlide slideToLoad = slideArrayList.get(index);

    slideToLoad.getCharacterLeft().getImage().setFitHeight(150);
    slideToLoad.getCharacterLeft().getImage().setFitWidth(150);
    leftDisplay.getChildren().add(slideToLoad.getCharacterLeft().getImage());
    display.add(slideToLoad.getLeftBubble().getImage(), 0, 2);

    slideToLoad.getCharacterRight().getImage().setFitHeight(150);
    slideToLoad.getCharacterRight().getImage().setFitWidth(150);
    rightDisplay.getChildren().add(slideToLoad.getCharacterRight().getImage());
  }

  public void delete(int index, ListView<GridPane> listView) {
    listView.getItems().remove(index);
    id--;
  }

  public GridPane generateSlide(Character left, Character right, HBox leftDisplay, HBox rightDisplay, Label leftLabel, Label rightLabel, TextField narrativeText, Bubble leftBubble, Bubble rightBubble) {
    GridPane generatedSlide = new GridPane();

    SavedSlide slide = new SavedSlide(id, left, right, leftLabel, rightLabel, narrativeText, leftBubble, rightBubble);
    slideArrayList.add(slide);

    ImageView leftImage = slide.getCharacterLeft().getImage();
    ImageView rightImage = slide.getCharacterRight().getImage();

    leftImage.setFitHeight(110);
    leftImage.setFitWidth(110);
    rightImage.setFitWidth(110);
    rightImage.setFitHeight(110);

    if (leftDisplay.getScaleX() == -1) {
      leftImage.setScaleX(-1);
    } else {
      leftImage.setScaleX(1);
    }

    if (rightDisplay.getScaleX() == -1) {
      rightImage.setScaleX(-1);
    } else {
      rightImage.setScaleX(1);
    }

    generatedSlide.add(leftImage, 0, 0);
    generatedSlide.add(rightImage, 1, 0);

    return generatedSlide;
  }
}
