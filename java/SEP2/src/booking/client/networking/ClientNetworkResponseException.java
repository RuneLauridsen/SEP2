package booking.client.networking;

import booking.shared.socketMessages.ErrorResponseReason;

// NOTE: Når server svarer med ErrorResponse
public class ClientNetworkResponseException extends Exception
{
    private final ErrorResponseReason reason;

    public ClientNetworkResponseException(ErrorResponseReason reason)
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
