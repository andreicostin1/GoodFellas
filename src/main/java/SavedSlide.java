package main.java;

public class SavedSlide {

  private int ID;
  private Character characterLeft, characterRight;
  private String userTextLeft, userTextRight;
  private String aboveNarrativeText;
  private String belowNarrativeText;
  private Bubble leftBubble, rightBubble;

  public SavedSlide(
      int ID,
      Character characterLeft,
      Character characterRight,
      String aboveNarrativeText,
      String belowNarrativeText) {
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

  public String getAboveNarrativeText() {
    return aboveNarrativeText;
  }

  public void setAboveNarrativeText(String aboveNarrativeText) {
    this.aboveNarrativeText = aboveNarrativeText;
  }

  public String getBelowNarrativeText() {
    return belowNarrativeText;
  }

  public void setBelowNarrativeText(String belowNarrativeText) {
    this.belowNarrativeText = belowNarrativeText;
  }
}
