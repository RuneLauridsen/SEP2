package booking.shared.socketMessages;

import java.time.LocalDate;

public class BookingsForRoomRequest extends Request
{
    private final String roomName;
    private final LocalDate from;
    private final LocalDate to;

    public BookingsForRoomRequest(String roomName, LocalDate from, LocalDate to)
    {
        this.roomName = roomName;
        this.from = from;
        this.to = to;
    }

    public String getRoomName()
    {
        return roomName;
    }

    public LocalDate getFrom()
    {
        return from;
    }

    public LocalDate getTo()
    {
        return to;
    }
}
