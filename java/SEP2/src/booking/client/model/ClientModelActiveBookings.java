package booking.client.model;

import booking.shared.objects.Booking;

import java.util.List;

public interface ClientModelActiveBookings
{
     List<Booking> getActiveBookings() throws ClientModelException;

     void deleteBooking(Booking booking) throws ClientModelException;
}
