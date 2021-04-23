package main.java;

import javafx.scene.layout.GridPane;

public class SavedSlide {

    private int ID;
    private Character characterLeft, characterRight;

    public  SavedSlide(int ID, Character characterLeft, Character characterRight){
        this.ID = ID;
        this.characterLeft = characterLeft;
        this.characterRight = characterRight;
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
}
