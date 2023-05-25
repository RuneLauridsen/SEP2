package booking.client.viewModel.coordinatorGUIVM;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModel;
import booking.client.model.ClientModelException;
import booking.client.viewModel.sharedVM.PredefinedColor;
import booking.shared.objects.Room;
import booking.shared.objects.RoomType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EditRoomViewModel
{
    private final ViewHandler viewHandler;
    private final ClientModel model;
    private final CoordinatorViewModelState sharedState;
    private final Room room;

    public EditRoomViewModel(ViewHandler viewHandler, ClientModel model, CoordinatorViewModelState sharedState, Room room)
    {
        this.viewHandler = viewHandler;
        this.model = model;
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
            return FXCollections.observableArrayList(model.getRoomTypes());
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
        if (name.isEmpty() || maxComf.isEmpty() || maxSafety.isEmpty() ||size.isEmpty() || type == null)
            viewHandler.showErrorDialog("Name, room type, max comfort, max safety or size must not be empty");
        else {
            try
            {
                if (room.getName().equals(name) || model.getRoom(name) == null)
                {
                    room.setName(name);
                    room.setType(type);
                    room.setSize(Integer.parseInt(size));
                    room.setComfortCapacity(Integer.parseInt(maxComf));
                    room.setFireCapacity(Integer.parseInt(maxSafety));
                    room.setComment(comment);
                    room.setUserComment(personalComment);

                    if (color != null)
                    {
                        room.setUserColor(color.getArgb());
                    }

                    model.updateRoom(room);
                    sharedState.refreshAllRooms();
                    return true;
                }
                else
                    viewHandler.showErrorDialog("Room name already in use");
            }

            catch(ClientModelException e)
            {
                viewHandler.showErrorDialog(e.getMessage());
            }
            catch(NumberFormatException e){
                    viewHandler.showErrorDialog( "Max capacity and size must be numbers");
            }
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
                model.deleteRoom(room);
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
