package booking.shared.socketMessages;

import booking.shared.objects.Booking;

import java.util.List;

public class BookingsForUserResponse extends Response
{
    private final List<Booking> bookings;

    public BookingsForUserResponse(List<Booking> bookings)
    {
        this.bookings = bookings;
    }

    public List<Booking> getBookings()
    {
        return bookings;
    }
}
