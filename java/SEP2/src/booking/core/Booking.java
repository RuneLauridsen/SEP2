package booking.core;

public class Booking
{
    private BookingInterval interval;
    private User bruger;
    private Room room;

    public Booking(BookingInterval interval, User bruger, Room room)
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

    public User getBruger()
    {
        return bruger;
    }

    public void setBruger(User bruger)
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
