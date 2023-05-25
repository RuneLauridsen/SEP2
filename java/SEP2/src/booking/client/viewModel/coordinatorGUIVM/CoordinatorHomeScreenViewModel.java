package booking.client.viewModel.coordinatorGUIVM;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModel;
import booking.client.model.ClientModelException;
import booking.shared.objects.Room;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CoordinatorHomeScreenViewModel
{
    private final StringProperty username;
    private final ViewHandler viewHandler;
    private final ClientModel model;
    private final CoordinatorViewModelState sharedState;

    public CoordinatorHomeScreenViewModel(ViewHandler viewHandler, ClientModel model, CoordinatorViewModelState sharedState)
    {
        username = new SimpleStringProperty();
        username.set(model.getUser().getName());
        this.viewHandler = viewHandler;
        this.model = model;
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
            if (model.isAvailable(room))
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

    public void changeToSearch(String name)
    {
        try
        {
            viewHandler.showRoomInfo(model.getRoom(name));
        }
        catch (ClientModelException e)
        {
            viewHandler.showErrorDialog(e.getMessage());
        }
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
