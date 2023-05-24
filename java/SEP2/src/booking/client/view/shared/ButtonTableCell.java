package booking.client.view.shared;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

import java.util.function.Consumer;

public class ButtonTableCell<T, S> extends TableCell<T, S>
{
    private final Button button;

    public ButtonTableCell(String buttonText, Consumer<S> onRoomClicked)
    {
        super();

        button = new Button(buttonText);
        button.setOnAction(event -> onRoomClicked.accept(getItem()));

        setGraphic(button);
    }

    @Override protected void updateItem(S item, boolean empty)
    {
        super.updateItem(item, empty);

        if (empty)
        {
            setGraphic(null);
            setText(null);
        }
        else
        {
            setGraphic(button);
            setText(null);
        }
    }
}
