package booking.shared.objects;

import java.io.Serializable;

public class Booking implements Serializable
{
    private final int id;
    private final BookingInterval interval;
    private final Room room;
    private final User user;
    private final UserGroup userGroup; // NOTE(rune): Null hvis booking ikke er for et hold/klasse

    public Booking(int id, BookingInterval interval, Room room, User user, UserGroup userGroup)
    {
        this.id = id;
        this.interval = interval;
        this.user = user;
        this.room = room;
        this.userGroup = userGroup;
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

    public UserGroup getUserGroup()
    {
        return userGroup;
    }

    @Override public String toString()
    {
        return user + " " + room + " " + interval;
    }
}
