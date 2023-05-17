package booking.client.view.CoordinatorGUI;

import booking.client.core.ViewHandler;
import booking.client.model.ClientModel;
import booking.shared.objects.RoomType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AddRoomViewModel
{
    private final ViewHandler viewHandler;
    private final ClientModel model;

    public AddRoomViewModel(ViewHandler viewHandler, ClientModel model)
    {
        this.viewHandler = viewHandler;
        this.model = model;
    }

    public void createRoom(String name, RoomType type, int maxComf, int maxSafety, int size, String comment, boolean isDouble, String doubleName)
    {
        model.createRoom(name, type, maxComf, maxSafety, size, comment, isDouble, doubleName);
    }

    public ObservableList<RoomType> getRoomTypes()
    {
        ObservableList<RoomType> types = FXCollections.observableArrayList(model.getRoomTypes());
        return types;
    }
}
