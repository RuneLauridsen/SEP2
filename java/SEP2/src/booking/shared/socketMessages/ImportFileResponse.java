package booking.shared.socketMessages;

import booking.server.model.importFile.ImportFileResult;

public class ImportFileResponse extends Response
{
    private final ImportFileResult result;

    public ImportFileResponse(ImportFileResult result)
    {
        this.result = result;
    }

    public ImportFileResult getResult()
    {
        return result;
    }
}
