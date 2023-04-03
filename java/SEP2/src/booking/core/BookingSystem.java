package booking.core;

import java.awt.print.Book;
import java.time.LocalDateTime;
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

    public void addRoom(Room room)
    {
        roomList.add(room);
    }

    public void addBooking(Booking booking)
    {
        bookingList.add(booking);
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

    public static boolean checkOverlap(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2)
    {
        if (start2.isAfter(start1) && start2.isBefore(end1))
        {
            return true;
        }

        if (end2.isAfter(start1) && end2.isBefore(end1))
        {
            return true;
        }

        if (start2.isBefore(start1) && (end2.isAfter(start1) || end2.equals(start1)))
        {
            return true;
        }

        /*
                       ⬇ start1 10            ⬇ end1 16
            ------------------------------------------------------
                   ⬆ start2 09                     ⬆ end2 19
         */

        return false;
    }

}
