package booking.client.viewModel.coordinatorGUIVM;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModelException;
import booking.client.model.ClientModelRoomMangement;
import booking.shared.objects.RoomType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AddRoomViewModel
{
    private final ViewHandler viewHandler;
    private final ClientModelRoomMangement roomManagementModel;
    private final CoordinatorViewModelState sharedState;

    public AddRoomViewModel(ViewHandler viewHandler, ClientModelRoomMangement roomManagementModel, CoordinatorViewModelState sharedState)
    {
        this.viewHandler = viewHandler;
        this.roomManagementModel = roomManagementModel;
        this.sharedState = sharedState;
    }

    public boolean createRoom(String name, RoomType type, String maxComf, String maxSafety, String size, String comment, boolean isDouble, String doubleName)
    {
        if (name.isEmpty() || maxComf.isEmpty() || maxSafety.isEmpty() || size.isEmpty() || type == null)
        {
            viewHandler.showErrorDialog("Name, room type, max comfort, max safety or size must not be empty");
            return false;
        }

        try
        {
            if (roomManagementModel.getRoom(name) == null)
            {
                int maxComfInt = Integer.parseInt(maxComf);
                int maxSafetyInt = Integer.parseInt(maxSafety);
                int sizeInt = Integer.parseInt(size);

                if(maxComfInt < 0)
                {
                    viewHandler.showErrorDialog("Comfort capacity must not be negative");
                    return false;
                }

                if(maxSafetyInt < 0)
                {
                    viewHandler.showErrorDialog("Safety capacity must not be negative");
                    return false;
                }

                if(sizeInt < 0)
                {
                    viewHandler.showErrorDialog("Size must not be negative");
                    return false;
                }

                roomManagementModel.createRoom(name, type, Integer.parseInt(maxComf), Integer.parseInt(maxSafety), Integer.parseInt(size), comment, isDouble, doubleName);
                sharedState.refreshAllRooms();
                return true;
            }
            else
                viewHandler.showErrorDialog("Room with this name is already taken");
        }

        catch (ClientModelException e)
        {
            viewHandler.showErrorDialog(e.getMessage());
        }
        catch (NumberFormatException e)
        {
            viewHandler.showErrorDialog("Max capacity and size must be numbers");
        }

        return false;
    }

    public ObservableList<RoomType> getRoomTypes()
    {
        try
        {
            return FXCollections.observableArrayList(roomManagementModel.getRoomTypes());
        }
        catch (ClientModelException e)
        {
            viewHandler.showErrorDialog(e.getMessage());
            return FXCollections.observableArrayList();
        }
    }
}
