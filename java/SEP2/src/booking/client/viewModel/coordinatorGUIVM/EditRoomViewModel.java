package booking.client.viewModel.coordinatorGUIVM;

import booking.client.core.ViewHandler;
import booking.client.model.ArgbIntConverter;
import booking.client.model.ClientModel;
import booking.client.model.ClientModelException;
import booking.shared.objects.Room;
import booking.shared.objects.RoomType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EditRoomViewModel
{
    private final ViewHandler viewHandler;
    private final ClientModel model;
    private final Room room;

    public EditRoomViewModel(ViewHandler viewHandler, ClientModel model, Room room)
    {
        this.viewHandler = viewHandler;
        this.model = model;
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

    public ObservableList<String> getColors()
    {
        ObservableList<String> colors = FXCollections.observableArrayList();
        colors.addAll("", "Red", "Blue", "Yellow", "Orange", "Green", "Purple", "Pink", "Mint", "Green", "Gray");
        return colors;
    }

    public void updateRoom(String name, RoomType type, int maxComf, int maxSafety, int size, String comment, boolean isDouble, String doubleName, String personalComment, String color)
    {
        room.setName(name);
        room.setType(type);
        room.setSize(size);
        room.setComfortCapacity(maxComf);
        room.setFireCapacity(maxSafety);
        room.setComment(comment);
        room.setUserComment(personalComment);

        if (color != null && !color.isEmpty())
        {
            int colorInt = switch (color)
                {
                    case "Red" -> ArgbIntConverter.argbToInt(243, 131, 131);
                    case "Blue" -> ArgbIntConverter.argbToInt(130, 137, 243);
                    case "Yellow" -> ArgbIntConverter.argbToInt(250, 250, 100);
                    case "Orange" -> ArgbIntConverter.argbToInt(255, 178, 61);
                    case "Green" -> ArgbIntConverter.argbToInt(141, 238, 127);
                    case "Purple" -> ArgbIntConverter.argbToInt(214, 142, 236);
                    case "Pink" -> ArgbIntConverter.argbToInt(255, 134, 211);
                    case "Mint" -> ArgbIntConverter.argbToInt(162, 255, 255);
                    case "Gray", default -> ArgbIntConverter.argbToInt(222, 222, 222);
                };
            room.setUserColor(colorInt);
        }

        try
        {
            model.updateRoom(room);
        }
        catch (ClientModelException e)
        {
            viewHandler.showErrorDialog(e.getMessage());
        }
    }

    //Kunne være gjort smartere hvis vi brugte de preset farver i Color klassen
    public String getRoomColor()
    {
        return switch (room.getUserColor())
            {
                case -818301 -> "Red";
                case -8222221 -> "Blue";
                case -329116 -> "Yellow";
                case -19907 -> "Orange";
                case -7475585 -> "Green";
                case -2715924 -> "Purple";
                case -31021 -> "Pink";
                case -6094849 -> "Mint";
                case -2171170, default -> "Gray";
            };

    }

    public boolean deleteRoom()
    {
        if (viewHandler.showOkCancelDialog("Confirm delete", "You're about to delete this room, are you sure?"))
        {
            try
            {
                model.deleteRoom(room);
            }
            catch (ClientModelException e)
            {
                viewHandler.showErrorDialog(e.getMessage());
            }
        }

        return false;
    }
}
