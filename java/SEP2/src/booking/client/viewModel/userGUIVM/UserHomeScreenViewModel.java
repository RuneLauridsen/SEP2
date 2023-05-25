package booking.client.viewModel.userGUIVM;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModel;
import booking.client.model.ClientModelActiveBookings;
import booking.client.model.ClientModelException;
import booking.shared.objects.Booking;
import javafx.beans.property.*;
import javafx.collections.ObservableList;

public class UserHomeScreenViewModel
{
    private final StringProperty username;

    private final ViewHandler viewHandler;
    private final ClientModelActiveBookings activeBookingsModel;
    private final UserViewModelState sharedState;
    private final SimpleStringProperty selectedFromSearch;
    private  final BooleanProperty searchDisable;

    public UserHomeScreenViewModel(ViewHandler viewHandler, ClientModelActiveBookings activeBookingsModel, UserViewModelState sharedState)
    {
        this.viewHandler = viewHandler;
        this.activeBookingsModel = activeBookingsModel;
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

    public void ChangeToBooking()
    {
        viewHandler.showUserBookRoom();
    }

    public void ChangeToSearch(String roomName)
    {
        try{
            viewHandler.showRoomInfo(roomName);
        }
        catch (NullPointerException e){
            viewHandler.showErrorDialog("No such Room");
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
