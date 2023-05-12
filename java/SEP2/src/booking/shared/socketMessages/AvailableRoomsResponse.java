package booking.shared.socketMessages;

import booking.core.Room;

import java.util.List;

public class AvailableRoomsResponse extends Response
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
