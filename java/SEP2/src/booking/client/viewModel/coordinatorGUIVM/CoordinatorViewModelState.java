package booking.client.viewModel.coordinatorGUIVM;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModelActiveBookings;
import booking.client.model.ClientModelActiveUser;
import booking.client.model.ClientModelException;
import booking.client.model.ClientModelRoomInfo;
import booking.shared.objects.Booking;
import booking.shared.objects.Room;
import booking.shared.objects.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// NOTE: State som deles mellem flere coordinator view models
public class CoordinatorViewModelState
{
    private final ViewHandler viewHandler;
    private final ClientModelActiveBookings activeBookingsModel;
    private final ClientModelActiveUser activeUserModel;
    private final ClientModelRoomInfo roomInfoModel;
    private final ObservableList<Booking> activeBookings;
    private final ObservableList<Room> allRooms;
    private final User activeUser;

    public CoordinatorViewModelState(ViewHandler viewHandler, ClientModelActiveBookings activeBookingsModel, ClientModelActiveUser activeUserModel, ClientModelRoomInfo roomInfoModel)
    {
        this.viewHandler = viewHandler;
        this.activeBookingsModel = activeBookingsModel;
        this.activeUserModel = activeUserModel;
        this.roomInfoModel = roomInfoModel;
        this.activeBookings = FXCollections.observableArrayList();
        this.allRooms = FXCollections.observableArrayList();
        this.activeUser = activeUserModel.getUser();

        refreshActiveBookings();
        refreshAllRooms();
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
            activeBookings.addAll(activeBookingsModel.getActiveBookings());
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
            allRooms.addAll(roomInfoModel.getRooms());
        }
        catch (ClientModelException e)
        {
            viewHandler.showErrorDialog(e.getMessage());
        }
    }

    public User getUser()
    {
        return activeUser;
    }
}
