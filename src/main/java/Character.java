package main.java;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Character {

  String name;
  String gender;
  ImageView image;
  Color hairColor;
  Color braidColor;
  Color skin;
  String text;
  Bubble bubble;

  String facing;

  public Character(String name, ImageView image) {
    this.name = name;
    this.image = image;
    gender = "female";
    facing = "right";
    hairColor = null;
    skin = null;
    braidColor = null;
    text = null;
    bubble = null;
  }

  public Character() {}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getGender() { return gender; }

  public void setGender() { this.gender = gender; }

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

  public void setBraidColor(Color braidColor) {
    this.braidColor = braidColor;
  }

  public Color getBraidColor() {
    return braidColor;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Bubble getBubble() {
    return bubble;
  }

  public void setBubble(Bubble bubble) {
    this.bubble = bubble;
  }

  public String getFacing() { return facing; }

  public void setFacing(String facing) { this.facing = facing; }
}
