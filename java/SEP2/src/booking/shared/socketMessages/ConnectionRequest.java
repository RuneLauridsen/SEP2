package booking.shared.socketMessages;

import java.util.Objects;

public class ConnectionRequest extends Request
{
    private final String username;
    private final String password;

    public ConnectionRequest(String username, String password)
    {
        this.username = Objects.requireNonNull(username);
        this.password = Objects.requireNonNull(password);
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }
}
