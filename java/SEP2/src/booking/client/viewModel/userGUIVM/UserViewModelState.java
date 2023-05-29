package booking.client.viewModel.userGUIVM;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModelActiveBookings;
import booking.client.model.ClientModelActiveUser;
import booking.client.model.ClientModelException;
import booking.shared.objects.Booking;
import booking.shared.objects.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// NOTE(rune): State som deles mellem flere user view models
public class UserViewModelState
{
    private final ViewHandler viewHandler;
    private final ClientModelActiveBookings activeBookingsModel;
    private final ClientModelActiveUser activeUserModel;
    private final ObservableList<Booking> activeBookings;
    private final User activeUser;

    public UserViewModelState(ViewHandler viewHandler, ClientModelActiveBookings activeBookingsModel, ClientModelActiveUser activeUserModel)
    {
        this.viewHandler = viewHandler;
        this.activeBookingsModel = activeBookingsModel;
        this.activeUserModel = activeUserModel;
        this.activeBookings = FXCollections.observableArrayList();
        this.activeUser = activeUserModel.getUser();

        refreshActiveBookings();
    }

    public ObservableList<Booking> getActiveBookings()
    {
        return activeBookings;
    }

    public User getUser()
    {
        return activeUser;
    }

    public void refreshActiveBookings()
    {
        activeBookings.clear();

        try
        {
            activeBookings.addAll(activeBookingsModel.getActiveBookings());
        }
        catch (ClientModelException e)
        {
            viewHandler.showErrorDialog(e.getMessage());
        }
    }
}
