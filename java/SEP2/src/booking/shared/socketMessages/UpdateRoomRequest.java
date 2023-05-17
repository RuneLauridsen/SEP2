package booking.shared.socketMessages;

import booking.shared.UpdateRoomParameters;
import booking.shared.objects.Room;

public class UpdateRoomRequest extends Request
{
    private final Room room;
    private final UpdateRoomParameters parameters;

    public UpdateRoomRequest(Room room, UpdateRoomParameters parameters)
    {
        this.room = room;
        this.parameters = parameters;
    }

    public Room getRoom()
    {
        return room;
    }

    public UpdateRoomParameters getParameters()
    {
        return parameters;
    }
}
