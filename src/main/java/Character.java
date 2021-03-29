package main.java;

import javafx.scene.image.ImageView;

public class Character {

    String name;
    ImageView image;

    public Character(String name, ImageView image){

        this.name = name;
        this.image = image;
    }

    public Character() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView character) {
        this.image = character;
    }
}
