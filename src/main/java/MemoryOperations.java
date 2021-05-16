package main.java;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MemoryOperations {

  private Map<Thumbnail, SavedSlide> slidesMap = new HashMap<>();
  private int id = 1;

  public ArrayList<SavedSlide> getSavedSlides(ListView<Thumbnail> listView) {
    ArrayList<SavedSlide> slideList = new ArrayList<>();
    for (int i = 1; i < listView.getItems().size(); i++) {
      slideList.add(slidesMap.get(listView.getItems().get(i)));
    }
    return slideList;
  }

  public void setSavedSlides(ArrayList<SavedSlide> slides, ListView<Thumbnail> listView) {
    clear(listView);
    id = 1;
    for (SavedSlide slide : slides) {
      Thumbnail thumbnail =
          new Thumbnail(
              slide.getID(),
              240,
              120,
              getImage(slide.getCharacterLeft(), slide.getCharacterRight()));
      listView.getItems().add(thumbnail);
      listView.setOrientation(Orientation.HORIZONTAL);
      slidesMap.put(thumbnail, slide);
      id++;
    }
  }

  public void save(
      Character left,
      Character right,
      ListView<Thumbnail> listView2,
      String aboveNarrativeText,
      String belowNarrativeText) {
    if (left == null || right == null) {
      throw new IllegalArgumentException("Needs two characters in frame");
    }
    SavedSlide slide = new SavedSlide(id, left, right, aboveNarrativeText, belowNarrativeText);
    Thumbnail thumbnail = new Thumbnail(id, 240, 120, getImage(left, right));
    listView2.getItems().add(thumbnail);
    listView2.setOrientation(Orientation.HORIZONTAL);
    slidesMap.put(thumbnail, slide);
    id++;
  }

  public Image getImage(Character left, Character right) {
    BufferedImage bufferedImage = new BufferedImage(240, 120, 1);
    Graphics g = bufferedImage.getGraphics();
    BufferedImage leftImage = SwingFXUtils.fromFXImage(left.getImage().getImage(), null);
    BufferedImage rightImage = SwingFXUtils.fromFXImage(right.getImage().getImage(), null);

    if (left.getScale() == -1) {
      g.drawImage(leftImage, 120, 0, -120, 120, null);
    } else {
      g.drawImage(leftImage, 0, 0, 120, 120, null);
    }

    if (right.getScale() == -1) {
      g.drawImage(rightImage, 240, 0, -120, 120, null);
    } else {
      g.drawImage(rightImage, 120, 0, 120, 120, null);
    }

    return SwingFXUtils.toFXImage(bufferedImage, null);
  }

  public void update(
      Character left,
      Character right,
      String aboveNarrativeText,
      String belowNarrativeText,
      ListView<Thumbnail> listView,
      int index) {
    if (left == null || right == null) {
      throw new IllegalArgumentException("Needs two characters in frame");
    }

    Thumbnail oldThumbnail = listView.getItems().get(index);
    Thumbnail newThumbnail = new Thumbnail(oldThumbnail.getID(), 240, 120, getImage(left, right));
    SavedSlide oldSlide = slidesMap.get(oldThumbnail);
    SavedSlide newSlide =
        new SavedSlide(oldThumbnail.getID(), left, right, aboveNarrativeText, belowNarrativeText);
    listView.getItems().set(index, newThumbnail);
    listView.setOrientation(Orientation.HORIZONTAL);
    slidesMap.remove(oldThumbnail, oldSlide);
    slidesMap.put(newThumbnail, newSlide);
  }

  public SavedSlide load(
      HBox leftDisplay,
      HBox rightDisplay,
      int index,
      ListView<Thumbnail> listView,
      HBox speechBubbleLeft,
      HBox speechBubbleRight,
      TextField textLeft,
      TextField textRight,
      Label upperNarrative,
      Label lowerNarrative,
      Label leftSpeech,
      Label rightSpeech) {
    if (slidesMap.isEmpty()) {
      throw new IllegalArgumentException("Please add a slide before trying to load it");
    }
    Thumbnail thumbnail = listView.getItems().get(index);
    SavedSlide slideToLoad = slidesMap.get(thumbnail);

    if (slideToLoad == null) {
      throw new IllegalArgumentException("Slide could not be found");
    }

    leftDisplay.getChildren().clear();
    rightDisplay.getChildren().clear();
    speechBubbleLeft.getChildren().clear();
    speechBubbleRight.getChildren().clear();

    ImageView left = new ImageView(slideToLoad.getCharacterLeft().getImage().getImage());
    ImageView right = new ImageView(slideToLoad.getCharacterRight().getImage().getImage());

    left.setFitHeight(210);
    left.setFitWidth(210);
    left.setScaleX(slideToLoad.getCharacterLeft().getScale());
    leftDisplay.getChildren().add(left);
    slideToLoad.getCharacterLeft().setImage(left);

    if (slideToLoad.getCharacterLeft().getBubble() != null) {
      speechBubbleLeft.getChildren().add(slideToLoad.getCharacterLeft().getBubble().getImage());
    }
    if (slideToLoad.getCharacterLeft().getText() != null) {
      textLeft.setText(slideToLoad.getCharacterLeft().getText());
    }

    right.setFitHeight(210);
    right.setFitWidth(210);
    right.setScaleX(slideToLoad.getCharacterRight().getScale());
    rightDisplay.getChildren().add(right);
    slideToLoad.getCharacterRight().setImage(right);

    if (slideToLoad.getAboveNarrativeText() != null) {
      upperNarrative.setText(slideToLoad.getAboveNarrativeText());
    }
    if (slideToLoad.getBelowNarrativeText() != null) {
      lowerNarrative.setText(slideToLoad.getBelowNarrativeText());
    }

    if (slideToLoad.getCharacterRight().getBubble() != null) {
      speechBubbleRight.getChildren().add(slideToLoad.getCharacterRight().getBubble().getImage());
    }
    if (slideToLoad.getCharacterRight().getText() != null) {
      textRight.setText(slideToLoad.getCharacterRight().getText());
    }

    leftSpeech.setText("     " + slideToLoad.getCharacterLeft().getText());
    rightSpeech.setText("     " + slideToLoad.getCharacterRight().getText());

    return slideToLoad;
  }

  public void delete(int index, ListView<Thumbnail> listView) {
    if (listView.getItems().size() == 1) {
      throw new IllegalArgumentException("Please add a slide before deleting");
    }
    Thumbnail thumbnail = listView.getItems().get(index);
    SavedSlide slide = slidesMap.get(thumbnail);
    listView.getItems().remove(index);
    slidesMap.remove(thumbnail, slide);
  }

  public void clear(ListView<Thumbnail> listView) {
    listView.getItems().remove(1, listView.getItems().size());
    slidesMap.clear();
  }

  public boolean isEmpty() {
    return slidesMap.isEmpty();
  }
}
