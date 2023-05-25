package booking.client.viewModel.userGUIVM;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModel;
import booking.client.model.ClientModelActiveBookings;
import booking.client.model.ClientModelException;
import booking.shared.objects.Booking;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class UserHomeScreenViewModel
{
    private final StringProperty username;

    private final ViewHandler viewHandler;
    private final ClientModelActiveBookings activeBookingsModel;
    private final UserViewModelState sharedState;
    private final ObjectProperty<String> selectedFromSearch;

    public UserHomeScreenViewModel(ViewHandler viewHandler, ClientModelActiveBookings activeBookingsModel, UserViewModelState sharedState)
    {
        this.viewHandler = viewHandler;
        this.activeBookingsModel = activeBookingsModel;
        this.sharedState = sharedState;

        username = new SimpleStringProperty();
        selectedFromSearch = new SimpleObjectProperty<>();

        username.set(sharedState.getUser().getName());
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
        return selectedFromSearch;
    }

    public void ChangeToBooking()
    {
        viewHandler.showUserBookRoom();
    }

    public void ChangeToSearch(String roomName)
    {
        viewHandler.showRoomInfo(roomName);
    }

    public void cancelBooking(Booking booking)
    {
        try
        {
            activeBookingsModel.deleteBooking(booking);
            sharedState.refreshActiveBookings();
        }
        catch (ClientModelException e)
        {
            viewHandler.showErrorDialog(e.getMessage());
        }
    }
}
