package booking.shared.socketMessages;

import booking.core.Room;

import java.io.Serializable;
import java.util.List;

public class AvailableRoomsResponse implements Serializable
{
    private List<Room> rooms;

    public AvailableRoomsResponse(List<Room> rooms)
    {
        this.rooms = rooms;
    }

    public List<Room> getRooms()
    {
        return rooms;
    }

    public void setRooms(List<Room> rooms)
    {
        this.rooms = rooms;
    }
}
