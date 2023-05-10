package booking.core;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class BookingInterval
{
    private final LocalDate date;
    private final LocalTime start;
    private final LocalTime end;

    public BookingInterval(LocalDate date, LocalTime from, LocalTime to)
    {
        this.date = Objects.requireNonNull(date);
        this.start = Objects.requireNonNull(from);
        this.end = Objects.requireNonNull(to);
    }

    public LocalDate getDate()
    {
        return date;
    }

    public LocalTime getStart()
    {
        return start;
    }

    public LocalTime getEnd()
    {
        return end;
    }

    public boolean isOverlapWith(LocalTime time)
    {
        boolean isAfterFrom = time.isAfter(start) || time.equals(start);
        boolean isBeforeTo = time.isBefore(end) || time.equals(end);

        if (isAfterFrom && isBeforeTo)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean isOverlapWith(BookingInterval other)
    {
        /*
                       ⬇ start1 10            ⬇ end1 16
            ------------------------------------------------------
                   ⬆ start2 09                     ⬆ end2 19
        */

        if (!date.equals(other.date))
        {
            return false;
        }

        if (isOverlapWith(other.start))
        {
            return false;
        }

        if (isOverlapWith(other.end))
        {
            return false;
        }

        return false;
    }

    @Override public String toString()
    {
        return date + " " + start + "-" + end;
    }
}
