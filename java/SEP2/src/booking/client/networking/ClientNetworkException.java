package booking.client.networking;

// NOTE(rune): Når noget går galt i BookingClientNetworkLayer
public class ClientNetworkException extends Exception
{
    public ClientNetworkException()
    {
    }

    public ClientNetworkException(String message)
    {
        super(message);
    }

    public ClientNetworkException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
