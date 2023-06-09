package booking.shared.socketMessages;

import booking.shared.objects.Overlap;

import java.util.List;

public class CreateBookingResponse extends Response
{
    private final List<Overlap> overlaps;

    public CreateBookingResponse(List<Overlap> overlaps)
    {
        this.overlaps = overlaps;
    }

    public List<Overlap> getOverlaps()
    {
        return overlaps;
    }
}
