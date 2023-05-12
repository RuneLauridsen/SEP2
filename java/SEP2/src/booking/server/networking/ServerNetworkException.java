package booking.server.networking;

public class ServerNetworkException extends Exception
{
    public ServerNetworkException()
    {
    }

    public ServerNetworkException(String message)
    {
        super(message);
    }

    public ServerNetworkException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
