package booking.view.roomList;

import booking.core.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class RoomListViewModel
{
    private final ObservableList<Room> roomList;

    public RoomListViewModel(List<Room> rooms)
    {
        roomList = FXCollections.observableList(rooms);
    }

    public ObservableList<Room> getRoomList()
    {
        return roomList;
    }
}
