package booking.client.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;

public interface FileIO
{
    public String readFile(String fileName) throws IOException;
}
