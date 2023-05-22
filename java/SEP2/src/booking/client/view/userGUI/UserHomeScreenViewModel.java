package booking.client.view.userGUI;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModel;
import booking.shared.objects.Booking;
import booking.shared.objects.Room;
import booking.shared.objects.User;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;

public class UserHomeScreenViewModel
{
    private final StringProperty username;

    // TODO(rune): MVVM -> Må view godt kende til Booking klassen?
    // Det synes jeg godt, men måske er Micheal ikke enig.
    private final ObservableList<Booking> activeBookings;

    private ViewHandler viewHandler;
    private ClientModel model;
    private final ObjectProperty<String> selctedFromSearch;

    public UserHomeScreenViewModel(ViewHandler viewHandler, ClientModel model)
    {
        this.viewHandler = viewHandler;
        this.model = model;

        username = new SimpleStringProperty();
        selctedFromSearch = new SimpleObjectProperty<>();
        activeBookings = FXCollections.observableArrayList();
    }

    public void refreshActiveBookings()
    {
        username.set(model.getUser().getName());

        List<Booking> bookings = model.getActiveBookings();
        activeBookings.clear();
        activeBookings.addAll(bookings);
    }

    public StringProperty usernameProperty()
    {
        return username;
    }

    public ObservableList<Booking> getActiveBookings()
    {
        return activeBookings;
    }

    public ObjectProperty<String> getSearchProperty()
    {
        return selctedFromSearch;
    }

    public void ChangeToBooking()
    {
        viewHandler.showUserBookRoom();
    }

    public void ChangeToSearch(String roomName)
    {
        viewHandler.showRoomInfo(model.getRoom(roomName));
    }

    public void cancelBooking(Booking booking)
    {
        model.deleteBooking(booking);
        refreshActiveBookings();
    }
}
