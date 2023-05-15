package booking.shared.socketMessages;

import booking.shared.objects.Booking;

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
