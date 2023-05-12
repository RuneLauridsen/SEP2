package booking.shared;

import booking.core.BookingInterval;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

// NOTE(rune): Disse parameter/argument sendes ofte rundt fra mellem
// forskellige metoder, så det er nemmere bare at gemme dem sammen.
public class GetAvailableRoomsParameters implements Serializable
{
    private BookingInterval interval;
    private Integer minCapacity;
    private Integer maxCapacity;
    private Character building;
    private Integer floor;

    public GetAvailableRoomsParameters(BookingInterval interval)
    {
        Objects.requireNonNull(interval);

        this.interval = interval;
    }

    public GetAvailableRoomsParameters(LocalDate date, LocalTime from, LocalTime to)
    {
        Objects.requireNonNull(date);
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);

        this.interval = new BookingInterval(date, from, to);
    }

    public BookingInterval getInterval()
    {
        return interval;
    }

    public void setInterval(BookingInterval interval)
    {
        Objects.requireNonNull(interval);
        this.interval = interval;
    }

    public Integer getMinCapacity()
    {
        return minCapacity;
    }

    public void setMinCapacity(Integer minCapacity)
    {
        this.minCapacity = minCapacity;
    }

    public Integer getMaxCapacity()
    {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity)
    {
        this.maxCapacity = maxCapacity;
    }

    public Character getBuilding()
    {
        return building;
    }

    public void setBuilding(Character building)
    {
        this.building = building;
    }

    public Integer getFloor()
    {
        return floor;
    }

    public void setFloor(Integer floor)
    {
        this.floor = floor;
    }
}
