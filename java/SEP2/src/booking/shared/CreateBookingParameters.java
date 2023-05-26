package booking.shared;

import booking.shared.objects.BookingInterval;
import booking.shared.objects.Room;
import booking.shared.objects.UserGroup;

import java.io.Serializable;
import java.util.Objects;

public class CreateBookingParameters implements Serializable
{
    private final Room room;
    private final BookingInterval interval;
    private final boolean isOverlapAllowed;
    private final UserGroup userGroup;

    public CreateBookingParameters(Room room, BookingInterval interval, boolean isOverlapAllowed, UserGroup userGroup)
    {
        this.room = Objects.requireNonNull(room);
        this.interval = Objects.requireNonNull(interval);
        this.isOverlapAllowed = isOverlapAllowed;
        this.userGroup = userGroup; // NOTE(rune): Null hvis booking ikk er til et bestemt hold/klasse
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

    public UserGroup getUserGroup()
    {
        return userGroup;
    }
}
