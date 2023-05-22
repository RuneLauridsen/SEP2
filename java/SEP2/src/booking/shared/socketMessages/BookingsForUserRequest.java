package booking.shared.socketMessages;

import booking.shared.objects.User;

import java.time.LocalDate;

public class BookingsForUserRequest extends Request
{
    private final User user;
    private final LocalDate from;
    private final LocalDate to;

    public BookingsForUserRequest(User user, LocalDate from, LocalDate to)
    {
        this.user = user;
        this.from = from;
        this.to = to;
    }

    public User getUser()
    {
        return user;
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
