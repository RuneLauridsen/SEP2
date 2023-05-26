package booking.client.model;

import booking.shared.objects.Booking;
import booking.shared.objects.Room;

import java.time.LocalDate;
import java.util.List;

public interface ClientModelRoomInfo
{
     Room getRoom(String room) throws ClientModelException;
     List<Room> getRooms() throws ClientModelException;
     List<Booking> getBookingsForRoom(String roomName, LocalDate start, LocalDate end) throws ClientModelException;
     boolean isAvailable(Room room) throws ClientModelException;
}
