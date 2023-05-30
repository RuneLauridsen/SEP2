package booking.shared.objects;

import java.io.Serializable;
import java.time.LocalTime;

public class TimeSlot implements Serializable
{
    private final LocalTime start;
    private final LocalTime end;

    public TimeSlot(LocalTime start, LocalTime end)
    {
        this.start = start;
        this.end = end;
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
            return this.start.equals(other.start)
                && this.end.equals(other.end);
        }
        else
        {
            return false;
        }
    }

    @Override
    public String toString() {
        return start + "-" + end;
    }
}
