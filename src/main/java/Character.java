package main.java;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Character {

    String name;
    ImageView image;
    Color hairColor;
    Color braidColor;
    Color skin;
    public Character(String name, ImageView image)
    {
        this.name = name;
        this.image = image;
        hairColor = null;
        skin = null;
        braidColor = null;
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

    public Color getHairColor() {
        return hairColor;
    }

    public void setHairColor(Color hairColor) {
        this.hairColor = hairColor;
    }

    public Color getSkin() {
        return skin;
    }

    public void setSkin(Color skin) {
        this.skin = skin;
    }

    public void setBraidColor(Color braidColor) { this.braidColor = braidColor; }

    public Color getBraidColor() { return braidColor; }
}
