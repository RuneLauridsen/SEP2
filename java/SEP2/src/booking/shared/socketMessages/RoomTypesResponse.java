package booking.shared.socketMessages;

import booking.shared.objects.RoomType;

import java.util.List;

public class RoomTypesResponse extends Response
{
    private final List<RoomType> roomTypes;

    public RoomTypesResponse(List<RoomType> roomTypes)
    {
        this.roomTypes = roomTypes;
    }

    public List<RoomType> getRoomTypes()
    {
        return roomTypes;
    }
}
