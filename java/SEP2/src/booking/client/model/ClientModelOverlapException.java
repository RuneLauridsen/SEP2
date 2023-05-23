package booking.client.model;

import booking.shared.objects.Overlap;

import java.util.List;

public class ClientModelOverlapException extends Exception
{
    private final List<Overlap> overlaps;

    public ClientModelOverlapException(List<Overlap> overlaps)
    {
        this.overlaps = overlaps;
    }

    public List<Overlap> getOverlaps()
    {
        return overlaps;
    }
}
