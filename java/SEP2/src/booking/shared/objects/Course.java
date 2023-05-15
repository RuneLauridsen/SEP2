package booking.shared.objects;

import java.io.Serializable;

public class Course implements Serializable
{
    private final int id;
    private final String name;
    private final int timeSlotCount;

    public Course(int id, String name, int timeSlotCount)
    {
        this.id = id;
        this.name = name;
        this.timeSlotCount = timeSlotCount;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public int getTimeSlotCount()
    {
        return timeSlotCount;
    }

    @Override public boolean equals(Object obj)
    {
        if (obj instanceof Course other)
        {
            return this.id == other.id;
        }
        else
        {
            return false;
        }
    }
}
