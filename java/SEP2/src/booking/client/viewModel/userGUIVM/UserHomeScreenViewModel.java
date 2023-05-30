package booking.client.viewModel.userGUIVM;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModelActiveBookings;
import booking.client.model.ClientModelException;
import booking.client.model.ClientModelRoomInfo;
import booking.shared.objects.Booking;
import booking.shared.objects.Room;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class UserHomeScreenViewModel
{
    private final StringProperty username;

    private final ViewHandler viewHandler;
    private final ClientModelActiveBookings activeBookingsModel;
    private final ClientModelRoomInfo roomInfoModel;
    private final UserViewModelState sharedState;
    private final SimpleStringProperty selectedFromSearch;
    private  final BooleanProperty searchDisable;

    public UserHomeScreenViewModel(ViewHandler viewHandler, ClientModelActiveBookings activeBookingsModel, ClientModelRoomInfo roomInfoModel, UserViewModelState sharedState)
    {
        this.viewHandler = viewHandler;
        this.activeBookingsModel = activeBookingsModel;
        this.roomInfoModel = roomInfoModel;
        this.sharedState = sharedState;


        username = new SimpleStringProperty();
        selectedFromSearch = new SimpleStringProperty();
        searchDisable = new SimpleBooleanProperty();
        searchDisable.bind(selectedFromSearch.isEmpty());

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

    public SimpleStringProperty getSearchProperty()
    {
        return selectedFromSearch;
    }

    public void changeToBooking()
    {
        viewHandler.showUserBookRoom();
    }

    public void changeToSearch()
    {
        try
        {
            Room room = roomInfoModel.getRoom(selectedFromSearch.get().toUpperCase());
            if(room != null)
            {
                viewHandler.showRoomInfo(room);
            }
            else
            {
                viewHandler.showErrorDialog("No such room");
            }
        }
        catch (ClientModelException e)
        {
            viewHandler.showErrorDialog(e.getMessage());
        }
    }

    public void showInfo(Room room)
    {
        if(room != null)
        {
            viewHandler.showRoomInfo(room);
        }
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

    public BooleanProperty searchDisable() {
        return searchDisable;
    }
}
