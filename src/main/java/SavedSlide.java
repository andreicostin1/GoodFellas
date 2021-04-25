package main.java;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SavedSlide {

  private int ID;
  private Character characterLeft, characterRight;
  private Label userTextLeft, userTextRight;
  private TextField narrativeText;
  private Bubble leftBubble, rightBubble;

  public SavedSlide(int ID, Character characterLeft, Character characterRight, Label userTextLeft, Label userTextRight, TextField narrativeText, Bubble leftBubble, Bubble rightBubble) {
    this.ID = ID;
    this.characterLeft = characterLeft;
    this.characterRight = characterRight;
    this.userTextLeft = userTextLeft;
    this.userTextRight = userTextRight;
    this.narrativeText = narrativeText;
    this.leftBubble = leftBubble;
    this.rightBubble = rightBubble;
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

  public Label getUserTextLeft() {
    return userTextLeft;
  }

  public void setUserTextLeft(Label userTextLeft) {
    this.userTextLeft = userTextLeft;
  }

  public Label getUserTextRight() {
    return userTextRight;
  }

  public void setUserTextRight(Label userTextRight) {
    this.userTextRight = userTextRight;
  }

  public TextField getNarrativeText() {
    return narrativeText;
  }

  public void setNarrativeText(TextField narrativeText) {
    this.narrativeText = narrativeText;
  }

  public Bubble getLeftBubble() {
    return leftBubble;
  }

  public void setLeftBubble(Bubble leftBubble) {
    this.leftBubble = leftBubble;
  }

  public Bubble getRightBubble() {
    return rightBubble;
  }

  public void setRightBubble(Bubble rightBubble) {
    this.rightBubble = rightBubble;
  }
}
