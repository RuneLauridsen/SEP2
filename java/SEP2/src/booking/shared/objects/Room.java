package booking.shared.objects;

import java.io.Serializable;

public class Room implements Serializable
{
    private final int id;
    private final String name;
    private final int size;
    private final int comfortCapacity;
    private final int fireCapacity;
    private final String comment;
    private final RoomType type;

    // NOTE(rune): Bruger-specifik
    private final String userComment;
    private final int userColor;

    public Room(int id, String name, int size, int comfortCapacity, int fireCapacity, String comment, RoomType type, String userComment, int userColor)
    {
        this.id = id;
        this.name = name;
        this.size = size;
        this.comfortCapacity = comfortCapacity;
        this.fireCapacity = fireCapacity;
        this.comment = comment;
        this.type = type;
        this.userComment = userComment;
        this.userColor = userColor;
    }

    public Room(int id, String name, int size, int comfortCapacity, int fireCapacity, String comment, RoomType type)
    {
        this.id = id;
        this.name = name;
        this.size = size;
        this.comfortCapacity = comfortCapacity;
        this.fireCapacity = fireCapacity;
        this.comment = comment;
        this.type = type;
        this.userComment = "";
        this.userColor = -1;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public int getSize()
    {
        return size;
    }

    public int getComfortCapacity()
    {
        return comfortCapacity;
    }

    public int getFireCapacity()
    {
        return fireCapacity;
    }

    public String getComment()
    {
        return comment;
    }

    public RoomType getType()
    {
        return type;
    }

    @Override public boolean equals(Object obj)
    {
        if (obj instanceof Room other)
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
