package booking.client.networking;

import booking.shared.socketMessages.ErrorResponseReason;

// NOTE(rune): Når server svarer med ErrorResponse
public class ClientResponseException extends Exception
{
    private final ErrorResponseReason reason;

    public ClientResponseException(ErrorResponseReason reason)
    {
        this.reason = reason;
    }

    public ErrorResponseReason getReason()
    {
        return reason;
    }

    public String getMessage()
    {
        return reason.getMessage();
    }
}
