package booking.shared.socketMessages;

import booking.shared.objects.Room;

public class UpdateRoomRequest extends Request
{
    private final Room room;

    public UpdateRoomRequest(Room room)
    {
        this.room = room;
    }

    public Room getRoom()
    {
        return room;
    }

}
