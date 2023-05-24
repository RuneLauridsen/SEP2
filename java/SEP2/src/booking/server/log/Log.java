package booking.server.log;

import java.util.Arrays;

public class Log
{
    public static void log(String message)
    {
        System.out.println(message);
    }

    public static void log(Exception e)
    {
        System.out.print(Arrays.toString(e.getStackTrace()));
    }
}
