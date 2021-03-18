import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class EditStage extends VBox
{
    public EditStage()
    {
        this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        this.getChildren().add(new NarrativeBox());
        this.getChildren().add(new StageGrid());
        this.getChildren().add(new NarrativeBox());
    }
}
