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

    public boolean updateRoom(String name, RoomType type, String maxComf, String maxSafety, String size, String comment, boolean isDouble, String doubleName, String personalComment, PredefinedColor color)
    {
        if (name.isEmpty() || maxComf.isEmpty() || maxSafety.isEmpty() || size.isEmpty() || type == null)
        {
            viewHandler.showErrorDialog("Name, room type, max comfort, max safety or size must not be empty");
            return false;
        }
        try
        {
            if (!room.getName().equals(name) && roomManagementModel.getRoom(name) != null)
            {
                viewHandler.showErrorDialog("Room name already in use");
                return false;
            }

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

            room.setName(name);
            room.setType(type);
            room.setSize(sizeInt);
            room.setComfortCapacity(maxComfInt);
            room.setFireCapacity(maxSafetyInt);
            room.setComment(comment);
            room.setUserComment(personalComment);

            if (color != null)
            {
                room.setUserColor(color.getArgb());
            }

            roomManagementModel.updateRoom(room);
            sharedState.refreshAllRooms();
            return true;
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
