package test;

import booking.client.model.FileIO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class FakeFileIO implements FileIO
{
    @Override public String readFile(String fileName) throws IOException
    {
        try
        {
            URL ulr = getClass().getResource(Path.of(fileName).getFileName().toString());
            if (ulr == null)
            {
                throw new FileNotFoundException(fileName + " not found");
            }
            else
            {
                return Files.readString(Path.of(ulr.toURI()));
            }
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
    }
}
