package main.java;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SavedSlide {

  private int ID;
  private Character characterLeft, characterRight;
  private Label userTextLeft, userTextRight;
  private TextField aboveNarrativeText;
  private TextField belowNarrativeText;
  private Bubble leftBubble, rightBubble;

  public SavedSlide(int ID, Character characterLeft, Character characterRight, TextField aboveNarrativeText, TextField belowNarrativeText) {
    this.ID = ID;
    this.characterLeft = characterLeft;
    this.characterRight = characterRight;
    this.aboveNarrativeText = aboveNarrativeText;
    this.belowNarrativeText = belowNarrativeText;
  }

  public int getID() {
    return ID;
  }

  public void setID(int ID) {
    this.ID = ID;
  }

  public Character getCharacterLeft() {
    return characterLeft;
  }

  public void setCharacterLeft(Character characterLeft) {
    this.characterLeft = characterLeft;
  }

  public Character getCharacterRight() {
    return characterRight;
  }

  public void setCharacterRight(Character characterRight) {
    this.characterRight = characterRight;
  }

  public TextField getAboveNarrativeText() { return aboveNarrativeText; }

  public void setAboveNarrativeText(TextField aboveNarrativeText) {
    this.aboveNarrativeText = aboveNarrativeText;
  }

  public TextField getBelowNarrativeText() {
    return belowNarrativeText;
  }

  public void setBelowNarrativeText(TextField belowNarrativeText) {
    this.belowNarrativeText = belowNarrativeText;
  }
}
