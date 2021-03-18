import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class MenuB extends MenuBar
{
    public MenuB()
    {
        //http://tutorials.jenkov.com/javafx/menubar.html
        Menu tab1 = new Menu("File");
        Menu tab2 = new Menu("View");
        Menu tab3 = new Menu("Configure");
        this.getMenus().addAll(tab1,tab2,tab3);
    }

}
