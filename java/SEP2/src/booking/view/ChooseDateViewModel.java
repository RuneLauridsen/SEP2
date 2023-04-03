package booking.view;

import booking.App;
import booking.TestData;
import booking.core.BookingInterval;
import booking.core.BookingSystem;
import booking.core.Room;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class ChooseDateViewModel
{
    private final ObjectProperty<BookingInterval> selectedInterval;

    private final BookingSystem model;

    public ChooseDateViewModel()
    {
        model = new TestData().getTestData();

        selectedInterval = new SimpleObjectProperty<>();
    }

    public BookingInterval getSelectedInterval()
    {
        return selectedInterval.get();
    }

    public ObjectProperty<BookingInterval> selectedIntervalProperty()
    {
        return selectedInterval;
    }

    public void showRooms()
    {
        List<Room> rooms = model.getAvailableRooms(selectedInterval.get());

        // TODO(rune): Move to ViewHandler
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view/RoomListView.fxml"));
        Scene scene = null;
        try
        {
            scene = new Scene(fxmlLoader.load(), 309, 238);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        RoomListViewModel viewModel = new RoomListViewModel(rooms);
        RoomListView controller = fxmlLoader.getController();
        controller.init(viewModel);

        Stage stage = new Stage();
        stage.setTitle("Room list");
        stage.setScene(scene);
        stage.show();
    }
}
