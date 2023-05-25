package booking.client.viewModel.coordinatorGUIVM;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModel;
import booking.client.model.ClientModelException;
import booking.shared.objects.RoomType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AddRoomViewModel
{
    private final ViewHandler viewHandler;
    private final ClientModel model;
    private final CoordinatorViewModelState sharedState;

    public AddRoomViewModel(ViewHandler viewHandler, ClientModel model, CoordinatorViewModelState sharedState)
    {
        this.viewHandler = viewHandler;
        this.model = model;
        this.sharedState = sharedState;
    }

    public void createRoom(String name, RoomType type, int maxComf, int maxSafety, int size, String comment, boolean isDouble, String doubleName)
    {
        try
        {
            model.createRoom(name, type, maxComf, maxSafety, size, comment, isDouble, doubleName);
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
            return FXCollections.observableArrayList(model.getRoomTypes());
        }
        catch (ClientModelException e)
        {
            viewHandler.showErrorDialog(e.getMessage());
            return FXCollections.observableArrayList();
        }
    }
}
