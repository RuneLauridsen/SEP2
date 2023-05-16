package booking.shared.socketMessages;

import booking.shared.objects.BookingInterval;
import booking.shared.objects.Room;
import booking.shared.objects.UserGroup;

public class CreateBookingRequest extends Request
{
    private final Room room;
    private final BookingInterval interval;
    private final UserGroup group;
    private final boolean isOverlapAllowed;

    public CreateBookingRequest(Room room, BookingInterval interval, boolean isOverlapAllowed)
    {
        this.room = room;
        this.interval = interval;
        this.isOverlapAllowed = isOverlapAllowed;
        this.group = null;
    }

    public CreateBookingRequest(Room room, BookingInterval interval, boolean isOverlapAllowed, UserGroup group)
    {
        this.room = room;
        this.interval = interval;
        this.isOverlapAllowed = isOverlapAllowed;
        this.group = group;
    }

    public Room getRoom()
    {
        return room;
    }

    public BookingInterval getInterval()
    {
        return interval;
    }

    public boolean isOverlapAllowed()
    {
        return isOverlapAllowed;
    }

    public UserGroup getGroup()
    {
        return group;
    }
}
