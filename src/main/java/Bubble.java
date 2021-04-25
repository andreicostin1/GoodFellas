package main.java;

import javafx.scene.image.ImageView;

public class Bubble {
  private String name;
  private ImageView image;

  public Bubble(String name, ImageView image) {
    this.name = name;
    this.image = image;
  }

  public Bubble() {}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ImageView getImage() {
    return image;
  }

  public void setImage(ImageView image) {
    this.image = image;
  }
}
