package booking.shared.socketMessages;

import booking.shared.CreateBookingParameters;

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
