package booking.view.chooseDate;

import booking.App;
import booking.TestData;
import booking.core.BookingInterval;
import booking.core.BookingSystem;
import booking.core.Room;
import booking.view.roomList.RoomListView;
import booking.view.roomList.RoomListViewModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ChooseDateViewModel
{
    private final ObjectProperty<LocalDate> selectedDate;
    private final ObjectProperty<Integer> selectedFromHour;
    private final ObjectProperty<Integer> selectedToHour;

    private final BookingSystem model;

    public ChooseDateViewModel()
    {
        model = new TestData().getTestData();

        selectedDate = new SimpleObjectProperty<>();
        selectedFromHour = new SimpleObjectProperty<>();
        selectedToHour = new SimpleObjectProperty<>();
    }

    public ObjectProperty<LocalDate> selectedDateProperty()
    {
        return selectedDate;
    }

    public ObjectProperty<Integer> selectedFromHourProperty()
    {
        return selectedFromHour;
    }

    public ObjectProperty<Integer> selectedToHourProperty()
    {
        return selectedToHour;
    }

    private boolean isInputValid()
    {
        return selectedDate.get() != null
            && selectedToHour.get() != null
            && selectedFromHour.get() != null
            && selectedFromHour.get() < selectedToHour.get();
    }

    public void showRoomsCommand()
    {
        if (isInputValid() == false)
        {
            // TODO(rune): Popup box?
            return;
        }

        LocalTime fromTime = LocalTime.of(selectedFromHour.get(), 0, 0);
        LocalTime toTime = LocalTime.of(selectedToHour.get(), 0, 0);
        LocalDate date = selectedDate.get();

        BookingInterval interval = new BookingInterval(date, fromTime, toTime);
        List<Room> rooms = model.getAvailableRooms(interval);

        // TODO(rune): Move to ViewHandler
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view/roomList/RoomListView.fxml"));
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
