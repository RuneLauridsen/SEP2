package booking.shared.socketMessages;

public class ImportFileRequest extends Request
{
    public final String fileContent;

    public ImportFileRequest(String fileContent)
    {
        this.fileContent = fileContent;
    }

    public String getFileContent()
    {
        return fileContent;
    }
}
