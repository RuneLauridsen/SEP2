package booking.shared.socketMessages;

import booking.shared.GetAvailableRoomsParameters;

public class AvailableRoomsRequest extends Request
{
    private final GetAvailableRoomsParameters parameters;

    public AvailableRoomsRequest(GetAvailableRoomsParameters parameters)
    {
        this.parameters = parameters;
    }

    public GetAvailableRoomsParameters getParameters()
    {
        return parameters;
    }
}
