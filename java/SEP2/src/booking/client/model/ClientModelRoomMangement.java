package booking.client.model;

import booking.shared.objects.Room;
import booking.shared.objects.RoomType;

import java.util.List;

public interface ClientModelRoomMangement
{
    public void createRoom(String name, RoomType type, int maxComf, int maxSafety, int size, String comment, boolean isDouble, String doubleName) throws ClientModelException;

    public void updateRoom(Room room) throws ClientModelException;

    public void deleteRoom(Room room) throws ClientModelException;

    public List<RoomType> getRoomTypes() throws ClientModelException;
}
