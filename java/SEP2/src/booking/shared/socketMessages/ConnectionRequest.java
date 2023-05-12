package booking.shared.socketMessages;

public class ConnectionRequest extends Request
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
