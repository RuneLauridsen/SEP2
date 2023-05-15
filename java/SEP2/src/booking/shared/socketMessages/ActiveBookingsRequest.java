package booking.shared.socketMessages;

import java.time.LocalDate;

public class ActiveBookingsRequest extends Request
{
    private LocalDate start;
    private LocalDate end;

    public ActiveBookingsRequest(LocalDate start, LocalDate end)
    {
        this.start = start;
        this.end = end;
    }

    public LocalDate getStart()
    {
        return start;
    }

    public LocalDate getEnd()
    {
        return end;
    }
}
