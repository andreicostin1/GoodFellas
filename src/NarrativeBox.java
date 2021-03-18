import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;


public class NarrativeBox extends HBox
{
    public NarrativeBox()
    {
        TextArea textArea = new TextArea("Box for Narration");
        textArea.setMaxHeight(100);
        textArea.setMinWidth(500);
        this.getChildren().add(textArea);
    }
}
