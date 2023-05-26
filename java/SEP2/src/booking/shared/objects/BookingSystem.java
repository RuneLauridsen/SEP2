package booking.shared.objects;

import java.util.ArrayList;
import java.util.List;

// TODO(rune): Better name
public class BookingSystem
{
    public final List<Booking> bookingList;
    public final List<Room> roomList;

    public BookingSystem()
    {
        bookingList = new ArrayList<>();
        roomList = new ArrayList<>();
    }


    public List<Room> getAvailableRooms(BookingInterval interval)
    {
        // TODO(rune): Very inefficient, should probably just use DB query instead?

        List<Room> availableRooms = new ArrayList<>();

        for (Room room : roomList)
        {
            if (!hasBookingAt(room, interval))
            {
                availableRooms.add(room);
            }
        }

        return availableRooms;
    }

    public boolean hasBookingAt(Room room, BookingInterval interval)
    {
        for (Booking booking : bookingList)
        {
            if (booking.getRoom().equals(room))
            {
                if (booking.getInterval().isOverlapWith(interval))
                {
                    return true;
                }
            }
        }

        return false;
    }

}
