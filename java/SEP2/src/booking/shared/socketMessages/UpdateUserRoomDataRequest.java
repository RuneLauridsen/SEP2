package booking.shared.socketMessages;

import booking.shared.objects.Room;

// NOTE(rune): Opdatere bruger specifik data for et lokale
public class UpdateUserRoomDataRequest extends Request
{
    private final Room room;
    private final String comment;
    private final int color;

    public UpdateUserRoomDataRequest(Room room, String comment, int color)
    {
        this.room = room;
        this.comment = comment;
        this.color = color;
    }

    public Room getRoom()
    {
        return room;
    }

    public String getComment()
    {
        return comment;
    }

    public int getColor()
    {
        return color;
    }
}
