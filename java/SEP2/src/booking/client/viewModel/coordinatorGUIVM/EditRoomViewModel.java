package booking.client.viewModel.coordinatorGUIVM;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModelException;
import booking.client.model.ClientModelRoomMangement;
import booking.client.viewModel.sharedVM.PredefinedColor;
import booking.shared.objects.Room;
import booking.shared.objects.RoomType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EditRoomViewModel
{
    private final ViewHandler viewHandler;
    private final ClientModelRoomMangement roomManagementModel;
    private final CoordinatorViewModelState sharedState;
    private final Room room;

    public EditRoomViewModel(ViewHandler viewHandler, ClientModelRoomMangement model, CoordinatorViewModelState sharedState, Room room)
    {
        this.viewHandler = viewHandler;
        this.roomManagementModel = model;
        this.sharedState = sharedState;
        this.room = room;
    }

    public Room getRoom()
    {
        return room;
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

    public ObservableList<PredefinedColor> getColors()
    {
        ObservableList<PredefinedColor> colors = FXCollections.observableArrayList();
        colors.add(null);
        colors.addAll(PredefinedColor.getPredefinedColors());
        return colors;
    }

    public void updateRoom(String name, RoomType type, int maxComf, int maxSafety, int size, String comment, boolean isDouble, String doubleName, String personalComment, PredefinedColor color)
    {
        room.setName(name);
        room.setType(type);
        room.setSize(size);
        room.setComfortCapacity(maxComf);
        room.setFireCapacity(maxSafety);
        room.setComment(comment);
        room.setUserComment(personalComment);

        if (color != null)
        {
            room.setUserColor(color.getArgb());
        }

        try
        {
            roomManagementModel.updateRoom(room);
            sharedState.refreshAllRooms();
        }
        catch (ClientModelException e)
        {
            viewHandler.showErrorDialog(e.getMessage());
        }
    }

    //Kunne være gjort smartere hvis vi brugte de preset farver i Color klassen
    public PredefinedColor getRoomColor()
    {
        return PredefinedColor.getPredefinedColorByArgb(room.getUserColor());
    }

    public boolean deleteRoom()
    {
        if (viewHandler.showOkCancelDialog("Confirm delete", "You're about to delete this room, are you sure?"))
        {
            try
            {
                roomManagementModel.deleteRoom(room);
                sharedState.refreshAllRooms();
            }
            catch (ClientModelException e)
            {
                viewHandler.showErrorDialog(e.getMessage());
            }
        }

        return false;
    }
}
