package booking.shared.socketMessages;

import booking.core.User;

import java.io.Serializable;

public class ConnectionResponse implements Serializable
{
    private final boolean ok;
    private final User user;

    public ConnectionResponse(boolean ok, User user)
    {
        this.ok = ok;
        this.user = user;
    }

    public boolean isOk()
    {
        return ok;
    }

    public User getUser()
    {
        return user;
    }
}
