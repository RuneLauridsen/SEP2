package booking.shared.objects;

import java.io.Serializable;

public class Booking implements Serializable
{
    private final int id;
    private final BookingInterval interval;
    private final Room room;
    private final User user;

    public Booking(int id, BookingInterval interval, Room room, User user)
    {
        this.id = id;
        this.interval = interval;
        this.user = user;
        this.room = room;
    }

    public int getId()
    {
        return id;
    }

    public BookingInterval getInterval()
    {
        return interval;
    }

    public User getUser()
    {
        return user;
    }

    public Room getRoom()
    {
        return room;
    }

    @Override public String toString()
    {
        return user + " " + room + " " + interval;
    }
}
