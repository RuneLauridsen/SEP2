package booking.client.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileIOImpl implements FileIO
{
    @Override public String readFile(String fileName) throws IOException
    {
        return Files.readString(Path.of(fileName));
    }
}
