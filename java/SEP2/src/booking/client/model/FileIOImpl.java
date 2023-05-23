package booking.client.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileIOImpl implements FileIO
{
    @Override public String readFile(String fileName) throws IOException
    {
        return Files.readString(Path.of(fileName));
    }
}
