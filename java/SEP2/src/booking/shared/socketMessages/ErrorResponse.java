package booking.shared.socketMessages;

import java.util.Objects;

public class ErrorResponse extends Response
{
    private final ErrorResponseReason reason;

    public ErrorResponse(ErrorResponseReason reason)
    {
        this.reason = Objects.requireNonNull(reason);
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
