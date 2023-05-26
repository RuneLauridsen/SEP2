package booking.client.model;

import booking.shared.objects.Room;
import booking.shared.objects.RoomType;

import java.util.List;

public interface ClientModelRoomMangement
{
     Room getRoom(String room) throws ClientModelException;

     void createRoom(String name, RoomType type, int maxComf, int maxSafety, int size, String comment, boolean isDouble, String doubleName) throws ClientModelException;

     void updateRoom(Room room) throws ClientModelException;

     void deleteRoom(Room room) throws ClientModelException;

     List<RoomType> getRoomTypes() throws ClientModelException;
}
