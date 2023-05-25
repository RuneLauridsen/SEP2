package booking.client.viewModel.coordinatorGUIVM;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModel;
import booking.client.model.ClientModelException;
import booking.shared.objects.Booking;
import booking.shared.objects.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// NOTE(rune): State som deles mellem flere coordinator view models
public class CoordinatorViewModelState
{
    private final ViewHandler viewHandler;
    private final ClientModel model;
    private final ObservableList<Booking> activeBookings;
    private final ObservableList<Room> allRooms;

    public CoordinatorViewModelState(ViewHandler viewHandler, ClientModel model)
    {
        this.viewHandler = viewHandler;
        this.model = model;
        this.activeBookings = FXCollections.observableArrayList();
        this.allRooms = FXCollections.observableArrayList();
    }

    public ObservableList<Booking> getActiveBookings()
    {
        return activeBookings;
    }

    public ObservableList<Room> getAllRooms()
    {
        return allRooms;
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

    public void refreshAllRooms()
    {
        allRooms.clear();

        try
        {
            allRooms.addAll(model.getRooms());
        }
        catch (ClientModelException e)
        {
            viewHandler.showErrorDialog(e.getMessage());
        }
    }
}
