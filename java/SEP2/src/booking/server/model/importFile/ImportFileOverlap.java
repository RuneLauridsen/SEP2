package booking.server.model.importFile;

import booking.shared.objects.Overlap;

import java.io.Serializable;

public class ImportFileOverlap implements Serializable
{
    private final int row;
    private final Overlap overlap;

    public ImportFileOverlap(int row, Overlap overlap)
    {
        this.row = row;
        this.overlap = overlap;
    }

    public int getRow()
    {
        return row;
    }

    public Overlap getOverlap()
    {
        return overlap;
    }

    @Override public String toString()
    {
        return "Row " + row + ": " + overlap;
    }
}
