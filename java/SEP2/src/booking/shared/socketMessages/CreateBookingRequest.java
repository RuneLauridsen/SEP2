package booking.shared.socketMessages;

import booking.core.BookingInterval;
import booking.core.Room;

public class CreateBookingRequest extends Request
{
    private final Room room;
    private final BookingInterval interval;

    public CreateBookingRequest(Room room, BookingInterval interval)
    {
        this.room = room;
        this.interval = interval;
    }

    public Room getRoom()
    {
        return room;
    }

    public BookingInterval getInterval()
    {
        return interval;
    }
}
