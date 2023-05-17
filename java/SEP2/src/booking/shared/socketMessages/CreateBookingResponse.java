package booking.shared.socketMessages;

import booking.shared.objects.Overlap;

import java.util.List;

public class CreateBookingResponse extends Response
{
    // NOTE(rune): Hvis CreateRoomResponse.isOverlapAllowed var sat til true,
    // vil conflictingOverlaps altid være tom (antaget at brugeren har adgang
    // til at overlappe bookninger). Hvis man vil checke om der er overlap,
    // skal man derfor først send CreateRoomResponse med isOverlapAllowed=false,

    private final List<Overlap> conflictingOverlaps;

    public CreateBookingResponse(List<Overlap> conflictingOverlaps)
    {
        this.conflictingOverlaps = conflictingOverlaps;
    }

    public List<Overlap> getConflictingOverlaps()
    {
        return conflictingOverlaps;
    }

    public boolean wasBookingCreated()
    {
        return conflictingOverlaps.size() == 0;
    }
}
