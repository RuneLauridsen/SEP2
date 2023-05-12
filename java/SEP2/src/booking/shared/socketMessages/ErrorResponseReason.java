package booking.shared.socketMessages;

// NOTE(rune): Hvis der er noget galt med en request, sender serveren et ErrorResponse tilbage,
// med en af følgende ErrorResponseReasons:
public enum ErrorResponseReason
{
    // Ingen fejl, alt er OK
    ERROR_RESPONSE_REASON_NONE(""),

    // Client sendte en request klasse som serveren ikke kender/kan håndtere.
    ERROR_RESPONSE_REASON_INVALID_REQUEST_TYPE("Invalid request type"),

    // Forkert brugernavn og adgangskode.
    ERROR_RESPONSE_REASON_INVALID_CREDENTIALS("Invalid credentials"),

    // F.eks. når studerende prøver at booke et medarbejderrum.
    ERROR_RESPONSE_REASON_ROOM_TYPE_NOT_ALLOWED("Room type not allowed"),

    // F.eks. når studerende booker mere end to lokaler ad gangen.
    ERROR_RESPONSE_REASON_TOO_MANY_ACTIVE_BOOKINGS("Too many active bookings"),

    // F.eks. når studerende sletter en anden brugers booking.
    ERROR_RESPONSE_REASON_ACCESS_DENIED("Access denied"),

    ERROR_RESPONSE_REASON_ROOM_NOT_FOUND("Room not found"),

    ERROR_RESPONSE_REASON_USER_NOT_FOUND("User not found");

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
