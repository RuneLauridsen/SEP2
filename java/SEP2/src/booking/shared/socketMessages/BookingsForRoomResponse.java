package booking.shared.socketMessages;

import booking.shared.objects.Booking;

import java.util.List;

public class BookingsForRoomResponse extends Response
{
    private final List<Booking> bookings;

    public BookingsForRoomResponse(List<Booking> bookings)
    {
        this.bookings = bookings;
    }

    public List<Booking> getBookings()
    {
        return bookings;
    }
}
