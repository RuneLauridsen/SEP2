package booking.shared.socketMessages;

import booking.core.Booking;

public class DeleteBookingRequest extends Request
{
    private final Booking booking;

    public DeleteBookingRequest(Booking booking)
    {
        this.booking = booking;
    }

    public Booking getBooking()
    {
        return booking;
    }
}
