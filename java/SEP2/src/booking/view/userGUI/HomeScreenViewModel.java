package booking.view.userGUI;

import booking.ViewHandler;
import booking.core.Booking;
import booking.core.User;
import booking.database.Persistence;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.awt.print.Book;
import java.time.LocalDate;
import java.util.List;

public class HomeScreenViewModel
{
    private final StringProperty username;
    private final ObservableList<Booking> activeBookings;

    private ViewHandler viewHandler;
    private Persistence persistence;
    private User user;

    public HomeScreenViewModel(ViewHandler viewHandler, Persistence persistence)
    {
        this.viewHandler = viewHandler;
        this.persistence = persistence;

        username = new SimpleStringProperty();
        activeBookings = FXCollections.observableArrayList();
    }

    public void setUser(User user)
    {
        this.user = user;
        username.set(user.getName());

        List<Booking> bookings = persistence.getActiveBookings(user, LocalDate.now(), LocalDate.MAX);
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
}
