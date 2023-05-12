package booking.server.model;

import booking.core.Room;
import booking.core.User;
import booking.database.Persistence;
import booking.shared.GetAvailableRoomsParameters;

import java.util.List;

public class ServerModelImpl implements ServerModel
{
    private final Persistence persistence;

    public ServerModelImpl(Persistence persistence)
    {
        this.persistence = persistence;
    }

    @Override public User getUser(String username)
    {
        return persistence.getUser(username);
    }

    @Override public List<Room> getAvailableRooms(User user, GetAvailableRoomsParameters parameters)
    {
        return persistence.getAvailableRooms(
            user,
            parameters.getInterval(),
            parameters.getMinCapacity(),
            parameters.getMaxCapacity(),
            parameters.getBuilding(),
            parameters.getFloor()
        );
    }
}
