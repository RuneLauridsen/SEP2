package booking.shared.socketMessages;

// NOTE(rune): Hvis der er noget galt med en request, sender serveren et ErrorResponse tilbage,
// med en af følgende ErrorResponseReasons:
public enum ErrorResponseReason
{
    ERROR_RESPONSE_REASON_NONE(""),
    ERROR_RESPONSE_REASON_INVALID_REQUEST_TYPE("Invalid request type"),
    ERROR_RESPONSE_REASON_INVALID_CREDENTIALS("Invalid credentials");

    private final String message;

    private ErrorResponseReason(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }
}
