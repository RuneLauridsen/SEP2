package booking.shared.socketMessages;

// NOTE(rune): Hvis der er noget galt med en request, sender serveren et ErrorResponse tilbage,
// med en af følgende ErrorResponseReasons:
public enum ErrorResponseReason
{
    ERROR_RESPONSE_REASON_NONE(""),

    // Client sendte en request klasse som serveren ikke kan håndtere.
    ERROR_RESPONSE_REASON_INVALID_REQUEST_TYPE("Invalid request type"),

    // Forkert brugernavn og adgangskode.
    ERROR_RESPONSE_REASON_INVALID_CREDENTIALS("Invalid credentials"),

    // F.eks. når studerende prøver at booke et medarbejderrum.
    ERROR_RESPONSE_REASON_ROOM_TYPE_NOT_ALLOWED("Room type not allowed");

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
