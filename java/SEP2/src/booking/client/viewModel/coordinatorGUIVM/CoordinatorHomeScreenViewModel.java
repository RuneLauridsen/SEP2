package booking.client.viewModel.coordinatorGUIVM;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModelException;
import booking.client.model.ClientModelRoomInfo;
import booking.shared.objects.Room;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class CoordinatorHomeScreenViewModel
{
    private final StringProperty username;
    private final ViewHandler viewHandler;
    private final ClientModelRoomInfo roomInfoModel;
    private final CoordinatorViewModelState sharedState;

    public CoordinatorHomeScreenViewModel(ViewHandler viewHandler, ClientModelRoomInfo roomInfoModel, CoordinatorViewModelState sharedState)
    {

        username = new SimpleStringProperty();
        username.set(sharedState.getUser().getName());
        this.viewHandler = viewHandler;
        this.roomInfoModel = roomInfoModel;
        this.sharedState = sharedState;
    }

    public StringProperty usernameProperty()
    {
        return username;
    }

    public ObservableList<Room> getAllRooms()
    {
        return sharedState.getAllRooms();
    }

    public void changeToAddRoom()
    {
        viewHandler.showAddRoom();
    }

    public String isAvailable(Room room)
    {
        try
        {
            if (roomInfoModel.isAvailable(room))
            {
                return "Available";
            }
            else
            {
                return "Occupied";
            }
        }
        catch (ClientModelException e)
        {
            viewHandler.showErrorDialog(e.getMessage());
            return "";
        }
    }

    public void changeToSearch(Room room)
    {
        viewHandler.showRoomInfo(room);
    }

    public void changeToEditRoom(Room room)
    {
        viewHandler.showEditRoom(room);
    }

    public void changeToBooking()
    {
        viewHandler.showCoordinatorBookingMenu();
    }
}
