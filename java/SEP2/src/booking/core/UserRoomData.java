package booking.core;

import java.io.Serializable;

public class UserRoomData implements Serializable
{
    private final User user;
    private final Room room;
    private final int color; // NOTE(rune): ARGB
    private final String comment;

    public UserRoomData(User user, Room room, int color, String comment)
    {
        this.user = user;
        this.room = room;
        this.color = color;
        this.comment = comment;
    }

    public User getUser()
    {
        return user;
    }

    public Room getRoom()
    {
        return room;
    }

    public int getColor()
    {
        return color;
    }

    public String getComment()
    {
        return comment;
    }

    // TODO(rune): Er der brug for en equals metode?
}
