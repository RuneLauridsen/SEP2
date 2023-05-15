package booking.shared.socketMessages;

import booking.shared.objects.Booking;

import java.util.List;

public class ActiveBookingsResponse extends Response
{
    private List<Booking> bookings;

    public ActiveBookingsResponse(List<Booking> bookings)
    {
        this.bookings = bookings;
    }

    public List<Booking> getBookings()
    {
        return bookings;
    }
}
