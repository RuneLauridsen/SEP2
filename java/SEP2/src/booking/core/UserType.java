package booking.core;

import java.io.Serializable;
import java.util.List;

public class UserType implements Serializable
{
    private final int id;
    private final String name;
    private final boolean canEditUsers;
    private final boolean canEditRooms;
    private final boolean canEditBookings; // NOTE(rune): Kan ændre andre folks bookinger
    private final int maxBookingCount;
    private final List<RoomType> allowedRoomTypes;

    public UserType(int id, String name, boolean canEditUsers, boolean canEditRooms, boolean canEditBookings, int maxBookingCount, List<RoomType> allowedRoomTypes)
    {
        this.id = id;
        this.name = name;
        this.canEditUsers = canEditUsers;
        this.canEditRooms = canEditRooms;
        this.canEditBookings = canEditBookings;
        this.maxBookingCount = maxBookingCount;
        this.allowedRoomTypes = allowedRoomTypes;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public boolean canEditUsers()
    {
        return canEditUsers;
    }

    public boolean canEditRooms()
    {
        return canEditRooms;
    }

    public boolean canEditBookings()
    {
        return canEditBookings;
    }

    public int getMaxBookingCount()
    {
        return maxBookingCount;
    }

    public List<RoomType> getAllowedRoomTypes()
    {
        return allowedRoomTypes;
    }

    @Override public boolean equals(Object obj)
    {
        if (obj instanceof UserType other)
        {
            return this.id == other.id;
        }
        else
        {
            return false;
        }
    }

    @Override public String toString()
    {
        return name;
    }
}
