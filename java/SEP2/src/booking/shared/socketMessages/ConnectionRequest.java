package booking.shared.socketMessages;

import java.util.Objects;

public class ConnectionRequest extends Request
{
    private final int viaid;
    private final String password;

    public ConnectionRequest(int viaid, String password)
    {
        this.viaid = viaid;
        this.password = Objects.requireNonNull(password);
    }

    public int getViaid()
    {
        return viaid;
    }

    public String getPassword()
    {
        return password;
    }
}
