package main.java;

import javafx.geometry.Orientation;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class MemoryOperations {

  GridPane savedSlide;
  private ArrayList<SavedSlide> slideArrayList = new ArrayList<>();
  private int id = 0;

  public void save(Character left, Character right, GridPane display, ListView<GridPane> listView) {
    if (left == null  || right == null) {
      throw new IllegalArgumentException("Needs two characters in frame");
    }
    savedSlide = generateSlide(left, right);

    savedSlide.setPrefSize(225, 225);
    savedSlide.setGridLinesVisible(true);

    listView.getItems().add(savedSlide);
    listView.setOrientation(Orientation.HORIZONTAL);

    // display.getChildren().clear();

    id++;
  }

  public void load(HBox leftDisplay, HBox rightDisplay, int index, ListView<GridPane> listView) {
    leftDisplay.getChildren().clear();
    rightDisplay.getChildren().clear();

    SavedSlide slideToLoad = slideArrayList.get(index);

    slideToLoad.getCharacterLeft().getImage().setFitHeight(150);
    slideToLoad.getCharacterLeft().getImage().setFitWidth(150);
    leftDisplay.getChildren().add(slideToLoad.getCharacterLeft().getImage());

    slideToLoad.getCharacterRight().getImage().setFitHeight(150);
    slideToLoad.getCharacterRight().getImage().setFitWidth(150);
    rightDisplay.getChildren().add(slideToLoad.getCharacterRight().getImage());

    listView.getItems().remove(index);
    id--;
  }

  public void delete(int index, ListView<GridPane> listView) {
    listView.getItems().remove(index);
    id--;
  }

  public GridPane generateSlide(Character left, Character right) {
    GridPane generatedSlide = new GridPane();

    SavedSlide slide = new SavedSlide(id, left, right);
    slideArrayList.add(slide);

    ImageView leftImage = slide.getCharacterLeft().getImage();
    ImageView rightImage = slide.getCharacterRight().getImage();

    leftImage.setFitHeight(110);
    leftImage.setFitWidth(110);
    rightImage.setFitWidth(110);
    rightImage.setFitHeight(110);

    generatedSlide.add(leftImage, 0, 0);
    generatedSlide.add(rightImage, 1, 0);

    return generatedSlide;
  }
}
