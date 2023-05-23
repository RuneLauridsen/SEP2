package booking.server.model.importFile;

import java.io.Serializable;

public class ImportFileError implements Serializable
{
    // NOTE(rune): Hvis row = 0 stammer fejlen ikke fra en bestemt række, f.eks. FileNotFound
    private final int row;
    private final String message;

    public ImportFileError(String message)
    {
        this.row = 0;
        this.message = message;
    }

    public ImportFileError(int row, String message)
    {
        this.row = row;
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }

    @Override public String toString()
    {
        if (row == 0)
        {
            return message;
        }
        else
        {
            return "Row " + row + ": " + message;
        }
    }
}
