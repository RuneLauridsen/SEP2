package booking.shared.socketMessages;

import booking.shared.GetAvailableRoomsParameters;

import java.io.Serializable;

public class AvailableRoomsRequest implements Serializable
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
