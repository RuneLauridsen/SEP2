package booking.shared;

import booking.shared.objects.Room;
import booking.shared.objects.RoomType;

import java.io.Serializable;

public class UpdateRoomParameters implements Serializable
{
    private String newName;
    private int newSize;
    private int newComfortCapacity;
    private int newFireCapacity;
    private String newComment; // NOTE(rune): Global kommentar, ikke bruger-specifik.
    private RoomType newType;

    public UpdateRoomParameters(Room room)
    {
        this.newName = room.getName();
        this.newSize = room.getSize();
        this.newComfortCapacity = room.getComfortCapacity();
        this.newFireCapacity = room.getFireCapacity();
        this.newComment = room.getComment();
        this.newType = room.getType();
    }

    public String getNewName()
    {
        return newName;
    }

    public void setNewName(String newName)
    {
        this.newName = newName;
    }

    public int getNewSize()
    {
        return newSize;
    }

    public void setNewSize(int newSize)
    {
        this.newSize = newSize;
    }

    public int getNewComfortCapacity()
    {
        return newComfortCapacity;
    }

    public void setNewComfortCapacity(int newComfortCapacity)
    {
        this.newComfortCapacity = newComfortCapacity;
    }

    public int getNewFireCapacity()
    {
        return newFireCapacity;
    }

    public void setNewFireCapacity(int newFireCapacity)
    {
        this.newFireCapacity = newFireCapacity;
    }

    public String getNewComment()
    {
        return newComment;
    }

    public void setNewComment(String newComment)
    {
        this.newComment = newComment;
    }

    public RoomType getNewType()
    {
        return newType;
    }

    public void setNewType(RoomType newType)
    {
        this.newType = newType;
    }
}
