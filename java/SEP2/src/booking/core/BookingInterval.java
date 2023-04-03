package booking.core;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class BookingInterval
{
    private LocalDate date;
    private LocalTime from;
    private LocalTime to;

    public BookingInterval(LocalDate date, LocalTime from, LocalTime to)
    {
        if (!from.isBefore(to))
        {
            throw new IllegalArgumentException("From time must be before to time");
        }

        this.date = date;
        this.from = from;
        this.to = to;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public LocalTime getFrom()
    {
        return from;
    }

    public void setFrom(LocalTime from)
    {
        this.from = from;
    }

    public LocalTime getTo()
    {
        return to;
    }

    public void setTo(LocalTime to)
    {
        this.to = to;
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
