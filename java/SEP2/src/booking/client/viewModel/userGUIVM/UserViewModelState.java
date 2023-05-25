package booking.client.viewModel.userGUIVM;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModel;
import booking.client.model.ClientModelException;
import booking.shared.objects.Booking;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// NOTE(rune): State som deles mellem flere user view models
public class UserViewModelState
{
    private final ViewHandler viewHandler;
    private final ClientModel model;
    private final ObservableList<Booking> activeBookings;

    public UserViewModelState(ViewHandler viewHandler, ClientModel model)
    {
        this.viewHandler = viewHandler;
        this.model = model;
        this.activeBookings = FXCollections.observableArrayList();
    }

    public ObservableList<Booking> getActiveBookings()
    {
        return activeBookings;
    }

    public void refreshActiveBookings()
    {
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
}
