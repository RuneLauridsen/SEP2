package booking.server.model.importFile;

import booking.shared.objects.Booking;

public class ImportFileRow
{
    private final int row;
    private final boolean isOverlapAllowed;
    private final Booking booking;

    public ImportFileRow(int row, boolean isOverlapAllowed, Booking booking)
    {
        this.row = row;
        this.isOverlapAllowed = isOverlapAllowed;
        this.booking = booking;
    }

    public int getRow()
    {
        return row;
    }

    public boolean isOverlapAllowed()
    {
        return isOverlapAllowed;
    }

    public Booking getBooking()
    {
        return booking;
    }
}
