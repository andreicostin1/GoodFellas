package main.java;

import javafx.scene.image.*;
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
    text = "";
    hairColor = new Color(249 / 255.0, 255 / 255.0, 0 / 255.0, 1);
    skin = new Color(255 / 255.0, 232 / 255.0, 216 / 255.0, 1);
    braidColor = new Color(240 / 255.0, 255 / 255.0, 0 / 255.0, 1);
  }

  public Character() {
    gender = Gender.FEMALE;
    scale = 1;
    setFacing();
    hairColor = new Color(249 / 255.0, 255 / 255.0, 0 / 255.0, 1);
    skin = new Color(255 / 255.0, 232 / 255.0, 216 / 255.0, 1);
    braidColor = new Color(240 / 255.0, 255 / 255.0, 0 / 255.0, 1);
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
    updateImageGender();
  }

  public void updateImageGender() {
    Image oldCharacterImage = image.getImage();
    int width = (int) oldCharacterImage.getWidth();
    int height = (int) oldCharacterImage.getHeight();

    WritableImage newCharacterImage = new WritableImage(width, height);
    PixelReader reader = oldCharacterImage.getPixelReader();
    PixelWriter writer = newCharacterImage.getPixelWriter();

    Color hideBraidColor = Color.rgb(253, 253, 253);
    Color hideBowColor = Color.rgb(254, 254, 254);

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        Color currColor = reader.getColor(j, i);

        if (currColor.equals(Color.RED)) { //hide lips
          double newGreen = (skin.getGreen() * 255) + 1;
          Color hideLipColor = new Color(255 / 255.0, newGreen / 255.0, 216 / 255.0, 1);
          writer.setColor(j, i, hideLipColor);
        }
        else if (currColor.equals(new Color(255 / 255.0, ((skin.getGreen() * 255) + 1) / 255.0, 216 / 255.0, 1))) { //show lips
          writer.setColor(j, i, Color.RED);
        }
        else if (currColor.equals(Color.rgb(236, 180, 181))) { //hide bow
          writer.setColor(j, i, hideBowColor);
        }
        else if (currColor.equals(hideBowColor)) { //show bow
          writer.setColor(j, i, Color.rgb(236, 180, 181));
        }
        else if (currColor.equals(braidColor)) { //hide braid
          writer.setColor(j, i, hideBraidColor);
        }
        else if (currColor.equals(hideBraidColor)) { //show braid
          writer.setColor(j, i, braidColor);
        }
        else {
          writer.setColor(j, i, currColor);
        }
      }
    }

    image = new ImageView(newCharacterImage);
    image.setFitHeight(150);
    image.setFitWidth(150);
    image.setScaleX(scale);

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
    updateImageHairColor(hairColor);
    this.hairColor = hairColor;
  }

  public void updateImageHairColor(Color newHairColor) {
    Color interpolateWithWhite = newHairColor.interpolate(Color.WHITE, 0.1);
    int r = (int) Math.round(interpolateWithWhite.getRed() * 255);
    int g = (int) Math.round(interpolateWithWhite.getGreen() * 255);
    int b = (int) Math.round(interpolateWithWhite.getBlue() * 255);
    Color newBraidColor = Color.rgb(r, g, b);

    Image oldCharacterImage = image.getImage();
    int width = (int) oldCharacterImage.getWidth();
    int height = (int) oldCharacterImage.getHeight();

    WritableImage newCharacterImage = new WritableImage(width, height);
    PixelReader reader = oldCharacterImage.getPixelReader();
    PixelWriter writer = newCharacterImage.getPixelWriter();

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        Color currColor = reader.getColor(j, i);
        if (currColor.equals(hairColor)) {
          writer.setColor(j, i, newHairColor);
        } else if (currColor.equals(braidColor)) {
          writer.setColor(j, i, newBraidColor);
        } else {
          writer.setColor(j, i, currColor);
        }
      }
    }

    braidColor = newBraidColor;

    image = new ImageView(newCharacterImage);
    image.setFitHeight(150);
    image.setFitWidth(150);
    image.setScaleX(scale);
  }

  public Color getSkin() {
    return skin;
  }

  public void setSkin(Color skin) {
    updateImageSkin(skin);
    this.skin = skin;
  }

  public void updateImageSkin(Color newSkinColor) {
    Image oldCharacterImage = image.getImage();
    int width = (int) oldCharacterImage.getWidth();
    int height = (int) oldCharacterImage.getHeight();

    WritableImage newCharacterImage = new WritableImage(width, height);
    PixelReader reader = oldCharacterImage.getPixelReader();
    PixelWriter writer = newCharacterImage.getPixelWriter();

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        Color currColor = reader.getColor(j, i);
        if (currColor.equals(skin)) {
          writer.setColor(j, i, newSkinColor);
        } else {
          writer.setColor(j, i, currColor);
        }
      }
    }

    image = new ImageView(newCharacterImage);
    image.setFitHeight(150);
    image.setFitWidth(150);
    image.setScaleX(scale);
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
