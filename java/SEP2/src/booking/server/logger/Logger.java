package booking.server.logger;

public class Logger
{
    public static void log(String message)
    {
        System.out.println(message);
    }

    public static void log(Exception e)
    {
        e.printStackTrace();
    }
}
