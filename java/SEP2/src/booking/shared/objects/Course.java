package booking.shared.objects;

import java.io.Serializable;

public class Course implements Serializable
{
    private final int id;
    private final String name;

    public Course(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
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

    @Override public String toString()
    {
        return name;
    }
}
