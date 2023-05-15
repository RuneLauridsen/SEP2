package booking.client.view.userGUI;

import booking.shared.objects.Room;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

import java.util.function.Consumer;

public class RoomListCell extends ListCell<Room>
{
    private final Label label;
    private final Button button;
    private final HBox hbox;

    public RoomListCell(String buttonText, Consumer<Room> onRoomClicked)
    {
        super();

        label = new Label();
        button = new Button(buttonText);
        button.setOnAction(event -> onRoomClicked.accept(getItem()));

        hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(button, label);

        setGraphic(hbox);
    }

    @Override protected void updateItem(Room item, boolean empty)
    {
        super.updateItem(item, empty);

        if (empty || item == null)
        {
            label.setText("");
            setGraphic(null);
        }
        else
        {
            label.setText(item.toString());
            setGraphic(hbox);
        }
    }
}
