package booking.client.logic;

import booking.client.networking.ClientNetwork;
import booking.client.networking.ClientNetworkException;
import booking.client.networking.ClientResponseException;
import booking.core.Booking;
import booking.core.BookingInterval;
import booking.core.Room;
import booking.core.User;
import booking.shared.GetAvailableRoomsParameters;

import javax.security.auth.login.LoginException;
import java.util.List;

public class ClientModelImpl implements ClientModel
{
    private final ClientNetwork networkLayer;
    private User user;

    public ClientModelImpl(ClientNetwork networkLayer)
    {
        this.networkLayer = networkLayer;
    }

    public User getUser()
    {
        return user;
    }

    @Override public void login(String username, String password)
    {
        try
        {
            user = networkLayer.connect(username, password);
        }
        catch (ClientResponseException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        catch (ClientNetworkException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
    }

    @Override public void logout()
    {
        try
        {
            networkLayer.disconnect();
        }
        catch (ClientResponseException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        catch (ClientNetworkException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
    }

    @Override public List<Room> getAvailableRooms(GetAvailableRoomsParameters parameters)
    {
        try
        {
            return networkLayer.getAvailableRooms(parameters);
        }
        catch (ClientResponseException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        catch (ClientNetworkException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
    }

    @Override public List<Booking> getActiveBookings()
    {
        try
        {
            return networkLayer.getActiveBookings();
        }
        catch (ClientResponseException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        catch (ClientNetworkException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
    }

    @Override public void createBooking(Room room, BookingInterval interval)
    {
        try
        {
            networkLayer.createBooking(room, interval);
        }
        catch (ClientResponseException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
        catch (ClientNetworkException e)
        {
            throw new RuntimeException(e); // TODO(rune): Bedre error handling
        }
    }
}
