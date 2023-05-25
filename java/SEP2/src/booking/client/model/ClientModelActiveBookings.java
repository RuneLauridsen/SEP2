package booking.client.model;

import booking.shared.objects.Booking;

import java.util.List;

public interface ClientModelActiveBookings
{
    public List<Booking> getActiveBookings() throws ClientModelException;

    public void deleteBooking(Booking booking) throws ClientModelException;
}
