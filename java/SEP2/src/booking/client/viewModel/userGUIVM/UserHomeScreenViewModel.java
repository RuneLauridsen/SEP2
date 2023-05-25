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

    private final ViewHandler viewHandler;
    private final ClientModel model;
    private final UserViewModelState sharedState;
    private final ObjectProperty<String> selctedFromSearch;

    public UserHomeScreenViewModel(ViewHandler viewHandler, ClientModel model, UserViewModelState sharedState)
    {
        this.viewHandler = viewHandler;
        this.model = model;
        this.sharedState = sharedState;

        username = new SimpleStringProperty();
        selctedFromSearch = new SimpleObjectProperty<>();

        username.set(model.getUser().getName());
    }

    public StringProperty usernameProperty()
    {
        return username;
    }

    public ObservableList<Booking> getActiveBookings()
    {
        return sharedState.getActiveBookings();
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
            sharedState.refreshActiveBookings();
        }
        catch (ClientModelException e)
        {
            viewHandler.showErrorDialog(e.getMessage());
        }
    }
}
