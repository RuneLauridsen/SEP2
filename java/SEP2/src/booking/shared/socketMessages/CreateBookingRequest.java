package booking.shared.socketMessages;

import booking.shared.CreateBookingParameters;
import booking.shared.objects.BookingInterval;
import booking.shared.objects.Room;
import booking.shared.objects.UserGroup;

public class CreateBookingRequest extends Request
{
    private final CreateBookingParameters parameters;

    public CreateBookingRequest(CreateBookingParameters parameters)
    {
        this.parameters = parameters;
    }

    public CreateBookingParameters getParameters()
    {
        return parameters;
    }
}
