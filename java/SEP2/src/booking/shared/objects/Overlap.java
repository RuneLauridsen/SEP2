package booking.shared.objects;

import java.io.Serializable;
import java.util.List;

public class Overlap implements Serializable
{
    private final Booking booking;
    private final List<User> users;

    public Overlap(Booking booking, List<User> users)
    {
        this.booking = booking;
        this.users = users;
    }

    public Booking getBooking()
    {
        return booking;
    }

    public List<User> getUsers()
    {
        return users;
    }

    @Override public String toString()
    {
        return "Overlap " + booking + " ( " + users.size() + " users)";
    }
}
