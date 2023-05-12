package booking.shared.socketMessages;

import java.io.Serializable;

// NOTE(rune): Parent klasse til andre XyzResponse klasser,
// da de alle skal kunne at returnere fejlbeskeder.
public abstract class Response implements Serializable
{
    private String errorMessage;

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public boolean isOk()
    {
        return errorMessage == null;
    }
}
