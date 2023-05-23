package booking.server.model;

import booking.shared.socketMessages.ErrorResponseReason;

public class ServerModelException extends Exception
{
    private final ErrorResponseReason reason;

    public ServerModelException(ErrorResponseReason reason)
    {
        super(reason.getMessage());
        this.reason = reason;
    }

    public ServerModelException(ErrorResponseReason reason, Throwable cause)
    {
        super(reason.getMessage(), cause);
        this.reason = reason;
    }

    public ErrorResponseReason getReason()
    {
        return reason;
    }
}
