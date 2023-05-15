package booking.shared.socketMessages;

import booking.shared.objects.User;

import java.util.Objects;

public class ConnectionResponse extends Response
{
    private final User user;

    public ConnectionResponse(User user)
    {
        this.user = Objects.requireNonNull(user);
    }

    public User getUser()
    {
        return user;
    }
}
