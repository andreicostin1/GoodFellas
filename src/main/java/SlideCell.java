package main.java;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.io.*;
import java.util.Base64;

public class SlideCell extends ListCell<Thumbnail> {

  private final ImageView imageView = new ImageView();

  public SlideCell() {
    ListCell<Thumbnail> thisCell = this;

    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    setAlignment(Pos.CENTER);

    setOnDragDetected(
        event -> {
          if (getItem() == null) {
            return;
          }

          if (getItem().getID() == 0) {
            return;
          }

          Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
          ClipboardContent content = new ClipboardContent();

          String savedSlide = "";
          try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(getItem());
            byte[] byteArray = bos.toByteArray();
            savedSlide = Base64.getEncoder().encodeToString(byteArray);
          } catch (Exception e) {
            System.err.println(e);
          }
          content.putString(savedSlide);
          dragboard.setDragView(getItem().getImage());
          dragboard.setContent(content);

          event.consume();
        });

    setOnDragOver(
        event -> {
          if (event.getGestureSource() != thisCell && event.getDragboard().hasString()) {

            if (getItem() != null && getItem().getID() == 0) {
              event.acceptTransferModes(TransferMode.NONE);
            } else {
              event.acceptTransferModes(TransferMode.MOVE);
            }
          }

          event.consume();
        });

    setOnDragEntered(
        event -> {
          if (event.getGestureSource() != thisCell && event.getDragboard().hasString()) {
            setOpacity(0.3);
          }
        });

    setOnDragExited(
        event -> {
          if (event.getGestureSource() != thisCell && event.getDragboard().hasString()) {
            setOpacity(1);
          }
        });

    setOnDragDropped(
        event -> {
          if (getItem() == null) {
            return;
          }

          Dragboard db = event.getDragboard();
          boolean success = false;

          if (db.hasString()) {
            ObservableList<Thumbnail> items = getListView().getItems();

            Thumbnail thumbnail = null;
            try {
              byte[] bytes = Base64.getDecoder().decode(db.getString());
              ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
              ObjectInput in = new ObjectInputStream(bis);
              thumbnail = (Thumbnail) in.readObject();
            } catch (Exception e) {
              System.err.println(e);
            }

            int draggedIdx = items.indexOf(thumbnail);
            int thisIdx = items.indexOf(getItem());

            if (draggedIdx > thisIdx) {
              swap(draggedIdx, thisIdx, -1);
            } else {
              swap(draggedIdx, thisIdx, 1);
            }

            success = true;
          }
          event.setDropCompleted(success);

          event.consume();
        });

    setOnDragDone(DragEvent::consume);
  }

  private void swap(int draggedIndex, int thisIndex, int direction) {
    Thumbnail draggedThumbnail = getListView().getItems().get(draggedIndex);
    Thumbnail nextThumbnail = getListView().getItems().get(draggedIndex + direction);
    getListView().getItems().set(draggedIndex, nextThumbnail);
    getListView().getItems().set(draggedIndex + direction, draggedThumbnail);

    if (draggedIndex + direction != thisIndex) {
      swap(draggedIndex + direction, thisIndex, direction);
    }
  }

  @Override
  protected void updateItem(Thumbnail item, boolean empty) {
    super.updateItem(item, empty);

    if (empty || item == null) {
      setGraphic(null);
    } else {
      imageView.setImage(item.getImage());
      setGraphic(imageView);
    }
  }
}
