package booking.view.userGUI;

import booking.ViewHandler;
import booking.core.Booking;
import booking.core.User;
import booking.database.Persistence;
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
    private Persistence persistence;
    private User user;
    private final ObjectProperty<String> selctedFromSearch;

    public UserHomeScreenViewModel(ViewHandler viewHandler, Persistence persistence)
    {
        this.viewHandler = viewHandler;
        this.persistence = persistence;

        username = new SimpleStringProperty();
        selctedFromSearch = new SimpleObjectProperty<>();
        activeBookings = FXCollections.observableArrayList();
    }

    public void setUser(User user)
    {
        this.user = user;
        username.set(user.getName());

        List<Booking> bookings = persistence.getBookingsForUser(user, LocalDate.now(), LocalDate.MAX);
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
        viewHandler.showUserBookRoom(user);
    }

    public void ChangeToSearch()
    {
        viewHandler.showRoomInfo(persistence.getRoom(selctedFromSearch.get()));
    }
}
