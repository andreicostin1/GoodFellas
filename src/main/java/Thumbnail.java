package main.java;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.io.Serializable;

public class Thumbnail implements Serializable {

    private int ID;
    private int width, height;
    private int[][] data;

    public Thumbnail(int ID, int width, int height, Image image) {
        this.ID = ID;
        this.width = width;
        this.height = height;
        setImage(image);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Image getImage() {
        WritableImage img = new WritableImage(width, height);

        PixelWriter w = img.getPixelWriter();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                w.setArgb(i, j, data[i][j]);
            }
        }

        return img;
    }

    public void setImage(Image image) {
        width = ((int) image.getWidth());
        height = ((int) image.getHeight());
        data = new int[width][height];

        PixelReader r = image.getPixelReader();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                data[i][j] = r.getArgb(i, j);
            }
        }
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Thumbnail)) {
            return false;
        }

        Thumbnail that = (Thumbnail) other;

        return this.ID == that.ID;
    }

}
