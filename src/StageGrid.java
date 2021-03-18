import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

public class StageGrid extends GridPane
{
    //http://www.java2s.com/Tutorials/Java/JavaFX_How_to/GridPane/Use_GridPane_to_layout_input_form.htm
    public StageGrid()
    {
        TextArea input_1 = new TextArea();
        input_1.setMaxWidth(225);
        TextArea input_2 = new TextArea();
        input_2.setMaxWidth(225);
        Label speechArrow1 = new Label("Speech Arrow");
        Label speechArrow2 = new Label("Speech Arrow");
        Label characterModel1 = new Label("Character Model");
        Label characterModel2 = new Label("Character Model");
        this.add(input_1,0,0);
        this.setHgap(50);
        this.add(input_2,1,0);
        this.add(speechArrow1,0,1);
        this.add(speechArrow2,1,1);
        this.add(characterModel1,0,2);
        this.add(characterModel2,1,2);
    }
}
