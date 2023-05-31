package booking.server.model.importFile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImportFileResult implements Serializable
{
    // NOTE: Forkert filformat, lokale ikke fundet, io error osv.
    private final List<ImportFileError> errors;

    // NOTE: Detaljer om overlap med eksisterende bookinger
    private final List<ImportFileOverlap> overlaps;

    public ImportFileResult()
    {
        this.errors = new ArrayList<>();
        this.overlaps = new ArrayList<>();
    }

    public List<ImportFileError> getErrors()
    {
        return errors;
    }

    public List<ImportFileOverlap> getOverlaps()
    {
        return overlaps;
    }

    public void addError(ImportFileError error)
    {
        errors.add(error);
    }

    public void addOverlap(ImportFileOverlap overlap)
    {
        overlaps.add(overlap);
    }

    public boolean isOk()
    {
        return (errors.size() == 0) && (overlaps.size() == 0);
    }
}
