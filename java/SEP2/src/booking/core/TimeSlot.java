package booking.core;

import java.time.LocalTime;

public class TimeSlot
{
    private final int id;
    private final LocalTime start;
    private final LocalTime end;

    public TimeSlot(int id, LocalTime start, LocalTime end)
    {
        this.id = id;
        this.start = start;
        this.end = end;
    }

    public int getId()
    {
        return id;
    }

    public LocalTime getStart()
    {
        return start;
    }

    public LocalTime getEnd()
    {
        return end;
    }

    @Override public boolean equals(Object obj)
    {
        if (obj instanceof TimeSlot other)
        {
            return this.id == other.id;
        }
        else
        {
            return false;
        }
    }
}