package booking.client.viewModel.userGUIVM;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModel;
import booking.client.model.ClientModelException;
import booking.shared.objects.Booking;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserHomeScreenViewModel
{
    private final StringProperty username;

    // TODO(rune): MVVM -> Må view godt kende til Booking klassen?
    // Det synes jeg godt, men måske er Micheal ikke enig.
    private final ObservableList<Booking> activeBookings;

    private final ViewHandler viewHandler;
    private final ClientModel model;
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

        activeBookings.clear();

        try
        {
            activeBookings.addAll(model.getActiveBookings());
        }
        catch (ClientModelException e)
        {
            viewHandler.showErrorDialog(e.getMessage());
        }
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
        try
        {
            viewHandler.showRoomInfo(model.getRoom(roomName));
        }
        catch (ClientModelException e)
        {
            viewHandler.showErrorDialog(e.getMessage());
        }
    }

    public void cancelBooking(Booking booking)
    {
        try
        {
            model.deleteBooking(booking);
            refreshActiveBookings();
        }
        catch (ClientModelException e)
        {
            viewHandler.showErrorDialog(e.getMessage());
        }
    }
}
