package booking.core;

public class Room
{
    public String roomName;

    public Room(String roomName)
    {
        this.roomName = roomName;
    }

    public String getRoomName()
    {
        return roomName;
    }

    public void setRoomName(String roomName)
    {
        this.roomName = roomName;
    }

    @Override public String toString()
    {
        return roomName;
    }

    // TODO(rune): Equals
}
