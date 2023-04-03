package booking.core;

import java.time.LocalDateTime;

public class Booking
{
    private BookingInterval interval;
    private Bruger bruger;
    private Room room;

    public Booking(BookingInterval interval, Bruger bruger, Room room)
    {
        this.interval = interval;
        this.bruger = bruger;
        this.room = room;
    }

    // TODO(rune): Maybe add copy methods
    public BookingInterval getInterval()
    {
        return interval;
    }

    public void setInterval(BookingInterval interval)
    {
        this.interval = interval;
    }

    public Bruger getBruger()
    {
        return bruger;
    }

    public void setBruger(Bruger bruger)
    {
        this.bruger = bruger;
    }

    public Room getRoom()
    {
        return room;
    }

    public void setRoom(Room room)
    {
        this.room = room;
    }
}
