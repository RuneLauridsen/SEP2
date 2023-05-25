package booking.client.model;

import booking.server.model.importFile.ImportFileResult;

public interface ClientModelImport
{
    public ImportFileResult importFile(String fileName) throws ClientModelException;
}
