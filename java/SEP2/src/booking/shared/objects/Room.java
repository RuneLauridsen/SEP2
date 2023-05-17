package booking.shared.objects;

import java.io.Serializable;

public class Room implements Serializable
{
    private final int id;
    private  String name;
    private  int size;
    private  int comfortCapacity;
    private  int fireCapacity;
    private  String comment;
    private  RoomType type;

    // NOTE(rune): Bruger-specifik
    private  String userComment;
    private  int userColor;


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

    public void setName(String name)
    {
        this.name = name;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public void setComfortCapacity(int comfortCapacity)
    {
        this.comfortCapacity = comfortCapacity;
    }

    public void setFireCapacity(int fireCapacity)
    {
        this.fireCapacity = fireCapacity;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public void setType(RoomType type)
    {
        this.type = type;
    }

    public void setUserComment(String userComment)
    {
        this.userComment = userComment;
    }

    public void setUserColor(int userColor)
    {
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

    public String getUserComment()
    {
        return userComment;
    }

    public int getUserColor()
    {
        return userColor;
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
