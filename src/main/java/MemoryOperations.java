package main.java;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MemoryOperations {

  GridPane savedSlide;
  private ArrayList<SavedSlide> slideArrayList = new ArrayList<>();
  private int id = 0;

  public ArrayList<SavedSlide> getSavedSlides() {
    return slideArrayList;
  }

  public void setSavedSlides(ArrayList<SavedSlide> slides, ListView<GridPane> listView) {
    slideArrayList = slides;
    id = 0;
    listView.getItems().remove(1, listView.getItems().size());
    for(SavedSlide slide : slideArrayList) {
      savedSlide = generateSlide(slide, slide.getCharacterLeft(), slide.getCharacterRight(),
              slide.getAboveNarrativeText(), slide.getBelowNarrativeText());
      savedSlide.setPrefSize(225, 125);
      savedSlide.setGridLinesVisible(true);
      listView.getItems().add(savedSlide);
      listView.setOrientation(Orientation.HORIZONTAL);
      id++;
    }
  }

  public void save(
      Character left,
      Character right,
      ListView<GridPane> listView,
      String aboveNarrativeText,
      String belowNarrativeText) {
    if (left == null || right == null) {
      throw new IllegalArgumentException("Needs two characters in frame");
    }
    SavedSlide slide = new SavedSlide(id, left, right, aboveNarrativeText, belowNarrativeText);
    slideArrayList.add(slide);

    savedSlide = generateSlide(slide, left, right, aboveNarrativeText, belowNarrativeText);
    savedSlide.setPrefSize(225, 125);
    savedSlide.setGridLinesVisible(true);
    listView.getItems().add(savedSlide);
    listView.setOrientation(Orientation.HORIZONTAL);
    id++;
  }

  public void update(Character left, Character right, String aboveNarrativeText, String belowNarrativeText,
                     ListView<GridPane> listView, int index) {
    if (left == null || right == null) {
      throw new IllegalArgumentException("Needs two characters in frame");
    }
    SavedSlide oldSlide = slideArrayList.get(index);
    SavedSlide slide = new SavedSlide(oldSlide.getID(), left, right, aboveNarrativeText, belowNarrativeText);
    slideArrayList.set(index, slide);

    savedSlide = generateSlide(slide, left, right, aboveNarrativeText, belowNarrativeText);
    savedSlide.setPrefSize(225, 125);
    savedSlide.setGridLinesVisible(true);
    listView.getItems().set(index+1, savedSlide);
    listView.setOrientation(Orientation.HORIZONTAL);
  }

  public SavedSlide load(
          HBox leftDisplay,
          HBox rightDisplay,
          int index,
          HBox speechBubbleLeft,
          HBox speechBubbleRight,
          TextField textLeft,
          TextField textRight,
          Label upperNarrative,
          Label lowerNarrative,
          Label leftSpeech,
          Label rightSpeech) {
    if (slideArrayList.size() == 0) {
      throw new IllegalArgumentException("Please add a slide before trying to load it");
    }

    leftDisplay.getChildren().clear();
    rightDisplay.getChildren().clear();
    speechBubbleLeft.getChildren().clear();
    speechBubbleRight.getChildren().clear();

    SavedSlide slideToLoad = slideArrayList.get(index);

    ImageView left = new ImageView(slideToLoad.getCharacterLeft().getImage().getImage());
    ImageView right = new ImageView(slideToLoad.getCharacterRight().getImage().getImage());

    left.setFitHeight(150);
    left.setFitWidth(150);
    left.setScaleX(slideToLoad.getCharacterLeft().getScale());
    leftDisplay.getChildren().add(left);
    slideToLoad.getCharacterLeft().setImage(left);

    if (slideToLoad.getCharacterLeft().getBubble() != null) {
      speechBubbleLeft.getChildren().add(slideToLoad.getCharacterLeft().getBubble().getImage());
    }
    if (slideToLoad.getCharacterLeft().getText() != null) {
      textLeft.setText(slideToLoad.getCharacterLeft().getText());
    }


    right.setFitHeight(150);
    right.setFitWidth(150);
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

  public void delete(int index, ListView<GridPane> listView) {
    if (listView.getItems().size() == 1) {
      throw new IllegalArgumentException("Please add a slide before deleting");
    }
    listView.getItems().remove(index+1);
    slideArrayList.remove(index);
    id--;
  }

  public void clear(ListView<GridPane> listView) {
    listView.getItems().remove(1, listView.getItems().size());
    slideArrayList.clear();
  }

  public boolean isEmpty() {
    return slideArrayList.isEmpty();
  }

  public GridPane generateSlide(SavedSlide slide, Character left, Character right, String aboveNarrativeText, String belowNarrativeText) {
    GridPane generatedSlide = new GridPane();

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

  public ArrayList<BufferedImage> toImages() throws IOException {
    ArrayList<BufferedImage> images = new ArrayList<>();
    BufferedImage endPane = ImageIO.read(this.getClass().getResource("/main/resources/images/end.png"));

    for(SavedSlide slide : slideArrayList) {
      BufferedImage pane = ImageIO.read(this.getClass().getResource("/main/resources/images/pane.png"));

      Graphics g = pane.getGraphics();
      ImageView left = slide.getCharacterLeft().getImage();
      ImageView right = slide.getCharacterRight().getImage();


      if(slide.getCharacterLeft().getScale() == -1) {
        g.drawImage(SwingFXUtils.fromFXImage(left.getImage(), null), 17+375, 342,-375, 375, null);
      }
      else {
        g.drawImage(SwingFXUtils.fromFXImage(left.getImage(), null), 17, 342,375, 375, null);
      }

      if(slide.getCharacterRight().getScale() == -1) {
        g.drawImage(SwingFXUtils.fromFXImage(right.getImage(), null), 402+375, 342, -375, 375, null);
      } else {
        g.drawImage(SwingFXUtils.fromFXImage(right.getImage(), null), 402, 342, 375, 375, null);
      }

      g.setColor(java.awt.Color.BLACK);

      //Narrative
      Font font = new Font("TimesRoman", Font.PLAIN, 25);
      g.setFont(font);
      FontMetrics metrics = g.getFontMetrics(font);
      int x = 5 + (790 - metrics.stringWidth(slide.getBelowNarrativeText())) / 2;
      g.drawString(slide.getAboveNarrativeText(), x, 50);
      x = 5 + (790 - metrics.stringWidth(slide.getBelowNarrativeText())) / 2;
      g.drawString(slide.getBelowNarrativeText(), x, 770);

      font = new Font("TimesRoman", Font.BOLD, 25);
      g.setFont(font);
      metrics = g.getFontMetrics(font);
      BufferedImage bubble;
      int length;
      int splitIndex;
      String firstLine;
      String secondLine;
      //Speech bubbles
      if(slide.getCharacterLeft().getBubble() != null) {
        bubble = ImageIO.read(this.getClass().getResource("/main/resources/images/"+slide.getCharacterLeft().getBubble().getName()+".png"));
        g.drawImage(bubble, 30, 135, null);
        if(metrics.stringWidth(slide.getCharacterLeft().getText()) <= 295) {//if text fits on 1 line
          g.drawString(slide.getCharacterLeft().getText(), 200-(metrics.stringWidth(slide.getCharacterLeft().getText())/2), 250);
        }
        else {
          length = slide.getCharacterLeft().getText().length();
          splitIndex = slide.getCharacterLeft().getText().indexOf(" ", length/2);

          if(splitIndex == -1) {
            firstLine = slide.getCharacterLeft().getText().substring(0, length/2);
            secondLine = slide.getCharacterLeft().getText().substring((length/2), length);
          }
          else {
            firstLine = slide.getCharacterLeft().getText().substring(0, splitIndex);
            secondLine = slide.getCharacterLeft().getText().substring(splitIndex+1, length);
          }

          g.drawString(firstLine, 200-(metrics.stringWidth(firstLine)/2), 250-metrics.getMaxDescent());
          g.drawString(secondLine, 200-(metrics.stringWidth(secondLine)/2), 250+metrics.getMaxAscent());
        }
      }

      if(slide.getCharacterRight().getBubble() != null) {
        bubble = ImageIO.read(this.getClass().getResource("/main/resources/images/"+slide.getCharacterRight().getBubble().getName()+".png"));
        g.drawImage(bubble, 435, 135, null);
        if(metrics.stringWidth(slide.getCharacterRight().getText()) <= 295) {//if text fits on 1 line
          g.drawString(slide.getCharacterRight().getText(), 603-(metrics.stringWidth(slide.getCharacterRight().getText())/2), 250);
        }
        else {
          length = slide.getCharacterRight().getText().length();
          splitIndex = slide.getCharacterRight().getText().indexOf(" ", length/2);

          if(splitIndex == -1) {
            firstLine = slide.getCharacterRight().getText().substring(0, length/2);
            secondLine = slide.getCharacterRight().getText().substring((length/2), length);
          }
          else {
            firstLine = slide.getCharacterRight().getText().substring(0, splitIndex);
            secondLine = slide.getCharacterRight().getText().substring(splitIndex+1, length);
          }

          g.drawString(firstLine, 603-(metrics.stringWidth(firstLine)/2), 250-metrics.getMaxDescent());
          g.drawString(secondLine, 603-(metrics.stringWidth(secondLine)/2), 250+metrics.getMaxAscent());
        }
      }

      images.add(pane);
    }
    images.add(endPane);

    return images;
  }
}
