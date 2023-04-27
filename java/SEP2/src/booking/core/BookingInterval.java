package booking.core;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class BookingInterval
{
    private final LocalDate date;
    private final LocalTime from;
    private final LocalTime to;

    public BookingInterval(LocalDate date, LocalTime from, LocalTime to)
    {
        this.date = Objects.requireNonNull(date);
        this.from = Objects.requireNonNull(from);
        this.to = Objects.requireNonNull(to);
    }

    public LocalDate getDate()
    {
        return date;
    }

    public LocalTime getFrom()
    {
        return from;
    }

    public LocalTime getTo()
    {
        return to;
    }

    public boolean isOverlapWith(LocalTime time)
    {
        boolean isAfterFrom = time.isAfter(from) || time.equals(from);
        boolean isBeforeTo = time.isBefore(to) || time.equals(to);

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

        if (isOverlapWith(other.from))
        {
            return false;
        }

        if (isOverlapWith(other.to))
        {
            return false;
        }

        return false;
    }
}
