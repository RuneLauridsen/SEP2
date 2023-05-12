package booking.shared.socketMessages;

import booking.shared.GetAvailableRoomsParameters;

import java.util.Objects;

public class AvailableRoomsRequest extends Request
{
    private final GetAvailableRoomsParameters parameters;

    public AvailableRoomsRequest(GetAvailableRoomsParameters parameters)
    {
        this.parameters = Objects.requireNonNull(parameters);
    }

    public GetAvailableRoomsParameters getParameters()
    {
        return parameters;
    }
}
