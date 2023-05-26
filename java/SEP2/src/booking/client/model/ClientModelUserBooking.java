package booking.client.model;

import booking.shared.CreateBookingParameters;
import booking.shared.GetAvailableRoomsParameters;
import booking.shared.objects.Room;

import java.util.List;

public interface ClientModelUserBooking
{
     List<Room> getAvailableRooms(GetAvailableRoomsParameters parameters) throws ClientModelException;
     void createBooking(CreateBookingParameters parameters) throws ClientModelOverlapException, ClientModelException;
}
