package booking.shared.socketMessages;

import java.io.Serializable;

public class ConnectionRequest implements Serializable
{
    private final String username;
    private final String password;

    public ConnectionRequest(String username, String password)
    {
        this.username = username;
        this.password = password;
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
