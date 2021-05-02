package main.java;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Character {

  public enum Gender {
    MALE,
    FEMALE
  }

  public enum Facing {
    LEFT,
    RIGHT
  }

  private String name;
  private ImageView image;
  private Color hairColor;
  private Color braidColor;
  private Color skin;
  private String text;
  private Bubble bubble;
  private int scale;
  private Facing facing;
  private Gender gender;

  public Character(String name, ImageView image) {
    this.name = name;
    this.image = image;
    gender = Gender.FEMALE;
    scale = 1;
    setFacing();
  }

  public Character() {
    gender = Gender.FEMALE;
    scale = 1;
    setFacing();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender() {
    if (gender == Gender.FEMALE) {
      gender = Gender.MALE;
    } else {
      gender = Gender.FEMALE;
    }
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

  public int getScale() {
    return scale;
  }

  public void setScale() {
    scale *= -1;
    image.setScaleX(scale);
    setFacing();
  }

  public Facing getFacing() {
    return facing;
  }

  public void setFacing() {
    if (scale == 1) {
      facing = Facing.LEFT;
    } else {
      facing = Facing.RIGHT;
    }
  }
}
