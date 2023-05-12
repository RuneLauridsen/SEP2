package booking.server.model;

import booking.core.Room;
import booking.core.User;
import booking.shared.GetAvailableRoomsParameters;

import java.util.List;

public interface ServerModel
{
    // TODO(rune): Password checking
    public User getUser(String username);

    public List<Room> getAvailableRooms(User user, GetAvailableRoomsParameters parameters);
}
