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

    public AddRoomViewModel(ViewHandler viewHandler, ClientModelRoomMangement roomMangementModel, CoordinatorViewModelState sharedState)
    {
        this.viewHandler = viewHandler;
        this.roomManagementModel = roomMangementModel;
        this.sharedState = sharedState;
    }

    public void createRoom(String name, RoomType type, int maxComf, int maxSafety, int size, String comment, boolean isDouble, String doubleName)
    {
        try
        {
            roomManagementModel.createRoom(name, type, maxComf, maxSafety, size, comment, isDouble, doubleName);
            sharedState.refreshAllRooms();
        }
        catch (ClientModelException e)
        {
            viewHandler.showErrorDialog(e.getMessage());
        }
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
