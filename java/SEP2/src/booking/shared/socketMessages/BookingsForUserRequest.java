package booking.shared.socketMessages;

import java.time.LocalDate;

public class BookingsForUserRequest extends Request
{
    private final String username;
    private final LocalDate from;
    private final LocalDate to;

    public BookingsForUserRequest(String username)
    {
        this.username = username;
        this.from = LocalDate.MIN;
        this.to = LocalDate.MAX;
    }

    public BookingsForUserRequest(String username, LocalDate from, LocalDate to)
    {
        this.username = username;
        this.from = from;
        this.to = to;
    }

    public String getUsername()
    {
        return username;
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
