package booking.client.view.shared;

import booking.client.model.ArgbIntConverter;
import booking.shared.objects.Room;
import javafx.geometry.Insets;
import javafx.scene.control.TableRow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class ColoredRoomTableRow extends TableRow<Room>
{
    @Override
    protected void updateItem(Room room, boolean empty)
    {
        super.updateItem(room, empty);

        if (room != null && room.getUserColor() != 0 && !empty)
        {
            Color color = ArgbIntConverter.intToColor(room.getUserColor());
            setBackground(new Background(new BackgroundFill(color,
                                                            CornerRadii.EMPTY, Insets.EMPTY)));
        }
        else
        {
            setBackground(null);
        }
    }
}
