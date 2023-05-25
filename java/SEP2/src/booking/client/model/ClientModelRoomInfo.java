package booking.client.model;

import booking.shared.objects.Booking;
import booking.shared.objects.Room;

import java.time.LocalDate;
import java.util.List;

public interface ClientModelRoomInfo
{
    public Room getRoom(String room) throws ClientModelException;
    public List<Room> getRooms() throws ClientModelException;
    public List<Booking> getBookingsForRoom(String roomName, LocalDate start, LocalDate end) throws ClientModelException;
    public boolean isAvailable(Room room) throws ClientModelException;
}
