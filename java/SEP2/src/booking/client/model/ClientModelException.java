package booking.client.model;

public class ClientModelException extends Exception
{
    public ClientModelException(String message)
    {
        super(message);
    }

    public ClientModelException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
